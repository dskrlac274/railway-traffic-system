package jezgra;

import command.KarteReceiver;
import command.KupovinaKartiInvoker;
import composite.VozniRedKompozit;
import memento.KupovinaKarteCaretaker;
import modeli.kompozicija.Kompozicija;
import modeli.korisnik.Korisnik;
import modeli.oznaka_dana.OznakaDana;
import modeli.pruga.Pruga;
import modeli.stanica.Stanica;
import modeli.vozilo.Vozilo;
import pomagaci.VrijemePomagac;
import modeli.CijenaKonfiguracija;

import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ZeljeznickiPromet {
    private static ZeljeznickiPromet zeljeznickiPromet;

    private final Map<String, List<Stanica>> stanice;
    private final List<Pruga> pruge;
    private final List<OznakaDana> oznakeDana;
    private final Map<String, Vozilo> vozila;
    private final Map<String, List<Kompozicija>> kompozicije;
    private VozniRedKompozit vozniRedKompozit;
    private final List<Korisnik> korisnici;

    private CijenaKonfiguracija cijenaKonfiguracija;
    private final KupovinaKarteCaretaker caretakerKupnja = new KupovinaKarteCaretaker();

    private final KupovinaKartiInvoker ponistivacInvoker;
    private final KarteReceiver karteReceiver;

    private ZeljeznickiPromet() {
        stanice = new HashMap<>();
        pruge = new ArrayList<>();
        oznakeDana = new ArrayList<>();
        vozila = new HashMap<>();
        kompozicije = new HashMap<>();
        korisnici = new ArrayList<>();

        this.ponistivacInvoker = new KupovinaKartiInvoker();
        this.karteReceiver = new KarteReceiver(this);
    }

    public KupovinaKartiInvoker getPonistivacInvoker() {
        return ponistivacInvoker;
    }

    public KarteReceiver getKarteReceiver() {
        return karteReceiver;
    }

    public static ZeljeznickiPromet dohvatiInstancu() {
        if (zeljeznickiPromet == null) {
            zeljeznickiPromet = new ZeljeznickiPromet();
        }
        return zeljeznickiPromet;
    }

    public void dodajKorisnika(Korisnik korisnik) {
        korisnici.add(korisnik);
    }

    public List<Korisnik> getKorisnici() {
        return korisnici;
    }

    public Map<String, List<Stanica>> getStanice() {
        return stanice;
    }

    public Map<String, List<Stanica>> getStaniceFlat() {
        return stanice.values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Stanica::getOznakaPruge));
    }

    public List<Stanica> getStaniceOkrenuto(List<Stanica> stanice) {
        List<Stanica> kopija = new ArrayList<>(stanice);
        Collections.reverse(kopija);
        return kopija;
    }

    public List<Pruga> getPruge() {
        return pruge;
    }

    public Pruga getPrugaPoOznaci(String oznakaPruge) {
        return pruge.stream()
                .filter(p -> p.getOznaka().equals(oznakaPruge))
                .findFirst()
                .orElse(null);
    }

    public List<Stanica> dohvatiStaniceNaPrugiIzmedu(String polaznaStanica, String odredisnaStanica, String smjer) {
        if (smjer.equals("O")) {
            String temp = polaznaStanica;
            polaznaStanica = odredisnaStanica;
            odredisnaStanica = temp;
        }

        AlgoritamPutanjeImplementator implementacija = new GrafBFSImplementator(zeljeznickiPromet, smjer);
        NajkraciPutAbstractor najkraciPutAbstractor = new NajkraciPutAbstractor(implementacija);

        if (smjer.equals("O")) {
            return getStaniceOkrenuto(najkraciPutAbstractor.pronadjiNajkraciPut(polaznaStanica, odredisnaStanica));
        }

        return najkraciPutAbstractor.pronadjiNajkraciPut(polaznaStanica, odredisnaStanica);
    }

    public void postaviUdaljenostOdPocetneStanica(List<Stanica> stanice) {
        if (!stanice.isEmpty()) {
            double kumulativnaUdaljenost = 0.0;
            stanice.getFirst().setUdaljenostOdPocetne(0.0);

            for (int i = 1; i < stanice.size(); i++) {
                Stanica stanica = stanice.get(i);
                kumulativnaUdaljenost += stanica.getDuzina();
                stanica.setUdaljenostOdPocetne(kumulativnaUdaljenost);
            }
        }
    }

    public int izracunajUkupnoVrijemeStanica(List<Stanica> stanice, Function<Stanica, Integer> vrijemeGetter) {
        int ukupnoVrijeme = 0;

        for (Stanica stanica : stanice) {
            Integer vrijeme = vrijemeGetter.apply(stanica);
            if (vrijeme != null) {
                ukupnoVrijeme += vrijeme;
            }
        }
        return ukupnoVrijeme;
    }

    public List<Stanica> akumulirajUdaljenostiStanica(List<Stanica> stanice, double duljinaPruge, String smjer) {
        stanice = stanice.stream().map(Stanica::kloniraj).toList();

        if (smjer.equals("O")) {
            for (Stanica stanica : stanice) {
                double novaUdaljenost = duljinaPruge - stanica.getUdaljenostOdPocetne();
                stanica.setUdaljenostOdPocetne(novaUdaljenost);
            }
        } else {
            postaviUdaljenostOdPocetneStanica(stanice);
        }
        return stanice;

    }

    public List<Stanica> akumulirajVremenaStanica(
            List<Stanica> stanice,
            int ukupnoTrajanje,
            String vrijemePolaska,
            String vrstaVlaka,
            String smjer) {

        stanice = stanice.stream().map(Stanica::kloniraj).toList();

        LocalTime vrijemePolaskaLocalTime = VrijemePomagac.getVrijemeKaoTime(vrijemePolaska);

        if (smjer.equals("O")) {
            int preostaloVrijeme = ukupnoTrajanje;

            if (!stanice.isEmpty()) {
                Stanica pocetnaStanica = stanice.getLast();
                LocalTime vrijemeStanice = vrijemePolaskaLocalTime.plusMinutes(ukupnoTrajanje);
                pocetnaStanica.setVrijemeVrstaVlaka(vrijemeStanice, vrstaVlaka);
            }

            for (int i = stanice.size() - 2; i >= 0; i--) {
                Stanica stanica = stanice.get(i);
                Integer minuteDoStanice = stanica.getPojedinacinoVrijemeVrstaVlaka(vrstaVlaka);

                if (minuteDoStanice == null) {
                    stanica.setVrijemeVrstaVlaka(null, vrstaVlaka);
                } else {
                    preostaloVrijeme -= minuteDoStanice;
                    LocalTime vrijemeStanice = vrijemePolaskaLocalTime.plusMinutes(preostaloVrijeme);
                    stanica.setVrijemeVrstaVlaka(vrijemeStanice, vrstaVlaka);
                }
            }
        } else {
            LocalTime kumulativnoVrijeme = vrijemePolaskaLocalTime;

            if (!stanice.isEmpty()) {
                stanice.getFirst().setVrijemeVrstaVlaka(vrijemePolaskaLocalTime, vrstaVlaka);

                for (int i = 1; i < stanice.size(); i++) {
                    Integer minuteTrajanja = stanice.get(i).getPojedinacinoVrijemeVrstaVlaka(vrstaVlaka);
                    if (minuteTrajanja == null) {
                        stanice.get(i).setVrijemeVrstaVlaka(null, vrstaVlaka);
                    } else {
                        kumulativnoVrijeme = kumulativnoVrijeme.plusMinutes(minuteTrajanja);
                        stanice.get(i).setVrijemeVrstaVlaka(kumulativnoVrijeme, vrstaVlaka);
                    }
                }
            }
        }
        return stanice;
    }

    public Map<String, Vozilo> getVozila() {
        return vozila;
    }

    public Vozilo getVoziloPoOznaci(String oznakaPrijevoznogSredstva) {
        return vozila.values().stream()
                .filter(v -> v.getOznaka().equals(oznakaPrijevoznogSredstva))
                .findFirst()
                .orElse(null);
    }

    public Map<String, List<Kompozicija>> getKompozicije() {
        return kompozicije;
    }

    public List<OznakaDana> getOznakeDana() {
        return oznakeDana;
    }

    public VozniRedKompozit getVozniRedKompozit() {
        return vozniRedKompozit;
    }

    public void setVozniRedKompozit(VozniRedKompozit vozniRedKompozit) {
        this.vozniRedKompozit = vozniRedKompozit;
    }

    public CijenaKonfiguracija getCijenaKonfiguracija() {
        return cijenaKonfiguracija;
    }

    public void setCijenaKonfiguracija(CijenaKonfiguracija konfiguracija) {
        this.cijenaKonfiguracija = konfiguracija;
    }

    public KupovinaKarteCaretaker getCaretakerKupnja() {
        return caretakerKupnja;
    }
}
