package command;

import composite.VlakKompozit;
import composite.VozniRedKomponenta;
import composite.VozniRedKompozit;
import jezgra.ZeljeznickiPromet;
import memento.KupovinaKarteMemento;
import memento.KupovinaKarteOriginator;
import modeli.CijenaKonfiguracija;
import modeli.KupovinaKarte;
import modeli.pruga.Pruga;
import modeli.pruga.SegmentPruge;
import modeli.stanica.Stanica;
import modeli.stanica.StatusPrugeStanice;
import modeli.vozni_red.VrstaVlakaVozniRed;
import pomagaci.TablicaPomagac;
import strategy.*;
import upravljaci.IKKPVUpravitelj;
import visitor.PregledVlakovaFilterVisitor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static upravljaci.KKPV2SUpravitelj.KKPV2S_PREDLOZAK;
import static upravljaci.KKPV2SUpravitelj.ZAGLAVLJE_KARTE;

public class KarteReceiver {

    private final ZeljeznickiPromet zeljeznickiPromet;

    public KarteReceiver(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    public void kupiKartu(String komanda) {
        Matcher matcher = KKPV2S_PREDLOZAK.matcher(komanda);
        if (!matcher.matches()) {
            System.out.println("Neispravna komanda.");
            return;
        }

        CijenaKonfiguracija cfg = zeljeznickiPromet.getCijenaKonfiguracija();

        if (cfg == null) {
            System.out.println("Kupnja karti trenutno nije moguća.");
            return;
        }

        String oznakaVlaka = matcher.group("oznaka");
        String polaznaStan = matcher.group("polazna");
        String odredisnaStan = matcher.group("odredisna");
        String datumStr = matcher.group("datum");
        String nacinKupnje = matcher.group("nacin");

        LocalDate datumPutovanja;
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            datumPutovanja = LocalDate.parse(datumStr, df);
        } catch (Exception e) {
            System.out.println("Neispravan format datuma (očekivan: dd.MM.yyyy.).");
            return;
        }

        if (datumPutovanja.isBefore(LocalDate.now())) {
            System.out.println("Nije moguće kupiti kartu za datum koji je već prošao.");
            return;
        }

        String danUTjednu = mapirajDayOfWeek(datumPutovanja.getDayOfWeek().getValue());

        String vrijemeOd = "00:00";
        String vrijemeDo = "23:59";

        VozniRedKompozit vrKompozit = zeljeznickiPromet.getVozniRedKompozit();
        if (vrKompozit == null) {
            System.out.println("Nema voznog reda u sustavu.");
            return;
        }

        VozniRedKomponenta komp = vrKompozit.dohvatiJedinstvenuDjecu().get(oznakaVlaka);
        if (komp == null) {
            System.out.println("Vlak s oznakom " + oznakaVlaka + " ne postoji u voznom redu.");
            return;
        }
        if (!(komp instanceof VlakKompozit vlakKompozit)) {
            System.out.println("Nije VlakKompozit: " + oznakaVlaka);
            return;
        }

        PregledVlakovaFilterVisitor visitor = new PregledVlakovaFilterVisitor(
                polaznaStan, odredisnaStan, danUTjednu, vrijemeOd, vrijemeDo, "", oznakaVlaka
        );
        vrKompozit.prihvati(visitor);

        List<Stanica> ruta = visitor.pregledRute;
        if (ruta.isEmpty() || ruta.size() < 2) {
            System.out.printf("Vlak %s na dan %s nema rutu %s->%s%n",
                    oznakaVlaka, danUTjednu, polaznaStan, odredisnaStan);
            return;
        }

        List<List<Stanica>> grupiraneStanice = new ArrayList<>();

        List<Stanica> trenutnaGrupa = new ArrayList<>();
        Stanica prethodna = null;

        for (Stanica stanica : ruta) {
            if (prethodna == null) {
                trenutnaGrupa.add(stanica);
            } else {
                if (stanica.getOznakaPruge().equals(prethodna.getOznakaPruge())) {
                    trenutnaGrupa.add(stanica);
                } else {
                    grupiraneStanice.add(new ArrayList<>(trenutnaGrupa));
                    trenutnaGrupa.clear();
                    trenutnaGrupa.add(stanica);
                }
            }
            prethodna = stanica;
        }

        grupiraneStanice.add(new ArrayList<>(trenutnaGrupa));

        var segmenti = kreirajSegmenteSaPostojecimStatusima(grupiraneStanice);
        if (!rutaJeValidna(segmenti)) {
            System.out.println("Ruta nije validna jer neke relacije nisu ispravne. Kupovina karte nije moguća.");
            return;
        }

        System.out.printf("Kupovina karte (KKPV2S): vlak %s, relacija %s - %s, datum %s, način=%s%n",
                oznakaVlaka, polaznaStan, odredisnaStan, datumStr, nacinKupnje);

        Stanica prvaStanica = ruta.getFirst();
        Stanica zadnjaStanica = ruta.getLast();

        double ukupniKilometri = zadnjaStanica.getUdaljenostOdPocetne();

        String vrstaVlaka = vlakKompozit.getVrstaVlaka();

        double izvornaCijena = izracunajOsnovnuCijenu(ukupniKilometri, vrstaVlaka);
        double konacnaCijena = izracunajKonacnuCijenu(ukupniKilometri, vrstaVlaka, datumPutovanja, nacinKupnje);

        double popust = izvornaCijena - konacnaCijena;

        var vrijemePolaska = prvaStanica.getVrijemeVrstaVlaka(vrstaVlaka);
        var vrijemeDolaska = zadnjaStanica.getVrijemeVrstaVlaka(vrstaVlaka);

        KupovinaKarteOriginator originator = new KupovinaKarteOriginator();
        KupovinaKarte kupnja = new KupovinaKarte();

        kupnja.setOznakaVlaka(oznakaVlaka);
        kupnja.setPolaznaStanica(polaznaStan);
        kupnja.setOdredisnaStanica(odredisnaStan);
        kupnja.setDatumPutovanja(datumPutovanja);
        kupnja.setVrijemePolaska(vrijemePolaska);
        kupnja.setVrijemeDolaska(vrijemeDolaska);
        kupnja.setIzvornaCijena(izvornaCijena);
        kupnja.setPopustiUkupno(popust);
        kupnja.setKonacnaCijena(konacnaCijena);
        kupnja.setNacinKupovine(nacinKupnje);
        kupnja.setDatumVrijemeKupovine(LocalDateTime.now());

        originator.setStanje(kupnja);
        KupovinaKarteMemento mem = originator.save();
        zeljeznickiPromet.getCaretakerKupnja().addMemento(mem);

        DateTimeFormatter formatterKupnja = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        DateTimeFormatter formatterPutovanje = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        String kupnjaFormatted = kupnja.getDatumVrijemeKupovine().format(formatterKupnja);
        String putovanjeFormatted = datumPutovanja.format(formatterPutovanje);

        List<String> red = List.of(
                oznakaVlaka,
                polaznaStan + "-" + odredisnaStan,
                putovanjeFormatted,
                (vrijemePolaska != null) ? vrijemePolaska.toString() : "",
                (vrijemeDolaska != null) ? vrijemeDolaska.toString() : "",
                String.format("%.2f €", izvornaCijena),
                String.format("%.2f €", popust),
                String.format("%.2f €", konacnaCijena),
                nacinKupnje,
                kupnjaFormatted
        );

        List<List<String>> tablica = new ArrayList<>();
        tablica.add(red);

        System.out.println("\nPodaci o kupljenoj karti:");
        TablicaPomagac.prikaziTablicu(ZAGLAVLJE_KARTE, tablica);
    }

    public List<SegmentPruge> kreirajSegmenteSaPostojecimStatusima(
            List<List<Stanica>> grupiraneStanice
    ) {
        List<SegmentPruge> segmenti = new ArrayList<>();

        for (List<Stanica> staniceGrupe : grupiraneStanice) {
            for (int i = 0; i < staniceGrupe.size() - 1; i++) {
                Stanica pocetna = staniceGrupe.get(i);
                Stanica zavrsna = staniceGrupe.get(i + 1);

                if (pocetna.getNaziv().equals(zavrsna.getNaziv())) continue;

                Pruga odgovarajucaPruga = ZeljeznickiPromet.dohvatiInstancu().getPrugaPoOznaci(pocetna.getOznakaPruge());

                if (odgovarajucaPruga != null) {
                    List<SegmentPruge> normalniSegmenti = odgovarajucaPruga.segmentiPrugeN;
                    for (SegmentPruge segment : normalniSegmenti) {
                        if (segment.segmentPoklapanje(segment, pocetna, zavrsna)) {
                            segmenti.add(segment);
                            break;
                        }
                    }

                    List<SegmentPruge> obrnutiSegmenti = odgovarajucaPruga.segmentiPrugeO;
                    for (SegmentPruge segment : obrnutiSegmenti) {
                        if (segment.segmentPoklapanje(segment, pocetna, zavrsna)) {
                            segmenti.add(segment);
                            break;
                        }
                    }
                }
            }
        }

        return segmenti;
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

    private boolean rutaJeValidna(List<SegmentPruge> segmenti) {
        for (SegmentPruge segment : segmenti) {
            if (!segment.getStatus().getStatus().equals(StatusPrugeStanice.ISPRAVNA)) {
                System.out.println("Segment nije ispravan: " +
                        segment.getPocetna().getNaziv() + " -> " +
                        segment.getZavrsna().getNaziv() + ", Status: " +
                        segment.getStatus().getStatus().getOpis());
                return false;
            }
        }
        return true;
    }

    public void ponistiKupnju(int i) {
        var caretaker = zeljeznickiPromet.getCaretakerKupnja();
        int total = caretaker.getBrojMementa();

        if (total == 0) {
            System.out.println("Nema kupnji za poništavanje (UNDO).");
            return;
        }

        int targetIndex = i - 1;

        if (targetIndex < 0 || targetIndex >= total) {
            System.out.printf("Ne postoji kupnja s rednim brojem %d (ukupno: %d).%n", i, total);
            return;
        }

        caretaker.removeMemento(targetIndex);
        int newTotal = caretaker.getBrojMementa();
        if (newTotal == 0) {
            System.out.println("Sve su kupnje uklonjene. Nema više stanja za vraćanje.");
            return;
        }

        int lastIndex = newTotal - 1;
        KupovinaKarteMemento oldState = caretaker.getMemento(lastIndex);

        KupovinaKarteOriginator originator = new KupovinaKarteOriginator();
        originator.restore(oldState);

        System.out.printf("Obrisana je %d. kupljena karta (indeks=%d). Preostalo %d karte.%n",
                i, targetIndex, newTotal);

        IKKPVUpravitelj ikkpvUpravitelj = new IKKPVUpravitelj(zeljeznickiPromet);
        ikkpvUpravitelj.obradi("IKKPV");
    }
}
