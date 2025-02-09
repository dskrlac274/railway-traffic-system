package upravljaci;

import composite.VlakKompozit;
import jezgra.ZeljeznickiPromet;
import visitor.SimulacijaVisitor;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SVVUpravitelj extends Upravitelj{
    private static final Pattern SVV_PREDLOZAK = Pattern.compile("^SVV (?<vlak>.+) - (?<dan>Po|U|Sr|Č|Pe|Su|N) - (?<koeficijent>\\d+)$");
    private final ZeljeznickiPromet zeljeznickiPromet;

    public SVVUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return SVV_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = SVV_PREDLOZAK.matcher(komanda);

        if (!matcher.matches()) {
            System.out.println("Neispravna komanda.");
            return;
        }

        String vlakOznaka = matcher.group("vlak");
        int koeficijent = Integer.parseInt(matcher.group("koeficijent"));

        SimulacijaVisitor simulacijaVisitor = new SimulacijaVisitor(vlakOznaka, koeficijent);

        VlakKompozit vlak = (VlakKompozit) zeljeznickiPromet.getVozniRedKompozit()
                .dohvatiJedinstvenuDjecu().get(vlakOznaka);

        if (vlak == null) {
            System.out.println("Vlak s oznakom " + vlakOznaka + " nije pronađen");
            return;
        }

        Thread simulacijaThread = new Thread(() -> vlak.prihvati(simulacijaVisitor));
        simulacijaThread.start();

        Scanner scanner = new Scanner(System.in);
        while (simulacijaThread.isAlive()) {
            String input = scanner.nextLine().trim();
            if (input.equals("X")) {
                simulacijaVisitor.prekiniSimulaciju();
                break;
            }
        }

        try {
            simulacijaThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
