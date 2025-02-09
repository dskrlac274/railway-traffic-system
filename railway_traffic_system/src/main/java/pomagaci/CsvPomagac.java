package pomagaci;

import jezgra.OpisnaReprezentacija;

import java.util.Arrays;

public class CsvPomagac {
    private static final String EMPTY_STRING = "";

    public static String dohvatiString(String[] polja, int index) {
        if (index >= 0 && index < polja.length) {
            return polja[index];
        }
        return EMPTY_STRING;
    }

    public static int dohvatiInt(String[] polja, int index) {
        return Integer.parseInt(dohvatiString(polja, index));
    }

    public static Integer dohvatiInteger(String[] polja, int index) {
        String vrijednost = dohvatiString(polja, index);
        try {
            return Integer.parseInt(vrijednost);
        } catch (Exception e) {
            return null;
        }
    }

    public static double dohvatiDouble(String[] polja, int index) {
        return Double.parseDouble(dohvatiString(polja, index).replace(",", "."));
    }

    public static <E extends OpisnaReprezentacija> E dohvatiEnum(Class<E> enumKl, String[] polja, int index) {
        String opis = dohvatiString(polja, index);
        return Arrays.stream(enumKl.getEnumConstants())
                .filter(enumValue -> enumValue.getClass() == enumKl && enumValue.getOpis().equals(opis))
                .findFirst()
                .orElse(null);
    }
}
