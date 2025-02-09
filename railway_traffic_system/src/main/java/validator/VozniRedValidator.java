package validator;

import composite.EtapaList;
import composite.VlakKompozit;
import composite.VozniRedKomponenta;
import pomagaci.CsvPomagac;
import pomagaci.GreskaIspis;
import pomagaci.VrijemePomagac;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class VozniRedValidator {
    private static final Pattern OZNAKA_PRUGE_PATTERN = Pattern.compile(".+$");
    private static final Pattern SMJER_PATTERN = Pattern.compile("^[NO]$");
    private static final Pattern POLAZNA_STANICA_PATTERN = Pattern.compile(".+$");
    private static final Pattern ODREDISNA_STANICA_PATTERN = Pattern.compile(".+$");
    private static final Pattern VRIJEME_PATTERN = Pattern.compile("^([01]?\\d|2[0-3]):[0-5]\\d$");
    private static final Pattern OZNAKA_VLAKA_PATTERN = Pattern.compile(".+$");
    private static final Pattern CIJELI_BROJ_PATTERN = Pattern.compile("^-?\\d+$");
    private static final Pattern VRSTA_VLAKA_PATTERN = Pattern.compile("^[UB]$");

    private static final String GRESKA_POLAZNA_STANICA = "Naziv polazne stanice nije ispravan.";
    private static final String GRESKA_ODREDISNA_STANICA = "Naziv odredisne stanice nije ispravan.";
    private static final String GRESKA_BROJ_POLJA = "Redak nema točan broj polja odvojenih točkama sa zarezom.";
    private static final String GRESKA_OZNAKA_PRUGE = "Oznaka pruge nije ispravna.";
    private static final String GRESKA_VRSTA_VLAKA = "Vrsta vlaka nije ispravna.";
    private static final String GRESKA_SMJER = "Smjer nije ispravan (N ili O).";
    private static final String GRESKA_VRIJEME_POLASKA = "Vrijeme polaska nije ispravno (hh:mm).";
    private static final String GRESKA_TRAJANJE_VOZNJE = "Trajanje voznje nije ispravno (hh:mm).";
    private static final String GRESKA_OZNAKA_VLAKA = "Oznaka vlaka nije ispravna.";
    private static final String GRESKA_OZNAKA_DANA = "Oznaka dana nije ispravna.";
    private static final String GRESKA_PREKLAPANJE_VREMENA_ETAPA = "Kompletni vlak ce biti obrisan zbog preklapanja vremena dolaska etapa";
    private static final String GRESKA_ISTE_STANICE = "Početna i završna stanica etape ne smiju biti iste.";
    private static final String GRESKA_VRIJEME_POLASKA_NAKON_DOLASKA = "Vrijeme polaska ne smije biti nakon vremena dolaska.";
    private static final String GRESKA_NEPOKLAPANJE_STANICA = "Kraj prethodne etape mora se poklapati s početkom iduće.";
    private static final String GRESKA_VRIJEME_DOLASKA_PRIJE_POLASKA = "Vrijeme dolaska prethodne etape ne smije biti nakon vremena polaska iduće.";
    private static final String GRESKA_RAZLICITI_TIPOVI_VLAKOVA = "Vlak s istom oznakom ne smije imati različite tipove brzine.";
    private static final int OCEKIVAN_BROJ_POLJA = 4;
    private static final int MAKSIMALAN_BROJ_POLJA = 9;

    public String validirajRedak(String[] polja) {
        if (polja.length < OCEKIVAN_BROJ_POLJA || polja.length > MAKSIMALAN_BROJ_POLJA) return GRESKA_BROJ_POLJA;
        if (!OZNAKA_PRUGE_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 0)).matches()) return GRESKA_OZNAKA_PRUGE;
        if (!SMJER_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 1)).matches()) return GRESKA_SMJER;
        if (!OZNAKA_VLAKA_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 4)).matches()) return GRESKA_OZNAKA_VLAKA;
        if (!VRIJEME_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 6)).matches()) return GRESKA_VRIJEME_POLASKA;

        var polaznaStanica = CsvPomagac.dohvatiString(polja, 2);
        if (!polaznaStanica.isEmpty() && !POLAZNA_STANICA_PATTERN.matcher(polaznaStanica).matches())
            return GRESKA_POLAZNA_STANICA;

        var odredisnaStanica = CsvPomagac.dohvatiString(polja, 3);
        if (!odredisnaStanica.isEmpty() && !ODREDISNA_STANICA_PATTERN.matcher(odredisnaStanica).matches())
            return GRESKA_ODREDISNA_STANICA;

        var vrstaVlaka = CsvPomagac.dohvatiString(polja, 5);
        if (!vrstaVlaka.isEmpty() && !VRSTA_VLAKA_PATTERN.matcher(vrstaVlaka).matches()) return GRESKA_VRSTA_VLAKA;

        var trajanjeVoznje = CsvPomagac.dohvatiString(polja, 7);
        if (!trajanjeVoznje.isEmpty() && !VRIJEME_PATTERN.matcher(trajanjeVoznje).matches())
            return GRESKA_TRAJANJE_VOZNJE;

        var oznakaDana = CsvPomagac.dohvatiString(polja, 8);
        if (!oznakaDana.isEmpty() && !CIJELI_BROJ_PATTERN.matcher(oznakaDana).matches()) return GRESKA_OZNAKA_DANA;

        return null;
    }

    public boolean provjeriEtapu(EtapaList etapa, String oznakaVlaka) {
        if (etapa.getPolaznaStanica().equals(etapa.getOdredisnaStanica())) {
            GreskaIspis.ispisiOpcuGresku(
                    String.format("Vlak %s: %s (Polazna: %s, Odredišna: %s)", oznakaVlaka,
                            GRESKA_ISTE_STANICE, etapa.getPolaznaStanica(), etapa.getOdredisnaStanica())
            );
            return false;
        }

        return true;
    }

    public boolean provjeriSekvencuEtapa(List<VozniRedKomponenta> etape, String oznakaVlaka) {
        for (int i = 0; i < etape.size() - 1; i++) {
            EtapaList trenutnaEtapa = (EtapaList) etape.get(i);
            EtapaList sljedecaEtapa = (EtapaList) etape.get(i + 1);

            if (!trenutnaEtapa.getOdredisnaStanica().equals(sljedecaEtapa.getPolaznaStanica())) {
                GreskaIspis.ispisiOpcuGresku(
                        String.format("Vlak %s: %s (Kraj prethodne: %s, Početak iduće: %s)",
                                oznakaVlaka, GRESKA_NEPOKLAPANJE_STANICA, trenutnaEtapa.getOdredisnaStanica(),
                                sljedecaEtapa.getPolaznaStanica())
                );
                return false;
            }

            LocalTime dolazak = VrijemePomagac.getVrijemeKaoTime(trenutnaEtapa.getVrijemeDolaska());
            LocalTime polazak = VrijemePomagac.getVrijemeKaoTime(sljedecaEtapa.getVrijemePolaska());

            if (dolazak != null && polazak != null && dolazak.isAfter(polazak)) {
                GreskaIspis.ispisiOpcuGresku(
                        String.format("Vlak %s: %s (Vrijeme dolaska: %s, Vrijeme polaska: %s)",
                                oznakaVlaka, GRESKA_VRIJEME_DOLASKA_PRIJE_POLASKA, dolazak, polazak)
                );
                return false;
            }
        }
        return true;
    }

    public boolean provjeriTipoveVlakova(Map<String, VlakKompozit> vlakovi) {
        for (Map.Entry<String, VlakKompozit> ulaz : vlakovi.entrySet()) {
            List<String> tipovi = ulaz.getValue().dohvatiDjecu().stream()
                    .map(etapa -> ((EtapaList) etapa).getVrstaVlaka())
                    .distinct()
                    .toList();
            if (tipovi.size() > 1) {
                GreskaIspis.ispisiOpcuGresku(
                        String.format("Vlak %s: %s (Tipovi: %s)", ulaz.getKey(), GRESKA_RAZLICITI_TIPOVI_VLAKOVA,
                                String.join(", ", tipovi))
                );
                return false;
            }
        }
        return true;
    }
}
