package upravljaci;

import jezgra.ZeljeznickiPromet;
import pomagaci.TablicaPomagac;
import visitor.PregledVlakovaFilterVisitor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IVI2SUpravitelj extends Upravitelj {
    private static final Pattern IVI2S_PREDLOZAK = Pattern.compile(
            "^IVI2S (?<polaznaStanica>.+) - (?<odredisnaStanica>.+) - (?<dan>Po|U|Sr|Č|Pe|Su|N)\\s-\\s(?<odVr>(?:[01]\\d|2[0-3]):[0-5]\\d)\\s-\\s(?<doVr>(?:[01]\\d|2[0-3]):[0-5]\\d)\\s-\\s(?<prikaz>.+)"
    );

    private final ZeljeznickiPromet zeljeznickiPromet;

    public IVI2SUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return IVI2S_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = IVI2S_PREDLOZAK.matcher(komanda);

        if (!matcher.matches()) return;

        String polaznaStanica = matcher.group("polaznaStanica").trim();
        String odredisnaStanica = matcher.group("odredisnaStanica").trim();
        String dan = matcher.group("dan").trim();
        String odVr = matcher.group("odVr").trim();
        String doVr = matcher.group("doVr").trim();
        String prikaz = matcher.group("prikaz").trim();

        var vozniRedKompozit = zeljeznickiPromet.getVozniRedKompozit();
        if (vozniRedKompozit == null) {
            System.out.println("Ne postoji vozni red u sustavu.");
            return;
        }

        PregledVlakovaFilterVisitor visitor = new PregledVlakovaFilterVisitor(polaznaStanica,
                odredisnaStanica, dan, odVr, doVr, prikaz);

        vozniRedKompozit.prihvati(visitor);

        var tablica = visitor.getTablica();

        if (!tablica.isEmpty()) {
            var zaglavlje = visitor.getZaglavlje();
            TablicaPomagac.prikaziTablicu(zaglavlje, tablica);
        } else {
            System.out.println("Ne postoje etape za navedeni vlak.");
        }
    }

}
