package upravljaci;

import composite.VozniRedKompozit;
import jezgra.ZeljeznickiPromet;
import pomagaci.TablicaPomagac;
import visitor.PregledVoznogRedaVlakaVisitor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IVRVUpravitelj extends Upravitelj {
    private static final Pattern IVRVU_PREDLOZAK = Pattern.compile("^IVRV (?<oznakaVlaka>.+)$");
    private static final List<String> ZAGLAVLJE_ETAPA = List.of(
            "Oznaka vlaka", "Oznaka pruge", "Zeljeznicka stanica", "Vrijeme polaska", "Broj km od polazne"
    );
    private final ZeljeznickiPromet zeljeznickiPromet;

    public IVRVUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return IVRVU_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = IVRVU_PREDLOZAK.matcher(komanda);

        if (!matcher.matches()) return;

        String oznakaVlaka = matcher.group("oznakaVlaka");
        VozniRedKompozit vozniRedKompozit = zeljeznickiPromet.getVozniRedKompozit();

        if (vozniRedKompozit == null) {
            System.out.println("Ne postoje etape u sustavu.");
            return;
        }

        PregledVoznogRedaVlakaVisitor visitor = new PregledVoznogRedaVlakaVisitor(oznakaVlaka);
        vozniRedKompozit.prihvati(visitor);

        var tablica = visitor.getTablica();

        if (!tablica.isEmpty())
            TablicaPomagac.prikaziTablicu(ZAGLAVLJE_ETAPA, tablica);
        else {
            System.out.println("Ne postoje etape za navedeni vlak.");
        }
    }
}
