package upravljaci;

import jezgra.ZeljeznickiPromet;
import modeli.CijenaKonfiguracija;
import pomagaci.CsvPomagac;
import pomagaci.TablicaPomagac;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CVPUpravitelj extends Upravitelj {
    private static final Pattern CVP_PREDLOZAK = Pattern.compile(
            "^CVP\\s+" +
                    "(?<norm>0(?:,\\d+)?|[1-9]\\d{0,2}(?:,\\d+)?)\\s+" +
                    "(?<ubr>0(?:,\\d+)?|[1-9]\\d{0,2}(?:,\\d+)?)\\s+" +
                    "(?<brz>0(?:,\\d+)?|[1-9]\\d{0,2}(?:,\\d+)?)\\s+" +
                    "(?<vikend>100(?:,0+)?|(?:0|[1-9]\\d?)(?:,\\d+)?)\\s+" +
                    "(?<web>100(?:,0+)?|(?:0|[1-9]\\d?)(?:,\\d+)?)\\s+" +
                    "(?<vlak>0(?:,\\d+)?|[1-9]\\d*(?:,\\d+)?)$"
    );

    private final ZeljeznickiPromet zeljeznickiPromet;

    public CVPUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return CVP_PREDLOZAK.matcher(komanda).matches();
    }


    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = CVP_PREDLOZAK.matcher(komanda);
        if (!matcher.matches()) {
            System.out.println("Neispravna komanda.");
            return;
        }

        String[] dijelovi = komanda.split("\\s+");

        double cijenaNormalni = CsvPomagac.dohvatiDouble(dijelovi, 1);
        double cijenaUbrzani = CsvPomagac.dohvatiDouble(dijelovi, 2);
        double cijenaBrzi = CsvPomagac.dohvatiDouble(dijelovi, 3);
        double popustVikend = CsvPomagac.dohvatiDouble(dijelovi, 4);
        double popustWeb = CsvPomagac.dohvatiDouble(dijelovi, 5);
        double uvecanjeVlak = CsvPomagac.dohvatiDouble(dijelovi, 6);

        CijenaKonfiguracija cfg = new CijenaKonfiguracija(
                cijenaNormalni,
                cijenaUbrzani,
                cijenaBrzi,
                popustVikend,
                popustWeb,
                uvecanjeVlak
        );
        zeljeznickiPromet.setCijenaKonfiguracija(cfg);

        String normStr = pretvoriSaZarezomBroj(cijenaNormalni);
        String ubrStr = pretvoriSaZarezomBroj(cijenaUbrzani);
        String brzStr = pretvoriSaZarezomBroj(cijenaBrzi);
        String vikStr = pretvoriSaZarezomPostotak(popustVikend);
        String webStr = pretvoriSaZarezomPostotak(popustWeb);
        String vlkStr = pretvoriSaZarezomPostotak(uvecanjeVlak);

        List<String> zaglavlja = List.of("Opis", "Vrijednost");

        List<List<String>> redovi = new ArrayList<>();
        redovi.add(List.of("Cijena normalni vlak", normStr + " €/km"));
        redovi.add(List.of("Cijena ubrzani vlak", ubrStr + " €/km"));
        redovi.add(List.of("Cijena brzi vlak", brzStr + " €/km"));
        redovi.add(List.of("Popust vikend (sub/ned)", vikStr + " %"));
        redovi.add(List.of("Popust web/mobilna aplikacija", webStr + " %"));
        redovi.add(List.of("Uvećanje kupovina u vlaku", vlkStr + " %"));

        TablicaPomagac.prikaziTablicu(zaglavlja, redovi);
    }

    private String pretvoriSaZarezomBroj(double vrijednost) {
        return String.format("%.2f", vrijednost).replace('.', ',');
    }

    private String pretvoriSaZarezomPostotak(double vrijednost) {
        return String.format("%.1f", vrijednost).replace('.', ',');
    }
}
