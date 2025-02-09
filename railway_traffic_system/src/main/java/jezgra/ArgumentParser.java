package jezgra;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentParser {
    private String staniceDatoteka;
    private String vozilaDatoteka;
    private String kompozicijeDatoteka;
    private String vozniRedDatoteka;
    private String oznakeDanaDatoteka;

    private static final Pattern ARGUMENT_PREDLOZAK =
            Pattern.compile("(?<prefix>\\s|^)--(?<oznaka>zs|zps|zk|zvr|zod)\\s+(?<datoteka>\\S+)");
    private static final Set<String> DOZVOLJENE_ZASTAVICE = Set.of("zs", "zps", "zk", "zvr", "zod");

    public boolean parsiraj(String[] args) {
        Set<String> pronadjeneZastavice = new HashSet<>();
        String argumenti = String.join(" ", args);
        Matcher matcher = ARGUMENT_PREDLOZAK.matcher(argumenti);

        while (matcher.find()) {
            String oznaka = matcher.group("oznaka");
            String datoteka = matcher.group("datoteka");

            if (!DOZVOLJENE_ZASTAVICE.contains(oznaka)) return false;

            if (pronadjeneZastavice.contains(oznaka)) return false;

            pronadjeneZastavice.add(oznaka);

            switch (oznaka) {
                case "zs":
                    staniceDatoteka = datoteka;
                    break;
                case "zps":
                    vozilaDatoteka = datoteka;
                    break;
                case "zk":
                    kompozicijeDatoteka = datoteka;
                    break;
                case "zvr":
                    vozniRedDatoteka = datoteka;
                    break;
                case "zod":
                    oznakeDanaDatoteka = datoteka;
                    break;
            }
        }

        if (!pronadjeneZastavice.containsAll(DOZVOLJENE_ZASTAVICE)) return false;

        int ukupniArgumenti = args.length;
        int brojParova = pronadjeneZastavice.size() * 2;

        return ukupniArgumenti == brojParova;
    }

    public String getStaniceDatoteka() {
        return staniceDatoteka;
    }

    public String getVozilaDatoteka() {
        return vozilaDatoteka;
    }

    public String getKompozicijeDatoteka() {
        return kompozicijeDatoteka;
    }

    public String getVozniRedDatoteka() {
        return vozniRedDatoteka;
    }

    public String getOznakeDanaDatoteka() {
        return oznakeDanaDatoteka;
    }
}
