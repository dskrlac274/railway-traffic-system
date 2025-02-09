package upravljaci;

import mediator.UpraviteljObavijestiMediator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class POUpravitelj extends Upravitelj{
    private static final Pattern PO_PREDLOZAK = Pattern.compile("^PO (?<ime>.+?) (?<prezime>.+)$");

    private final UpraviteljObavijestiMediator mediator;

    public POUpravitelj(UpraviteljObavijestiMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return PO_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = PO_PREDLOZAK.matcher(komanda);

        if (!matcher.matches()) {
            System.out.println("Neispravna komanda.");
            return;
        }

        String ime = matcher.group("ime");
        String prezime = matcher.group("prezime");

        mediator.prikaziSveObavijesti(ime, prezime);
    }
}
