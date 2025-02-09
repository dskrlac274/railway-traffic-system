package upravljaci;

import jezgra.ZeljeznickiPromet;
import modeli.korisnik.Korisnik;
import observer.SimulacijaSubject;
import visitor.SimulacijaVisitor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DPKUpravitelj extends Upravitelj {
    private static final Pattern DPK_PREDLOZAK = Pattern.compile("^DPK (?<ime>.+?) (?<prezime>.+?) - (?<vlak>\\d+)( - (?<stanica>.+))?$");

    private final ZeljeznickiPromet zeljeznickiPromet;

    public DPKUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return DPK_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = DPK_PREDLOZAK.matcher(komanda);

        if (!matcher.matches()) {
            System.out.println("Neispravna komanda.");
            return;
        }

        String ime = matcher.group("ime");
        String prezime = matcher.group("prezime");
        String vlak = matcher.group("vlak");
        String stanica = matcher.group("stanica");

        Korisnik korisnik = zeljeznickiPromet.getKorisnici().stream()
                .filter(k -> k.getIme().equals(ime) && k.getPrezime().equals(prezime))
                .findFirst()
                .orElse(null);

        if (korisnik == null) {
            System.out.println("Korisnik " + ime + " " + prezime + " nije pronađen u registru");
            return;
        }

        boolean vlakPostoji = zeljeznickiPromet.getVozniRedKompozit()
                .dohvatiJedinstvenuDjecu()
                .containsKey(vlak);
        if (!vlakPostoji) {
            System.out.println("Vlak s oznakom " + vlak + " nije pronađen");
            return;
        }

        if (stanica != null) {
            boolean stanicaPostoji = zeljeznickiPromet.getStaniceFlat().values().stream()
                    .flatMap(List::stream)
                    .anyMatch(s -> s.getNaziv().equals(stanica));

            if (!stanicaPostoji) {
                System.out.println("Stanica s imenom " + stanica + " nije pronađena");
                return;
            }
        }

        SimulacijaSubject simulacijaSubject = new SimulacijaVisitor(vlak);

        if (stanica == null) {
            simulacijaSubject.dodajObserverZaVlak(korisnik, vlak);
            System.out.println("Dodavanje korisnika " + ime + " " + prezime + " za praćenje vlaka s oznakom " + vlak);
        } else {
            simulacijaSubject.dodajObserverZaVlakIStanicu(korisnik, vlak, stanica);
            System.out.println("Dodavanje korisnika " + ime + " " + prezime + " za praćenje vlaka s oznakom " + vlak
                    + " za željezničku stanicu " + stanica);
        }
    }
}
