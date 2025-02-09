package upravljaci;

import composite.VozniRedKompozit;
import jezgra.ZeljeznickiPromet;
import pomagaci.TablicaPomagac;
import visitor.PregledEtapaVlakaDaniVisitor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IEVDUpravitelj extends Upravitelj {
    private static final Pattern IEVD_PREDLOZAK = Pattern.compile("^IEVD (?<dani>.+)$");
    private static final List<String> ZAGLAVLJE_ETAPA = List.of(
            "Oznaka vlaka", "Oznaka pruge", "Polazna stanica", "Odredišna stanica",
            "Vrijeme polaska", "Vrijeme dolaska", "Dani u tjednu"
    );
    private final ZeljeznickiPromet zeljeznickiPromet;

    public IEVDUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return IEVD_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = IEVD_PREDLOZAK.matcher(komanda);

        if (!matcher.matches()) return;

        String dani = matcher.group("dani");
        VozniRedKompozit vozniRedKompozit = zeljeznickiPromet.getVozniRedKompozit();

        if (vozniRedKompozit == null) {
            System.out.println("Ne postoje etape u sustavu.");
            return;
        }

        PregledEtapaVlakaDaniVisitor pregledEtapaVlakaDaniVisitor = new PregledEtapaVlakaDaniVisitor(dani);
        vozniRedKompozit.prihvati(pregledEtapaVlakaDaniVisitor);

        var tablica = pregledEtapaVlakaDaniVisitor.getTablica();

        if (!tablica.isEmpty())
            TablicaPomagac.prikaziTablicu(ZAGLAVLJE_ETAPA, tablica);
        else {
            System.out.println("Ne postoje etape za navedeni vlak za odredene dane.");
        }
    }
}
