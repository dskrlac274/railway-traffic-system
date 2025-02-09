package validator;

import modeli.kompozicija.Kompozicija;
import modeli.kompozicija.UlogaKompozicije;

import java.util.List;
import java.util.regex.Pattern;

public class KompozicijaValidator {
    private static final Pattern OZNAKA_PATTERN = Pattern.compile(".+$");
    private static final Pattern OZNAKA_PRIJEVOZNOG_SREDSTVA_PATTERN = Pattern.compile(".+$");
    private static final Pattern ULOGA_PATTERN = Pattern.compile("^[PV]$");

    private static final String GRESKA_BROJ_POLJA = "Redak nema točan broj polja odvojenih točkama sa zarezom.";
    private static final String GRESKA_OZNAKA_KOMPOZICIJE = "Oznaka kompozicije nije ispravna.";
    private static final String GRESKA_OZNAKA_PRIJEVOZNOG_SREDSTVA = "Oznaka prijevoznog sredstva nije ispravna. " +
            "Očekivani su samo velika slova, brojevi i crtica.";
    private static final String GRESKA_ULOGA = "Uloga nije ispravna. Dozvoljene vrijednosti su 'P' za pogonsku " +
            "ili 'V' za vagonsku.";
    private static final String GRESKA_NEDOVOLJNO_ELEMENATA =
            "Kompozicija %s uklonjena je jer ne zadovoljava uvjete: mora sadržavati barem dva vozila (pogona ili vagona).";
    private static final String GRESKA_PRVA_ULOGA_NIJE_POGON =
            "Kompozicija %s uklonjena je jer ne počinje s vozilom s ulogom pogona.";

    public String validirajRedak(String[] polja) {
        if (polja.length != 3) return GRESKA_BROJ_POLJA;

        String oznakaKompozicije = polja[0];
        String oznakaPrijevoznogSredstva = polja[1];
        String uloga = polja[2];

        if (!OZNAKA_PATTERN.matcher(oznakaKompozicije).matches()) return GRESKA_OZNAKA_KOMPOZICIJE;

        if (!OZNAKA_PRIJEVOZNOG_SREDSTVA_PATTERN.matcher(oznakaPrijevoznogSredstva).matches())
            return GRESKA_OZNAKA_PRIJEVOZNOG_SREDSTVA;

        if (!ULOGA_PATTERN.matcher(uloga).matches()) return GRESKA_ULOGA;

        return null;
    }

    public String validirajKompoziciju(String oznakaKompozicije, List<Kompozicija> kompozicije) {
        if (!jePrvaUlogaPogon(kompozicije)) {
            return String.format(GRESKA_PRVA_ULOGA_NIJE_POGON, oznakaKompozicije);
        }

        if (!imaDvaVozila(kompozicije)) {
            return String.format(GRESKA_NEDOVOLJNO_ELEMENATA, oznakaKompozicije);
        }

        return null;
    }

    private boolean imaDvaVozila(List<Kompozicija> kompozicije) {
        return kompozicije.size() >= 2;
    }

    private boolean jePrvaUlogaPogon(List<Kompozicija> kompozicije) {
        return !kompozicije.isEmpty() && jePogonskaUloga(kompozicije.getFirst());
    }

    private boolean jePogonskaUloga(Kompozicija kompozicija) {
        return UlogaKompozicije.POGON.equals(kompozicija.getUloga());
    }
}
