package upravljaci;

import jezgra.AlgoritamPutanjeAbstractor;
import jezgra.AlgoritamPutanjeImplementator;
import jezgra.GrafBFSImplementator;
import jezgra.ZeljeznickiPromet;
import modeli.stanica.Stanica;
import pomagaci.TablicaPomagac;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ISI2SUpravitelj extends Upravitelj {
    private static final Pattern ISI2S_PREDLOZAK = Pattern.compile("^ISI2S (?<polaznaStanica>.+) - (?<odredisnaStanica>.+)$");
    public static final List<String> ZAGLAVLJE_STANICA_IZMEDU = List.of("Naziv stanice", "Vrsta", "Km od polazne stanice");
    private final ZeljeznickiPromet zeljeznickiPromet;

    public ISI2SUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return ISI2S_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = ISI2S_PREDLOZAK.matcher(komanda);
        if (matcher.matches()) {
            String polaznaStanica = matcher.group("polaznaStanica");
            String odredisnaStanica = matcher.group("odredisnaStanica");

            AlgoritamPutanjeImplementator implementacija = new GrafBFSImplementator(zeljeznickiPromet);
            AlgoritamPutanjeAbstractor putanjaAbstraction = new AlgoritamPutanjeAbstractor(implementacija);

            var svePutanje = putanjaAbstraction.pronadjiPutanje(polaznaStanica, odredisnaStanica);

            if (svePutanje.isEmpty()) {
                System.out.println("Nema pronađenih putanja između " + polaznaStanica + " i " + odredisnaStanica + ".");
            }

            for (List<Stanica> putanja : svePutanje) {
                var redovi = putanja.stream()
                        .map(stanica -> List.of(
                                stanica.getNaziv(),
                                stanica.getVrstaStanice().getOpis(),
                                String.format("%.2f km", stanica.getUdaljenostOdPocetne())
                        ))
                        .toList();

                TablicaPomagac.prikaziTablicu(ZAGLAVLJE_STANICA_IZMEDU, redovi);
            }
        }
    }
}
