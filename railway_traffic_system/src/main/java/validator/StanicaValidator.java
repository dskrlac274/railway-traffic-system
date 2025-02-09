package validator;

import pomagaci.CsvPomagac;

import java.util.regex.Pattern;

public class StanicaValidator {
    private static final Pattern NAZIV_PATTERN = Pattern.compile(".+$");
    private static final Pattern OZNAKA_PRUGE_PATTERN = Pattern.compile(".+$");
    private static final Pattern STATUS_DA_NE_PATTERN = Pattern.compile("^(DA|NE)$");
    private static final Pattern STATUS_PRUGE_PATTERN = Pattern.compile("^[IKZ]$");
    private static final Pattern VRSTA_STANICE_PATTERN = Pattern.compile("^(kol\\.|staj\\.|)$");
    private static final Pattern KATEGORIJA_PRUGE_PATTERN = Pattern.compile("^[MLR]$");
    private static final Pattern VRSTA_PRUGE_PATTERN = Pattern.compile("^[EK]$");
    private static final Pattern STATUS_STANICE_PATTERN = Pattern.compile("^[OZ]$");
    private static final Pattern DECIMALNI_BROJ_PATTERN = Pattern.compile("^-?\\d+([.,]?\\d+)?$");
    private static final Pattern CIJELI_BROJ_PATTERN = Pattern.compile("^-?\\d+$");

    private static final String GRESKA_BROJ_POLJA = "Redak nema točan broj polja odvojenih točkama sa zarezom.";
    private static final String GRESKA_NAZIV = "Naziv stanice nije ispravan.";
    private static final String GRESKA_OZNAKA_PRUGE = "Oznaka pruge nije ispravna.";
    private static final String GRESKA_VRSTA_STANICE = "Vrsta stanice nije ispravna.";
    private static final String GRESKA_STATUS_STANICE = "Status stanice nije ispravan.";
    private static final String GRESKA_STATUS_DA_NE = "Vrijednost za putnike ul/iz ili robu ut/ist mora biti 'DA' ili 'NE'.";
    private static final String GRESKA_KATEGORIJA_PRUGE = "Kategorija pruge nije ispravna.";
    private static final String GRESKA_VRSTA_PRUGE = "Vrsta pruge nije ispravna.";
    private static final String GRESKA_DO_PO_OSOVINI = "Vrijednost DO po osovini nije ispravna (10-50 t/os).";
    private static final String GRESKA_DO_PO_DUZNOM_M = "Vrijednost DO po dužnom metru nije ispravna (2-10 t/m).";
    private static final String GRESKA_STATUS_PRUGE = "Status pruge nije ispravan.";
    private static final String GRESKA_DUZINA = "Dužina pruge nije ispravna (0-999 km).";
    private static final String GRESKA_KOLOSIJEK = "Broj kolosijeka mora biti 1 ili 2.";
    private static final String GRESKA_PERON = "Broj perona mora biti cijeli broj.";
    private static final String GRESKA_VRIJEME = "Vrijeme za vlak mora biti cjelobrojna vrijednost.";
    private static final int OCEKIVAN_BROJ_POLJA = 14;
    private static final int MAKSIMALAN_BROJ_POLJA = 17;

    public String validirajRedak(String[] polja) {
        if (polja.length < OCEKIVAN_BROJ_POLJA || polja.length > MAKSIMALAN_BROJ_POLJA) return GRESKA_BROJ_POLJA;
        if (!NAZIV_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 0)).matches()) return GRESKA_NAZIV;
        if (!OZNAKA_PRUGE_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 1)).matches()) return GRESKA_OZNAKA_PRUGE;
        if (!VRSTA_STANICE_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 2)).matches()) return GRESKA_VRSTA_STANICE;
        if (!STATUS_STANICE_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 3)).matches()) return GRESKA_STATUS_STANICE;
        if (!STATUS_DA_NE_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 4)).matches() ||
                !STATUS_DA_NE_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 5)).matches())
            return GRESKA_STATUS_DA_NE;
        if (!KATEGORIJA_PRUGE_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 6)).matches())
            return GRESKA_KATEGORIJA_PRUGE;
        if (!CIJELI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 7)).matches()) return GRESKA_PERON;
        if (!VRSTA_PRUGE_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 8)).matches()) return GRESKA_VRSTA_PRUGE;
        if (!CIJELI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 9)).matches()) return GRESKA_KOLOSIJEK;
        if (!DECIMALNI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 10)).matches()) return GRESKA_DO_PO_OSOVINI;
        if (!DECIMALNI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 11)).matches()) return GRESKA_DO_PO_DUZNOM_M;
        if (!STATUS_PRUGE_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 12)).matches()) return GRESKA_STATUS_PRUGE;
        if (!DECIMALNI_BROJ_PATTERN.matcher(CsvPomagac.dohvatiString(polja, 13)).matches()) return GRESKA_DUZINA;

        var vrijemeNormalni = CsvPomagac.dohvatiString(polja, 14);
        if (!vrijemeNormalni.isEmpty() && !CIJELI_BROJ_PATTERN.matcher(vrijemeNormalni).matches()) return GRESKA_VRIJEME;
        var vrijemeUbrzani = CsvPomagac.dohvatiString(polja, 15);
        if (!vrijemeUbrzani.isEmpty() && !CIJELI_BROJ_PATTERN.matcher(vrijemeUbrzani).matches()) return GRESKA_VRIJEME;
        var vrijemeBrzi = CsvPomagac.dohvatiString(polja, 16);
        if (!vrijemeBrzi.isEmpty() && !CIJELI_BROJ_PATTERN.matcher(vrijemeBrzi).matches()) return GRESKA_VRIJEME;

        return validirajLogickeAtribute(polja);
    }

    private String validirajLogickeAtribute(String[] polja) {
        int brojKolosijeka = CsvPomagac.dohvatiInt(polja, 9);
        if (brojKolosijeka < 1 || brojKolosijeka > 2) return GRESKA_KOLOSIJEK;

        double doPoOsovini = CsvPomagac.dohvatiDouble(polja, 10);
        if (doPoOsovini < 10 || doPoOsovini > 50) return GRESKA_DO_PO_OSOVINI;

        double doPoDuznomM = CsvPomagac.dohvatiDouble(polja, 11);
        if (doPoDuznomM < 2 || doPoDuznomM > 10) return GRESKA_DO_PO_DUZNOM_M;

        double duzina = CsvPomagac.dohvatiDouble(polja, 13);
        if (duzina < 0 || duzina > 999) return GRESKA_DUZINA;

        return null;
    }
}
