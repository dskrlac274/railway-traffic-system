package validator;

import pomagaci.CsvPomagac;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OznakaDanaValidator {
    private static final Pattern CIJELI_BROJ_PATTERN = Pattern.compile("^-?\\d+$");
    private static final Pattern DANI_VOZNJE_PATTERN = Pattern.compile("^(Po|U|Sr|Č|Pe|Su|N)+$");
    private static final String DAN_REGEX = "Po|U|Sr|Č|Pe|Su|N";

    private static final String GRESKA_OZNAKA_DANA = "Oznaka dana nije ispravna.";
    private static final String GRESKA_DANI_VOZNJE = "Dani vožnje nisu ispravni.";
    private static final String GRESKA_DUPLICIRANI_DANI = "Dani vožnje ne smiju sadržavati duplicirane dane.";

    public String validirajRedak(String[] polja) {
        String oznakaDana = CsvPomagac.dohvatiString(polja, 0);
        String daniVoznje = CsvPomagac.dohvatiString(polja, 1);

        if (daniVoznje.isEmpty()) {
            return null;
        }

        if (!CIJELI_BROJ_PATTERN.matcher(oznakaDana).matches()) {
            return GRESKA_OZNAKA_DANA;
        }

        if (!DANI_VOZNJE_PATTERN.matcher(daniVoznje).matches()) {
            return GRESKA_DANI_VOZNJE;
        }

        if (imaDupliciraneDane(daniVoznje)) {
            return GRESKA_DUPLICIRANI_DANI;
        }

        return null;
    }

    private boolean imaDupliciraneDane(String daniVoznje) {
        Set<String> daniSet = new HashSet<>();
        Pattern danPattern = Pattern.compile(DAN_REGEX);
        Matcher matcher = danPattern.matcher(daniVoznje);

        while (matcher.find()) {
            String dan = matcher.group();
            if (!daniSet.add(dan)) {
                return true;
            }
        }
        return false;
    }
}
