package upravljaci;

import command.KarteCommand;
import command.KarteReceiver;
import command.KopovinaKartiCommand;
import command.KupovinaKartiInvoker;
import jezgra.ZeljeznickiPromet;

import java.util.List;
import java.util.regex.Pattern;

public class KKPV2SUpravitelj extends Upravitelj {
    public static final Pattern KKPV2S_PREDLOZAK = Pattern.compile(
            "^KKPV2S\\s+(?<oznaka>\\S+)\\s+-\\s+(?<polazna>.+)\\s+-\\s+(?<odredisna>.+)\\s+-\\s+(?<datum>[^-]+)\\s+-\\s+(?<nacin>(WM|B|V))$"
    );

    public static final List<String> ZAGLAVLJE_KARTE = List.of(
            "Vlak",
            "Relacija",
            "Datum",
            "Vrijeme polaska",
            "Vrijeme dolaska",
            "Izvorna cijena",
            "Popust (€)",
            "Konačna cijena",
            "Način kupnje",
            "Vrijeme kupnje karte"
    );

    private final ZeljeznickiPromet zeljeznickiPromet;

    public KKPV2SUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return KKPV2S_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    public void izvrsi(String komanda) {
        KupovinaKartiInvoker invoker = zeljeznickiPromet.getPonistivacInvoker();
        KarteReceiver receiver = zeljeznickiPromet.getKarteReceiver();
        KarteCommand cmd = new KopovinaKartiCommand(receiver, komanda);

        invoker.izvrsiKomandu(cmd);
    }
}
