package upravljaci;

import jezgra.ZeljeznickiPromet;
import mediator.UpraviteljObavijesti;
import modeli.korisnik.Korisnik;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DKUpravitelj extends Upravitelj {
    private static final Pattern DK_PREDLOZAK = Pattern.compile("^DK (?<ime>.+?) (?<prezime>.+)$");

    private final ZeljeznickiPromet zeljeznickiPromet;

    public DKUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return DK_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = DK_PREDLOZAK.matcher(komanda);

        if (!matcher.matches()) {
            System.out.println("Neispravna komanda.");
            return;
        }

        String ime = matcher.group("ime");
        String prezime = matcher.group("prezime");

        if (ime.contains("-") || prezime.contains("-")) {
            System.out.println("Neispravna komanda.");
            return;
        }

        var upraviteljObavijesti = new UpraviteljObavijesti();
        Korisnik noviKorisnik = new Korisnik(ime, prezime, upraviteljObavijesti);
        zeljeznickiPromet.dodajKorisnika(noviKorisnik);
        upraviteljObavijesti.dodajKorisnika(noviKorisnik);

        System.out.println("Dodaje se korisnik: " + ime + " " + prezime);
    }
}
