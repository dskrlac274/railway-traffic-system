package validator;

import pomagaci.CsvPomagac;

import java.util.regex.Pattern;

public class VoziloValidator {
    private static final Pattern OZNAKA_PATTERN = Pattern.compile(".+$");
    private static final Pattern OPIS_PATTERN = Pattern.compile(".+$");
    private static final Pattern PROIZVODAC_PATTERN = Pattern.compile(".+$");
    private static final Pattern GODINA_PATTERN = Pattern.compile("^\\d{4}$");
    private static final Pattern STATUS_PATTERN = Pattern.compile("^[IKZ]$");
    private static final Pattern NAMJENA_PATTERN = Pattern.compile("^(PSVP|PSVPVK|PSBP)$");
    private static final Pattern VRSTA_PRIJEVOZA_PATTERN = Pattern.compile("^(N|P|TA|TK|TRS|TTS)$");
    private static final Pattern VRSTA_POGONA_PATTERN = Pattern.compile("^([DEN])$");
    private static final Pattern DECIMALNI_BROJ_PATTERN = Pattern.compile("^-?\\d+([.,]?\\d+)?$");
    private static final Pattern CIJELI_BROJ_PATTERN = Pattern.compile("^-?\\d+$");

    private static final String GRESKA_OZNAKA = "Oznaka vozila nije ispravna.";
    private static final String GRESKA_OPIS = "Opis vozila nije ispravan.";
    private static final String GRESKA_PROIZVODAC = "Proizvođač nije ispravan.";
    private static final String GRESKA_GODINA = "Godina proizvodnje nije ispravna. Očekuju se četiri znamenke.";
    private static final String GRESKA_STATUS = "Status vozila nije ispravan. Dozvoljene vrijednosti su 'I' " +
            "za ispravno, 'K' za kvar i 'Z' za zatvoreno.";
    private static final String GRESKA_NAMJENA = "Namjena vozila nije ispravna.";
    private static final String GRESKA_PRIJEVOZ = "Vrsta prijevoza nije ispravna.";
    private static final String GRESKA_POGON = "Vrsta pogona nije ispravna.";
    private static final String GRESKA_BROJ_POLJA = "Redak nema točan broj polja odvojenih točkama sa zarezom.";
    private static final String GRESKA_MAKS_SNAGA = "Maksimalna snaga mora biti između 0 i 10 MW ili -1 ako nije poznato.";
    private static final String GRESKA_MAKS_BRZINA = "Maksimalna brzina mora biti između 1 i 200 km/h.";
    private static final String GRESKA_BROJ_SJEDISTA = "Broj sjedećih mjesta nije ispravan.";
    private static final String GRESKA_BROJ_STAJALISTA = "Broj stajaćih mjesta nije ispravan.";
    private static final String GRESKA_BROJ_KREVETA = "Broj kreveta nije ispravan.";
    private static final String GRESKA_BROJ_BICIKALA = "Broj mjesta za bicikle nije ispravan.";
    private static final String GRESKA_BROJ_AUTOMOBILA = "Broj mjesta za automobile nije ispravan.";
    private static final String GRESKA_NOSIVOST = "Nosivost nije ispravna.";
    private static final String GRESKA_ZAPREMINA = "Zapremina nije ispravna.";
    private static final String GRESKA_POVRSINA = "Povrsina nije ispravna.";
    private static final int OCEKIVAN_BROJ_POLJA = 18;

    public String validirajRedak(String[] polja) {
        if (polja.length != OCEKIVAN_BROJ_POLJA) return GRESKA_BROJ_POLJA;
        if (!OZNAKA_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 0)).matches()) return GRESKA_OZNAKA;
        if (!OPIS_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 1)).matches()) return GRESKA_OPIS;
        if (!PROIZVODAC_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 2)).matches()) return GRESKA_PROIZVODAC;
        if (!GODINA_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 3)).matches()) return GRESKA_GODINA;
        if (!NAMJENA_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 4)).matches()) return GRESKA_NAMJENA;
        if (!VRSTA_PRIJEVOZA_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 5)).matches()) return GRESKA_PRIJEVOZ;
        if (!VRSTA_POGONA_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 6)).matches()) return GRESKA_POGON;
        if (!DECIMALNI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 7)).matches())
            return GRESKA_MAKS_BRZINA;
        if (!DECIMALNI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 8)).matches()) return GRESKA_MAKS_SNAGA;
        if (!CIJELI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 9)).matches()) return GRESKA_BROJ_SJEDISTA;
        if (!CIJELI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 10)).matches())
            return GRESKA_BROJ_STAJALISTA;
        if (!CIJELI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 11)).matches()) return GRESKA_BROJ_KREVETA;
        if (!CIJELI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 12)).matches())
            return GRESKA_BROJ_BICIKALA;
        if (!CIJELI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 13)).matches())
            return GRESKA_BROJ_AUTOMOBILA;
        if (!DECIMALNI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 14)).matches()) return GRESKA_NOSIVOST;
        if (!DECIMALNI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 15)).matches()) return GRESKA_ZAPREMINA;
        if (!DECIMALNI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 16)).matches()) return GRESKA_POVRSINA;
        if (!STATUS_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 17)).matches()) return GRESKA_STATUS;

        return validirajLogickeAtribute(polja);
    }

    private String validirajLogickeAtribute(String[] polja) {
        double maksimalnaBrzina = CsvPomagac.dohvatiDouble(polja, 7);
        double maksimalnaSnaga = CsvPomagac.dohvatiDouble(polja, 8);

        if (maksimalnaSnaga != -1 && (maksimalnaSnaga < 0 || maksimalnaSnaga > 10)) return GRESKA_MAKS_SNAGA;
        if (maksimalnaBrzina < 1 || maksimalnaBrzina > 200) return GRESKA_MAKS_BRZINA;

        return null;
    }
}
