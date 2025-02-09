package visitor;

import composite.EtapaList;
import composite.VlakKompozit;
import composite.VozniRedKomponenta;
import composite.VozniRedKompozit;
import jezgra.ZeljeznickiPromet;
import modeli.stanica.Stanica;
import modeli.vozni_red.SmjerVozniRed;
import modeli.vozni_red.VrstaVlakaVozniRed;
import pomagaci.VrijemePomagac;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class PregledVlakovaFilterVisitor extends VozniRedVisitor {

    private final String polaznaStanica;
    private final String odredisnaStanica;
    private final String dan;
    private final LocalTime vrijemeOd;
    private final LocalTime vrijemeDo;
    private final String prikaz;
    private String oznakaVlaka;

    private final Map<String, StanicaRedak> staniceRedak = new LinkedHashMap<>();

    private final Set<String> relevantVlakovi = new LinkedHashSet<>();

    private static class StanicaRedak {
        String pruga;
        double km;
        Map<String, LocalTime> vlakVremena = new HashMap<>();
    }

    private List<String> zaglavlje = new ArrayList<>();

    public List<Stanica> pregledRute = new ArrayList<>();

    public PregledVlakovaFilterVisitor(String polaznaStanica, String odredisnaStanica, String dan, String odVr,
                                       String doVr, String prikaz) {
        this.polaznaStanica = polaznaStanica;
        this.odredisnaStanica = odredisnaStanica;
        this.dan = dan;
        this.vrijemeOd = VrijemePomagac.getVrijemeKaoTime(odVr);
        this.vrijemeDo = VrijemePomagac.getVrijemeKaoTime(doVr);
        this.prikaz = prikaz;
    }

    public PregledVlakovaFilterVisitor(String polaznaStanica, String odredisnaStanica, String dan, String odVr,
                                       String doVr, String prikaz, String oznakaVlaka) {
        this.polaznaStanica = polaznaStanica;
        this.odredisnaStanica = odredisnaStanica;
        this.dan = dan;
        this.vrijemeOd = VrijemePomagac.getVrijemeKaoTime(odVr);
        this.vrijemeDo = VrijemePomagac.getVrijemeKaoTime(doVr);
        this.prikaz = prikaz;
        this.oznakaVlaka = oznakaVlaka;
    }

    @Override
    public void posjeti(VozniRedKompozit voznired) {
        voznired.dohvatiJedinstvenuDjecu().values().forEach(v -> v.prihvati(this));

        this.zaglavlje = generirajZaglavlje();

        for (Map.Entry<String, StanicaRedak> entry : staniceRedak.entrySet()) {
            String nazivStanice = entry.getKey().split(" \\(")[0];
            StanicaRedak rowData = entry.getValue();

            List<String> red = new ArrayList<>();
            for (char c : prikaz.toCharArray()) {
                switch (c) {
                    case 'S' -> red.add(nazivStanice);
                    case 'P' -> red.add(rowData.pruga != null ? rowData.pruga : "");
                    case 'K' -> red.add(String.valueOf(rowData.km));
                    case 'V' -> {
                        for (String oznakaVlaka : relevantVlakovi) {
                            LocalTime t = rowData.vlakVremena.get(oznakaVlaka);
                            red.add(t != null ? t.toString() : "");
                        }
                    }
                }
            }
            this.getTablica().add(red);
        }
    }

    @Override
    public void posjeti(VlakKompozit vlak) {
        List<VozniRedKomponenta> etape = vlak.dohvatiDjecu();

        Map<Stanica, EtapaList> sveStanice = etape.stream()
                .filter(EtapaList.class::isInstance)
                .map(EtapaList.class::cast)
                .flatMap(etapa -> etapa.getPruga().getStanice().stream()
                        .filter(Objects::nonNull)
                        .map(stanica -> Map.entry(stanica, etapa)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (etapa1, etapa2) -> etapa1,
                        LinkedHashMap::new
                ));

        List<Stanica> listaStanica = new ArrayList<>(sveStanice.keySet());

        if (listaStanica.isEmpty()) return;

        Map<Optional<EtapaList>, List<Stanica>> filtriraneStanicePoEtapama = listaStanica.stream()
                .collect(Collectors.groupingBy(
                        stanica -> sveStanice.values().stream()
                                .filter(etapa -> etapa.getOznakaPruge().equals(stanica.getOznakaPruge()))
                                .findFirst(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<EtapaList> sveEtape = filtriraneStanicePoEtapama.keySet().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        List<Stanica> stanice = spojiEtape(filtriraneStanicePoEtapama, new ArrayList<>(sveEtape));

        if (stanice.isEmpty() || stanice.size() < 2) return;

        if (!stanice.getFirst().getNaziv().equals(polaznaStanica) || !stanice.getLast().getNaziv().equals(odredisnaStanica)) {
            return;
        }

        LocalTime polazak = stanice.getFirst().getVrijemeVrstaVlaka(vlak.getVrstaVlaka());
        LocalTime dolazak = stanice.getLast().getVrijemeVrstaVlaka(vlak.getVrstaVlaka());

        if (polazak == null || dolazak == null) {
            return;
        }

        if (polazak.isBefore(vrijemeOd) || dolazak.isAfter(vrijemeDo)) {
            return;
        }

        if (vlak.getOznaka().equals(oznakaVlaka)) {
            this.pregledRute = stanice;
        }

        relevantVlakovi.add(vlak.getOznaka());

        for (Stanica stanica : stanice) {
            String nazivStanice = stanica.getNaziv();
            String pruga = stanica.getOznakaPruge();
            LocalTime vrijeme = stanica.getVrijemeVrstaVlaka(vlak.getVrstaVlaka());

            String kljuc = nazivStanice + " (" + pruga + ")";
            if (!staniceRedak.containsKey(kljuc)) {
                StanicaRedak newRow = new StanicaRedak();
                newRow.pruga = pruga;
                newRow.km = stanica.getUdaljenostOdPocetne();
                staniceRedak.put(kljuc, newRow);
            }

            staniceRedak.get(kljuc).vlakVremena.put(vlak.getOznaka(), vrijeme);
        }
    }

    List<Stanica> spojiEtape(Map<Optional<EtapaList>, List<Stanica>> sveStanice, List<EtapaList> etape) {
        List<Stanica> sveStaniceUkupno = new ArrayList<>();
        List<Stanica> staniceEtape;

        List<EtapaList> filtriraneEtape = etape.stream()
                .filter(etapa -> etapa.getDaniUTjednu().contains(dan))
                .toList();

        List<Stanica> ruteVlaka = dohvatiRutuVlaka(filtriraneEtape);

        LinkedHashMap<String, Stanica> mapaStanica = ruteVlaka
                .stream()
                .collect(Collectors.toMap(
                        Stanica::getNaziv,
                        st -> st,
                        (existing, replacement) -> replacement,
                        LinkedHashMap::new
                ));

        List<Stanica> sveStaniece = new ArrayList<>(mapaStanica.values());

        for (EtapaList etapa : filtriraneEtape) {
            List<Stanica> sveStaniceSmjer = new ArrayList<>();

            staniceEtape = sveStanice.entrySet().stream()
                    .filter(entry -> entry.getKey().orElse(null) == etapa)
                    .flatMap(entry -> entry.getValue().stream())
                    .map(Stanica::kloniraj)
                    .toList();

            if (staniceEtape.isEmpty() || staniceEtape.size() == 1) continue;

            List<Stanica> noveStanice = new ArrayList<>(ZeljeznickiPromet.dohvatiInstancu()
                    .dohvatiStaniceNaPrugiIzmedu(polaznaStanica, odredisnaStanica, etapa.getSmjer())
                    .stream().map(Stanica::kloniraj).toList());

            if (noveStanice.isEmpty() || noveStanice.size() < 2) continue;


            noveStanice = ZeljeznickiPromet.dohvatiInstancu().akumulirajUdaljenostiStanica(noveStanice, noveStanice.getLast().getUdaljenostOdPocetne(),
                    "N");

            Set<String> jedinstvenePruge = noveStanice.stream()
                    .map(Stanica::getOznakaPruge)
                    .collect(Collectors.toSet());

            boolean viseRazlicitihPruge = jedinstvenePruge.size() > 1;

            int indexA = -1;
            int indexB = -1;

            for (int i = 0; i < noveStanice.size() - 1; i++) {
                Stanica stanicaNovoA = noveStanice.get(i);
                Stanica stanicaNovoB = noveStanice.get(i + 1);

                for (int j = 0; j < sveStaniece.size(); j++) {
                    if (sveStaniece.get(j).getNaziv().equals(stanicaNovoA.getNaziv())) {
                        indexA = j;
                    }
                    if (sveStaniece.get(j).getNaziv().equals(stanicaNovoB.getNaziv())) {
                        indexB = j;
                    }
                }
                if (indexA != -1 && indexB != -1) break;
            }

            if (indexA > indexB) {
                continue;
            }

            var vremenaEtapa = ZeljeznickiPromet.dohvatiInstancu()
                    .akumulirajVremenaStanica(
                            staniceEtape,
                            etapa.getPruga().getUKupnoTrajanje(etapa.getVrstaVlaka()),
                            etapa.getVrijemePolaska(),
                            etapa.getVrstaVlaka(),
                            etapa.getSmjer()
                    );

            for (Stanica stanicaNovo : noveStanice) {
                for (Stanica stanicaStaro : vremenaEtapa) {
                    if (stanicaNovo.getNaziv().equals(stanicaStaro.getNaziv())) {
                        if (viseRazlicitihPruge || stanicaStaro.getOznakaPruge().equals(stanicaNovo.getOznakaPruge())) {
                            stanicaNovo.setVrijemeVrstaVlaka(stanicaStaro.getVrijemeVrstaVlaka(etapa.getVrstaVlaka()), etapa.getVrstaVlaka());
                            sveStaniceUkupno.removeIf(st -> st.getNaziv().equals(stanicaNovo.getNaziv()));
                            sveStaniceSmjer.add(stanicaNovo);
                        }
                    }
                }
            }

            if (sveStaniceSmjer.isEmpty() || sveStaniceSmjer.size() == 1) continue;

            sveStaniceUkupno.addAll(sveStaniceSmjer);
        }
        return sveStaniceUkupno;
    }

    private List<Stanica> dohvatiRutuVlaka(List<EtapaList> etape) {
        List<Stanica> rezultat = new ArrayList<>();
        for (EtapaList et : etape) {
            List<Stanica> stanice = new ArrayList<>(et.getPruga().getStanice());

            rezultat.addAll(stanice);
        }
        return rezultat;
    }

    private List<String> generirajZaglavlje() {
        List<String> zag = new ArrayList<>();
        for (char c : prikaz.toCharArray()) {
            switch (c) {
                case 'S' -> zag.add("Stanica");
                case 'P' -> zag.add("Pruga");
                case 'K' -> zag.add("Km");
                case 'V' -> {
                    for (String oznaka : relevantVlakovi) {
                        zag.add("Vlak " + oznaka);
                    }
                }
            }
        }
        return zag;
    }

    public List<String> getZaglavlje() {
        return zaglavlje;
    }

    @Override
    public void posjeti(EtapaList etapaList) {
    }
}
