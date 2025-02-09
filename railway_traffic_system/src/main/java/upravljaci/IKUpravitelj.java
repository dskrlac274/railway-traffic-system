package upravljaci;

import jezgra.ZeljeznickiPromet;
import modeli.kompozicija.Kompozicija;
import pomagaci.TablicaPomagac;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IKUpravitelj extends Upravitelj {
    private static final Pattern IK_PREDLOZAK = Pattern.compile("^IK (?<oznakaKompozicije>.+)$");
    public static final List<String> ZAGLAVLJE_KOMPOZICIJA = List.of("Oznaka", "Uloga", "Opis", "Godina", "Namjena",
            "Vrsta pogona", "Maks. brzina");
    private final ZeljeznickiPromet zeljeznickiPromet;

    public IKUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return IK_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = IK_PREDLOZAK.matcher(komanda);
        if (matcher.matches()) {
            String oznakaKompozicije = matcher.group("oznakaKompozicije");
            List<Kompozicija> kompozicije = zeljeznickiPromet.getKompozicije().get(oznakaKompozicije);
            if (kompozicije == null || kompozicije.isEmpty()) {
                System.out.println("Kompozicija s oznakom " + oznakaKompozicije + " nije pronađena.");
                return;
            }

            var redovi = kompozicije.stream()
                    .flatMap(kompozicija -> zeljeznickiPromet.getVozila().values().stream()
                            .filter(vozilo -> vozilo.getOznaka().equals(kompozicija.getOznakaPrijevoznogSredstva()))
                            .map(vozilo -> List.of(
                                    vozilo.getOznaka(),
                                    kompozicija.getUloga().getOpis(),
                                    vozilo.getOpis(),
                                    String.valueOf(vozilo.getGodina()),
                                    vozilo.getNamjena().getOpis(),
                                    vozilo.getVrstaPogona().getOpis(),
                                    String.format("%.2f km/h", vozilo.getMaksBrzina())
                            ))
                    )
                    .distinct()
                    .toList();

            TablicaPomagac.prikaziTablicu(ZAGLAVLJE_KOMPOZICIJA, redovi);
        }
    }
}
