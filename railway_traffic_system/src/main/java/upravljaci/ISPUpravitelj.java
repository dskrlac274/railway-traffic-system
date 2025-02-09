package upravljaci;

import jezgra.ZeljeznickiPromet;
import modeli.pruga.Pruga;
import modeli.stanica.Stanica;
import pomagaci.TablicaPomagac;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ISPUpravitelj extends Upravitelj {
    private static final Pattern ISP_PREDLOZAK = Pattern.compile("^ISP (?<oznakaPruge>.+) (?<redoslijed>[NO])$");
    public static final List<String> ZAGLAVLJE_STANICA = List.of("Naziv stanice", "Vrsta", "Km od početne stanice");
    private final ZeljeznickiPromet zeljeznickiPromet;

    public ISPUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return ISP_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = ISP_PREDLOZAK.matcher(komanda);
        if (matcher.matches()) {
            String oznakaPruge = matcher.group("oznakaPruge");
            String redoslijed = matcher.group("redoslijed");

            Pruga pruga = zeljeznickiPromet.getPrugaPoOznaci(oznakaPruge);

            if (pruga == null) {
                System.out.println("Pruga s oznakom " + oznakaPruge + " nije pronađena.");
                return;
            }

            List<Stanica> staniceNaPrugi = pruga.getStanice().stream().map(Stanica::kloniraj).toList();

            if ("O".equals(redoslijed)) {
                staniceNaPrugi = zeljeznickiPromet.getStaniceOkrenuto(staniceNaPrugi);

                double ukupnaDuljina = pruga.getDuljina();
                for (Stanica stanica : staniceNaPrugi) {
                    double novaUdaljenost = ukupnaDuljina - stanica.getUdaljenostOdPocetne();
                    stanica.setUdaljenostOdPocetne(novaUdaljenost);
                }
            } else {
                if (!staniceNaPrugi.isEmpty()) {
                    double kumulativnaUdaljenost = 0.0;
                    staniceNaPrugi.getFirst().setUdaljenostOdPocetne(0.0);

                    for (int i = 1; i < staniceNaPrugi.size(); i++) {
                        Stanica stanica = staniceNaPrugi.get(i);
                        kumulativnaUdaljenost += stanica.getDuzina();
                        stanica.setUdaljenostOdPocetne(kumulativnaUdaljenost);
                    }
                }
            }

            var redovi = staniceNaPrugi.stream()
                    .map(stanica -> List.of(
                            stanica.getNaziv(),
                            stanica.getVrstaStanice().getOpis(),
                            String.format("%.2f km", stanica.getUdaljenostOdPocetne())
                    ))
                    .toList();

            TablicaPomagac.prikaziTablicu(ZAGLAVLJE_STANICA, redovi);
        }
    }
}
