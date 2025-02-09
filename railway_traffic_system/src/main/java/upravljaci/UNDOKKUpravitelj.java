package upravljaci;

import command.KupovinaKartiInvoker;
import jezgra.ZeljeznickiPromet;

import java.util.regex.Pattern;

import java.util.regex.Matcher;

public class UNDOKKUpravitelj extends Upravitelj {
    private static final Pattern UNDO_PATTERN = Pattern.compile("^UNDOKK\\s+(\\d+)$");

    private final ZeljeznickiPromet zeljeznickiPromet;

    public UNDOKKUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return UNDO_PATTERN.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher m = UNDO_PATTERN.matcher(komanda);
        if (!m.matches()) {
            System.out.println("Nepoznata komanda.");
            return;
        }

        KupovinaKartiInvoker invoker = zeljeznickiPromet.getPonistivacInvoker();

        int i;
        try {
            i = Integer.parseInt(m.group(1));
        } catch (NumberFormatException e) {
            System.out.println("UNOS: Pogrešan broj za UNDO.");
            return;
        }

        invoker.ponistiKomanduNti(i);
    }
}

