package upravljaci;

import jezgra.ZeljeznickiPromet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PKUpravitelj extends Upravitelj {
    private static final Pattern PK_PREDLOZAK = Pattern.compile("^PK$");

    private final ZeljeznickiPromet zeljeznickiPromet;

    public PKUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return PK_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = PK_PREDLOZAK.matcher(komanda);

        if (!matcher.matches()) {
            System.out.println("Neispravna komanda.");
            return;
        }

        var korisnici = zeljeznickiPromet.getKorisnici();

        if (korisnici.isEmpty()) {
            System.out.println("Registar korisnika je prazan.");
        } else {
            System.out.println("Registar korisnika:");
            korisnici.forEach(k -> System.out.println("- " + k));
        }
    }
}
