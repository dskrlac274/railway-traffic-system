package pomagaci;

import java.util.ArrayList;
import java.util.List;

public class TablicaPomagac {
    private final List<String> zaglavlja;
    private final List<List<String>> redovi;
    private final List<Integer> sirineStupaca;

    public TablicaPomagac(List<String> zaglavlja) {
        this.zaglavlja = new ArrayList<>(zaglavlja);
        this.redovi = new ArrayList<>();
        this.sirineStupaca = new ArrayList<>();

        for (String zaglavlje : zaglavlja) {
            sirineStupaca.add(zaglavlje.length());
        }
    }

    public static void prikaziTablicu(List<String> zaglavlja, List<List<String>> redovi) {
        TablicaPomagac tablica = new TablicaPomagac(zaglavlja);
        for (List<String> redak : redovi) {
            tablica.dodajRedak(redak);
        }
        System.out.println(tablica.generirajTablicu());
    }

    public void dodajRedak(List<String> redak) {
        redovi.add(new ArrayList<>(redak));

        for (int i = 0; i < redak.size(); i++) {
            String vrijednost = redak.get(i) == null ? "" : redak.get(i);
            if (i >= sirineStupaca.size()) {
                sirineStupaca.add(vrijednost.length());
            } else {
                sirineStupaca.set(i, Math.max(sirineStupaca.get(i), vrijednost.length()));
            }
        }
    }

    public String generirajTablicu() {
        StringBuilder sb = new StringBuilder();
        sb.append(generirajZaglavlje()).append("\n");

        for (List<String> redak : redovi) {
            sb.append(generirajRedak(redak)).append("\n");
        }

        return sb.toString();
    }

    private String generirajZaglavlje() {
        StringBuilder sb = new StringBuilder();
        sb.append("|");

        for (int i = 0; i < zaglavlja.size(); i++) {
            sb.append(" ").append(String.format("%-" + sirineStupaca.get(i) + "s",
                    zaglavlja.get(i))).append(" |");
        }

        sb.append("\n").append(generirajRazdjelnik());
        return sb.toString();
    }

    private String generirajRazdjelnik() {
        StringBuilder sb = new StringBuilder();
        sb.append("|");

        for (int sirina : sirineStupaca) {
            sb.append("-".repeat(sirina + 2)).append("|");
        }

        return sb.toString();
    }

    private String generirajRedak(List<String> redak) {
        StringBuilder sb = new StringBuilder();
        sb.append("|");

        for (int i = 0; i < redak.size(); i++) {
            sb.append(" ").append(String.format("%-" + sirineStupaca.get(i) + "s",
                    redak.get(i))).append(" |");
        }

        return sb.toString();
    }
}
