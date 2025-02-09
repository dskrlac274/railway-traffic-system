package upravljaci;

import jezgra.ZeljeznickiPromet;
import pomagaci.TablicaPomagac;

import java.util.List;
import java.util.regex.Pattern;

public class IPUpravitelj extends Upravitelj {
    private static final Pattern IP_PREDLOZAK = Pattern.compile("^IP$");
    public static final List<String> ZAGLAVLJE_PRUGE = List.of("Oznaka", "Početna stanica", "Završna stanica", "Duljina");
    private final ZeljeznickiPromet zeljeznickiPromet;

    public IPUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return IP_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        var redovi = zeljeznickiPromet.getPruge().stream()
                .map(pruga -> List.of(
                        pruga.getOznaka(),
                        pruga.getPocetnaStanica(),
                        pruga.getZavrsnaStanica(),
                        String.format("%.2f km", pruga.getDuljina())
                ))
                .toList();

        TablicaPomagac.prikaziTablicu(ZAGLAVLJE_PRUGE, redovi);
    }
}
