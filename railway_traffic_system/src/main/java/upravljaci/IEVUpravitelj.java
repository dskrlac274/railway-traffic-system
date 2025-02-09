package upravljaci;

import composite.VozniRedKompozit;
import jezgra.ZeljeznickiPromet;
import pomagaci.TablicaPomagac;
import visitor.PregledEtapaVlakaVisitor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IEVUpravitelj extends Upravitelj {
    private static final Pattern IEV_PREDLOZAK = Pattern.compile("^IEV (?<oznakaVlaka>.+)$");
    private static final List<String> ZAGLAVLJE_ETAPA = List.of(
            "Oznaka vlaka", "Oznaka pruge", "Polazna stanica", "Odredišna stanica",
            "Vrijeme polaska", "Vrijeme dolaska", "Ukupno km", "Dani u tjednu"
    );
    private final ZeljeznickiPromet zeljeznickiPromet;

    public IEVUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return IEV_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = IEV_PREDLOZAK.matcher(komanda);

        if (!matcher.matches()) return;

        String oznakaVlaka = matcher.group("oznakaVlaka");
        VozniRedKompozit vozniRedKompozit = zeljeznickiPromet.getVozniRedKompozit();

        if (vozniRedKompozit == null) {
            System.out.println("Ne postoje etape u sustavu.");
            return;
        }

        PregledEtapaVlakaVisitor visitor = new PregledEtapaVlakaVisitor(oznakaVlaka);
        vozniRedKompozit.prihvati(visitor);

        var tablica = visitor.getTablica();

        if (!tablica.isEmpty())
            TablicaPomagac.prikaziTablicu(ZAGLAVLJE_ETAPA, tablica);
        else {
            System.out.println("Ne postoje etape za navedeni vlak.");
        }
    }
}
