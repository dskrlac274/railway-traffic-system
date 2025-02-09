package upravljaci;

import composite.VozniRedKompozit;
import jezgra.ZeljeznickiPromet;
import pomagaci.TablicaPomagac;
import visitor.PregledVlakovaVisitor;

import java.util.List;
import java.util.regex.Pattern;

public class IVUpravitelj extends Upravitelj {
    private static final Pattern IV_PREDLOZAK = Pattern.compile("^IV$");
    public static final List<String> ZAGLAVLJE_VLAKOVA = List.of("Oznaka vlaka", "Polazna stanica",
            "Odredišna stanica", "Vrijeme polaska",
            "Vrijeme dolaska", "Ukupno km");
    private final ZeljeznickiPromet zeljeznickiPromet;

    public IVUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return IV_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        VozniRedKompozit vozniRedKompozit = zeljeznickiPromet.getVozniRedKompozit();

        if (vozniRedKompozit == null) {
            System.out.println("Ne postoje etape u sustavu.");
            return;
        }

        PregledVlakovaVisitor pregledVlakovaVisitor = new PregledVlakovaVisitor();
        vozniRedKompozit.prihvati(pregledVlakovaVisitor);

        var tablica = pregledVlakovaVisitor.getTablica();

        if (!tablica.isEmpty()) {
            TablicaPomagac.prikaziTablicu(ZAGLAVLJE_VLAKOVA, pregledVlakovaVisitor.getTablica());
        } else {
            System.out.println("Ne postoje etape.");
        }
    }
}
