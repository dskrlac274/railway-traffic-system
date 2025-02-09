package upravljaci;

import composite.EtapaList;
import composite.VlakKompozit;
import composite.VozniRedKompozit;
import jezgra.*;
import modeli.CijenaKonfiguracija;
import modeli.stanica.Stanica;
import modeli.vozni_red.VrstaVlakaVozniRed;
import pomagaci.TablicaPomagac;
import strategy.*;
import visitor.PregledVoznogRedaVlakaVisitor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UKP2SUpravitelj extends Upravitelj {
    private static final Pattern UKP2S_PREDLOZAK = Pattern.compile(
            "^UKP2S\\s+(?<polazna>.+)\\s+-\\s+(?<odredisna>.+)" +
                    "\\s+-\\s+(?<datum>[^-]+)" +
                    "\\s+-\\s+(?<odVr>(?:[01]?\\d|2[0-3]):[0-5]\\d)" +
                    "\\s+-\\s+(?<doVr>(?:[01]?\\d|2[0-3]):[0-5]\\d)" +
                    "\\s+-\\s+(?<nacin>(WM|B|V))$"
    );

    private static final List<String> ZAGLAVLJE_KARTE = List.of(
            "Vlak",
            "Relacija",
            "Datum",
            "Vrijeme polaska",
            "Vrijeme dolaska",
            "Izvorna cijena",
            "Popust (€)",
            "Konačna cijena",
            "Način kupnje"
    );

    private final ZeljeznickiPromet zeljeznickiPromet;
    private final Set<String> visitedGlobal = new HashSet<>();

    public UKP2SUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return UKP2S_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = UKP2S_PREDLOZAK.matcher(komanda);
        if (!matcher.matches()) {
            System.out.println("Neispravna komanda.");
            return;
        }

        CijenaKonfiguracija cfg = zeljeznickiPromet.getCijenaKonfiguracija();
        visitedGlobal.clear();

        if (cfg == null) {
            System.out.println("Kupnja karti trenutno nije moguća.");
            return;
        }

        String polaznaStan = matcher.group("polazna");
        String odredisnaStan = matcher.group("odredisna");
        String datumStr = matcher.group("datum");
        String odVrStr = matcher.group("odVr");
        String doVrStr = matcher.group("doVr");
        String nacinKupnje = matcher.group("nacin");

        LocalDate datumPutovanja;
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            datumPutovanja = LocalDate.parse(datumStr, df);
        } catch (Exception e) {
            System.out.println("Neispravan format datuma (očekivano dd.MM.yyyy.).");
            return;
        }

        if (datumPutovanja.isBefore(LocalDate.now())) {
            System.out.println("Nije moguće prikazati karte za datum koji je već prošao.");
            return;
        }

        LocalTime odTime, doTime;
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
            odTime = LocalTime.parse(odVrStr, timeFormatter);
            doTime = LocalTime.parse(doVrStr, timeFormatter);
        } catch (Exception e) {
            System.out.println("Neispravan format vremena (očekivano H:mm).");
            return;
        }

        VozniRedKompozit vrKompozit = zeljeznickiPromet.getVozniRedKompozit();
        if (vrKompozit == null) {
            System.out.println("Nema voznog reda u sustavu.");
            return;
        }

        Map<String, VlakKompozit> vlakovi = vrKompozit.dohvatiJedinstvenuDjecu()
                .entrySet()
                .stream()
                .filter(e -> e.getValue() instanceof VlakKompozit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> (VlakKompozit) e.getValue()
                ));

        Map<String, List<EtapaList>> etapeVlakova = new HashMap<>();
        for (var entry : vlakovi.entrySet()) {
            String oznakaVlaka = entry.getKey();
            VlakKompozit vk = entry.getValue();

            PregledVoznogRedaVlakaVisitor visitor = new PregledVoznogRedaVlakaVisitor(oznakaVlaka);
            vk.prihvati(visitor);
            etapeVlakova.put(oznakaVlaka, visitor.etape);
        }

        AlgoritamPutanjeImplementator implementacija = new GrafBFSImplementator(zeljeznickiPromet);
        NajkraciPutAbstractor najkraciPutAbstractor = new NajkraciPutAbstractor(implementacija);

        List<Stanica> svePutanje = najkraciPutAbstractor.pronadjiNajkraciPut(polaznaStan, odredisnaStan);

        if (svePutanje.isEmpty()) {
            System.out.println("Nema pronađenih putanja između " + polaznaStan + " i " + odredisnaStan + ".");
            return;
        }

        List<Map<String, List<Stanica>>> sveKombinacije = new ArrayList<>();
        pokrijPutanjuRekurzivno(svePutanje, 0, null, etapeVlakova,
                new LinkedHashMap<>(), sveKombinacije, datumPutovanja);

        sveKombinacije.sort(Comparator.comparingInt(Map::size));

        var filtriraneKombinacije = sveKombinacije.stream()
                .filter(komb -> provjeriVremenaCombo(komb, odTime, doTime, vlakovi))
                .filter(komb -> jeVremenskiIzvodljiva(komb, vlakovi))
                .toList();

        Set<Map<String, List<Stanica>>> jedinstveneKombinacije = new HashSet<>();

        Optional<Map<String, List<Stanica>>> kombinacijaBrzi = filtriraneKombinacije.stream()
                .filter(k -> k.keySet().stream()
                        .anyMatch(oznaka -> {
                            VlakKompozit vk = vlakovi.get(oznaka);
                            return vk != null && vrstaVlakaJeBrzi(vk.getVrstaVlaka());
                        }))
                .filter(jedinstveneKombinacije::add)
                .findFirst();

        Optional<Map<String, List<Stanica>>> kombinacijaUbrzani = filtriraneKombinacije.stream()
                .filter(k -> k.keySet().stream()
                        .anyMatch(oznaka -> {
                            VlakKompozit vk = vlakovi.get(oznaka);
                            return vk != null && vrstaVlakaJeUbrzani(vk.getVrstaVlaka());
                        }))
                .filter(jedinstveneKombinacije::add)
                .findFirst();

        Optional<Map<String, List<Stanica>>> kombinacijaNormalni = filtriraneKombinacije.stream()
                .filter(k -> k.keySet().stream()
                        .anyMatch(oznaka -> {
                            VlakKompozit vk = vlakovi.get(oznaka);
                            return vk != null && vrstaVlakaJeNormalni(vk.getVrstaVlaka());
                        }))
                .filter(jedinstveneKombinacije::add)
                .findFirst();

        sveKombinacije = new ArrayList<>();
        kombinacijaBrzi.ifPresent(sveKombinacije::add);
        kombinacijaUbrzani.ifPresent(sveKombinacije::add);
        kombinacijaNormalni.ifPresent(sveKombinacije::add);

        double ukupnoIzvorna = 0.0;
        double ukupnoPopust = 0.0;
        double ukupnoKonacna = 0.0;

        if (sveKombinacije.isEmpty()) {
            System.out.println("  -> Nema kombinacija presjedanja koje pokrivaju ovu putanju unutar zadanog vremena.");
        } else {
            int br = 1;
            for (Map<String, List<Stanica>> kombinacija : sveKombinacije) {
                System.out.println("     Kombinacija " + (br++) + ":");
                List<List<String>> retciTablice = new ArrayList<>();
                for (Map.Entry<String, List<Stanica>> dionica : kombinacija.entrySet()) {
                    String vlak = dionica.getKey();
                    List<Stanica> staniceSegment = dionica.getValue();

                    VlakKompozit vk = vlakovi.get(vlak);
                    String vrstaVlaka = (vk != null) ? vk.getVrstaVlaka() : "N";

                    Stanica prvaStanica = staniceSegment.getFirst();
                    Stanica zadnjaStanica = staniceSegment.getLast();

                    double ukupniKilometri = staniceSegment.getLast().getUdaljenostOdPocetne();

                    double izvornaCijena = izracunajOsnovnuCijenu(ukupniKilometri, vrstaVlaka);
                    double konacnaCijena = izracunajKonacnuCijenu(ukupniKilometri, vrstaVlaka, datumPutovanja, nacinKupnje);

                    double popust = izvornaCijena - konacnaCijena;

                    ukupnoIzvorna += izvornaCijena;
                    ukupnoPopust += popust;
                    ukupnoKonacna += konacnaCijena;

                    String relacija = prvaStanica.getNaziv() + " - " + zadnjaStanica.getNaziv();
                    var vrijemePolaska = staniceSegment.getFirst().getVrijemeVrstaVlaka(vrstaVlaka);
                    var vrijemeDolaska = staniceSegment.getLast().getVrijemeVrstaVlaka(vrstaVlaka);

                    DateTimeFormatter formatterPutovanje = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                    String putovanjeFormatted = datumPutovanja.format(formatterPutovanje);

                    List<String> red = List.of(
                            vlak,
                            relacija,
                            putovanjeFormatted,
                            (vrijemePolaska != null) ? vrijemePolaska.toString() : "",
                            (vrijemeDolaska != null) ? vrijemeDolaska.toString() : "",
                            String.format("%.2f €", izvornaCijena),
                            String.format("%.2f €", popust),
                            String.format("%.2f €", konacnaCijena),
                            nacinKupnje
                    );
                    retciTablice.add(red);
                }

                List<String> rowZbroj = List.of(
                        "UKUPNO", "", "", "", "",
                        String.format("%.2f €", ukupnoIzvorna),
                        String.format("%.2f €", ukupnoPopust),
                        String.format("%.2f €", ukupnoKonacna),
                        nacinKupnje
                );

                ukupnoIzvorna = 0.0;
                ukupnoPopust = 0.0;
                ukupnoKonacna = 0.0;
                retciTablice.add(rowZbroj);

                TablicaPomagac.prikaziTablicu(ZAGLAVLJE_KARTE, retciTablice);
            }
        }
    }

    private void pokrijPutanjuRekurzivno(
            List<Stanica> putStanica,
            int index,
            String zadnjiVlak,
            Map<String, List<EtapaList>> etapeVlakova,
            Map<String, List<Stanica>> trenutnaSekvenca,
            List<Map<String, List<Stanica>>> rezultati,
            LocalDate datumPutovanja
    ) {
        if (index >= putStanica.size()) {
            rezultati.add(new LinkedHashMap<>(trenutnaSekvenca));
            return;
        }

        Stanica currStan = putStanica.get(index);
        String currStanNaziv = currStan.getNaziv();

        String kljuc = index + "|" + currStanNaziv + "|" + (zadnjiVlak == null ? "null" : zadnjiVlak);
        if (visitedGlobal.contains(kljuc)) {
            return;
        }
        visitedGlobal.add(kljuc);

        for (var entry : etapeVlakova.entrySet()) {
            String oznakaVlaka = entry.getKey();
            List<EtapaList> etape = entry.getValue();

            DayOfWeek dan = datumPutovanja.getDayOfWeek();
            etape = etape.stream()
                    .filter(etapa -> etapa.getDaniUTjednu().contains(mapirajDayOfWeek(dan.getValue())))
                    .collect(Collectors.toList());

            List<Stanica> ruteVlaka = dohvatiRutuVlaka(etape);

            if (ruteVlaka.size() >= 2) {
                var prva = ruteVlaka.getFirst();
                var druga = ruteVlaka.getLast();
                if (prva.getUdaljenostOdPocetne() > druga.getUdaljenostOdPocetne()) {
                    continue;
                }
            }

            int startPos = indexOfFromByNaziv(ruteVlaka, currStanNaziv, 0);
            while (startPos != -1) {
                int routeIdx = startPos;
                int putIdx = index;

                while (routeIdx < ruteVlaka.size()
                        && putIdx < putStanica.size()
                        && ruteVlaka.get(routeIdx).getNaziv().equals(putStanica.get(putIdx).getNaziv())) {
                    routeIdx++;
                    putIdx++;
                }

                int pokriveno = putIdx - ((index - 1 >= 0) ? (index - 1) : index);
                if (pokriveno > 0) {
                    int adjustedStartPos = (startPos - 1 >= 0) ? startPos - 1 : startPos;
                    LinkedHashMap<String, Stanica> mapaStanica = ruteVlaka.subList(adjustedStartPos, routeIdx)
                            .stream()
                            .filter(st -> putStanica.stream()
                                    .anyMatch(ps -> ps.getNaziv().equals(st.getNaziv())))
                            .collect(Collectors.toMap(
                                    Stanica::getNaziv,
                                    st -> st,
                                    (existing, replacement) -> replacement,
                                    LinkedHashMap::new
                            ));

                    List<Stanica> segmentStanice = new ArrayList<>(mapaStanica.values());

                    if (segmentStanice.size() < 2 ||
                            segmentStanice.getFirst().getNaziv().equals(
                                    segmentStanice.getLast().getNaziv()
                            )) {
                        startPos = indexOfFromByNaziv(ruteVlaka, currStanNaziv, startPos + 1);
                        continue;
                    }

                    trenutnaSekvenca.put(oznakaVlaka, new ArrayList<>(segmentStanice));

                    pokrijPutanjuRekurzivno(putStanica, putIdx, oznakaVlaka,
                            etapeVlakova, trenutnaSekvenca, rezultati, datumPutovanja);
                    trenutnaSekvenca.remove(oznakaVlaka);
                }

                startPos = indexOfFromByNaziv(ruteVlaka, currStanNaziv, startPos + 1);
            }
        }
    }

    private boolean jeVremenskiIzvodljiva(Map<String, List<Stanica>> kombinacija, Map<String, VlakKompozit> vlakovi) {
        List<Map.Entry<String, List<Stanica>>> segmenti = new ArrayList<>(kombinacija.entrySet());
        for (int i = 1; i < segmenti.size(); i++) {
            Map.Entry<String, List<Stanica>> prethodniSegment = segmenti.get(i - 1);
            Map.Entry<String, List<Stanica>> trenutniSegment = segmenti.get(i);

            List<Stanica> stanicePrethodni = prethodniSegment.getValue();
            List<Stanica> staniceTrenutni = trenutniSegment.getValue();

            if (stanicePrethodni.isEmpty() || staniceTrenutni.isEmpty()) continue;

            Stanica zadnjaPrethodna = stanicePrethodni.getLast();
            Stanica prvaTrenutna = staniceTrenutni.getFirst();

            String prethodniVlakOznaka = prethodniSegment.getKey();
            String trenutniVlakOznaka = trenutniSegment.getKey();

            String vrstaPrethodni = vlakovi.containsKey(prethodniVlakOznaka) ? vlakovi.get(prethodniVlakOznaka).getVrstaVlaka() : "N";
            String vrstaTrenutni = vlakovi.containsKey(trenutniVlakOznaka) ? vlakovi.get(trenutniVlakOznaka).getVrstaVlaka() : "N";

            LocalTime vrijemeDolaskaPrethodni = zadnjaPrethodna.getVrijemeVrstaVlaka(vrstaPrethodni);
            LocalTime vrijemePolaskaTrenutni = prvaTrenutna.getVrijemeVrstaVlaka(vrstaTrenutni);

            if (vrijemeDolaskaPrethodni == null || vrijemePolaskaTrenutni == null || vrijemeDolaskaPrethodni.isAfter(vrijemePolaskaTrenutni)) {
                return false;
            }
        }
        return true;
    }

    private boolean provjeriVremenaCombo(
            Map<String, List<Stanica>> kombinacija,
            LocalTime odTime,
            LocalTime doTime,
            Map<String, VlakKompozit> vlakovi
    ) {
        if (kombinacija.isEmpty()) return false;

        var iterator = kombinacija.entrySet().iterator();
        Map.Entry<String, List<Stanica>> prvaMap = iterator.hasNext() ? iterator.next() : null;
        if (prvaMap == null) return false;

        String vlakPrvi = prvaMap.getKey();
        List<Stanica> stanicePrve = prvaMap.getValue();
        if (stanicePrve.isEmpty()) return false;
        Stanica stPocetna = stanicePrve.getFirst();

        Map.Entry<String, List<Stanica>> zadnjaMap = null;
        for (Map.Entry<String, List<Stanica>> entry : kombinacija.entrySet()) {
            zadnjaMap = entry;
        }
        if (zadnjaMap == null) return false;

        String vlakZadnji = zadnjaMap.getKey();
        List<Stanica> staniceZadnje = zadnjaMap.getValue();
        if (staniceZadnje.isEmpty()) return false;
        Stanica stZavrsna = staniceZadnje.getLast();

        String vrstaPrvi = vlakovi.containsKey(vlakPrvi) ? vlakovi.get(vlakPrvi).getVrstaVlaka() : "N";
        String vrstaZadnji = vlakovi.containsKey(vlakZadnji) ? vlakovi.get(vlakZadnji).getVrstaVlaka() : "N";

        LocalTime vrijemePolaska = stPocetna.getVrijemeVrstaVlaka(vrstaPrvi);
        LocalTime vrijemeDolaska = stZavrsna.getVrijemeVrstaVlaka(vrstaZadnji);

        if (vrijemePolaska == null || vrijemeDolaska == null) return false;

        return !vrijemePolaska.isBefore(odTime) && !vrijemeDolaska.isAfter(doTime);
    }

    private List<Stanica> dohvatiRutuVlaka(List<EtapaList> etape) {
        List<Stanica> rezultat = new ArrayList<>();
        for (EtapaList et : etape) {
            List<Stanica> stanice = new ArrayList<>(et.getPruga().getStanice());

            if (et.getSmjer().equals("O")) {
                Collections.reverse(stanice);
            }

            rezultat.addAll(stanice);
        }
        return rezultat;
    }

    private String mapirajDayOfWeek(int dayOfWeekValue) {
        return switch (dayOfWeekValue) {
            case 1 -> "Po";
            case 2 -> "U";
            case 3 -> "Sr";
            case 4 -> "Č";
            case 5 -> "Pe";
            case 6 -> "Su";
            case 7 -> "N";
            default -> "?";
        };
    }

    private int indexOfFromByNaziv(List<Stanica> list, String naziv, int fromIndex) {
        for (int i = fromIndex; i < list.size(); i++) {
            if (list.get(i).getNaziv().equalsIgnoreCase(naziv)) {
                return i;
            }
        }
        return -1;
    }

    private double izracunajOsnovnuCijenu(double km, String vrstaVlaka) {
        CijenaKonfiguracija cfg = zeljeznickiPromet.getCijenaKonfiguracija();
        double cijenaPoKm;

        if (vrstaVlaka.equals(VrstaVlakaVozniRed.UBRZANI.getOpis())) {
            cijenaPoKm = cfg.getCijenaUbrzani();
        } else if (vrstaVlaka.equals(VrstaVlakaVozniRed.BRZI.getOpis())) {
            cijenaPoKm = cfg.getCijenaBrzi();
        } else {
            cijenaPoKm = cfg.getCijenaNormalni();
        }

        return km * cijenaPoKm;
    }

    private double izracunajKonacnuCijenu(
            double km,
            String vrstaVlaka,
            LocalDate datumPutovanja,
            String nacinKupnje
    ) {
        CijenaKonfiguracija cfg = zeljeznickiPromet.getCijenaKonfiguracija();
        double cijenaPoKm;
        if (vrstaVlaka.equals(VrstaVlakaVozniRed.UBRZANI.getOpis())) {
            cijenaPoKm = cfg.getCijenaUbrzani();
        } else if (vrstaVlaka.equals(VrstaVlakaVozniRed.BRZI.getOpis())) {
            cijenaPoKm = cfg.getCijenaBrzi();
        } else {
            cijenaPoKm = cfg.getCijenaNormalni();
        }

        ICijenaStrategy strategija;
        switch (nacinKupnje) {
            case "WM" -> strategija = new WebMobilnaCijenaStrategy();
            case "V" -> strategija = new KupovinaUVlakuCijenaStrategy();
            default -> strategija = new BlagajnaCijenaStrategy();
        }

        return new CjenikContext(strategija).izracunajCijenu(km, datumPutovanja, cijenaPoKm, cfg);
    }

    private boolean vrstaVlakaJeBrzi(String vrsta) {
        return VrstaVlakaVozniRed.BRZI.getOpis().equals(vrsta);
    }

    private boolean vrstaVlakaJeUbrzani(String vrsta) {
        return VrstaVlakaVozniRed.UBRZANI.getOpis().equals(vrsta);
    }

    private boolean vrstaVlakaJeNormalni(String vrsta) {
        return VrstaVlakaVozniRed.NORMALNI.getOpis().equals(vrsta);
    }
}
