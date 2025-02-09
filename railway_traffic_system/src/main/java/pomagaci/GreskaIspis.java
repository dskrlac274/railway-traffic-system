package pomagaci;

public class GreskaIspis {
    private static int ukupneGreske = 0;

    public static void ispisiGresku(int redniBroj, String linija, String opis) {
        ukupneGreske++;
        System.out.printf("Greška #%d (redak %d): %s - %s%n", ukupneGreske, redniBroj, linija, opis);
    }

    public static void ispisiOpcuGresku(String opis) {
        ukupneGreske++;
        System.out.printf("Greška #%d: %s%n", ukupneGreske, opis);
    }
}
