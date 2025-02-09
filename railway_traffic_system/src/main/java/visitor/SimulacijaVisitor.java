package visitor;

import composite.EtapaList;
import composite.VlakKompozit;
import composite.VozniRedKomponenta;
import jezgra.ZeljeznickiPromet;
import modeli.stanica.Stanica;
import observer.SimulacijaObserver;
import observer.SimulacijaSubject;
import pomagaci.TablicaPomagac;
import pomagaci.VrijemePomagac;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulacijaVisitor extends VozniRedVisitor implements SimulacijaSubject {
    private final String oznakaVlaka;
    private int koeficijent;
    private AtomicBoolean simulacijaAktivna;
    public static final List<String> ZAGLAVLJE_SIMULACIJE = List.of("Oznaka pruge", "Stanica", "Vrijeme");
    public static final Map<String, List<SimulacijaObserver>> observeriZaVlak = new HashMap<>();
    public static final Map<String, Map<String, List<SimulacijaObserver>>> observeriZaVlakIStanicu = new HashMap<>();


    public SimulacijaVisitor(String oznakaVlaka, int koeficijent) {
        this.oznakaVlaka = oznakaVlaka;
        this.koeficijent = koeficijent;
        this.simulacijaAktivna = new AtomicBoolean(true);
    }

    public SimulacijaVisitor(String oznakaVlaka) {
        this.oznakaVlaka = oznakaVlaka;
    }

    @Override
    public void posjeti(VlakKompozit vlakKompozit) {
        if (vlakKompozit.getOznaka().equals(oznakaVlaka)) {
            for (VozniRedKomponenta etapa : vlakKompozit.dohvatiDjecu()) {
                etapa.prihvati(this);
            }
        }
    }

    @Override
    public void posjeti(EtapaList etapaList) {
        if (!simulacijaAktivna.get()) {
            return;
        }

        List<Stanica> stanice = ZeljeznickiPromet.dohvatiInstancu()
                .dohvatiStaniceNaPrugiIzmedu(etapaList.getPolaznaStanica(), etapaList.getOdredisnaStanica(), etapaList.getSmjer());

        var staniceVrijeme = ZeljeznickiPromet.dohvatiInstancu()
                .akumulirajVremenaStanica(
                        stanice, etapaList.getPruga().getUKupnoTrajanje(etapaList.getVrstaVlaka()),
                        etapaList.getVrijemePolaska(), etapaList.getVrstaVlaka(), etapaList.getSmjer());

        etapaList.getPruga().setStanice(staniceVrijeme);

        LocalTime virtualnoVrijeme = VrijemePomagac.getVrijemeKaoTime(etapaList.getVrijemePolaska());

        for (Stanica stanica : etapaList.getPruga().getStanice()) {
            LocalTime vrijemeDolaska = stanica.getVrijemeVrstaVlaka(etapaList.getVrstaVlaka());

            if (vrijemeDolaska == null || virtualnoVrijeme.isAfter(vrijemeDolaska)) {
                continue;
            }

            while (!virtualnoVrijeme.equals(vrijemeDolaska)) {
                if (!simulacijaAktivna.get()) {
                    return;
                }

                try {
                    Thread.sleep(1000 / koeficijent);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                virtualnoVrijeme = virtualnoVrijeme.plusMinutes(1);
            }

            List<String> red = new ArrayList<>();
            red.add(etapaList.getOznakaPruge());
            red.add(stanica.getNaziv());
            red.add(String.valueOf(virtualnoVrijeme));

            this.getTablica().add(red);

            String poruka = "Vlak " + oznakaVlaka + " je stigao na stanicu: " + stanica.getNaziv() +
                    " (pruga: " + etapaList.getOznakaPruge() + ")";

            obavijestiZaVlak(poruka);
            obavijestiZaVlakIStanicu(stanica.getNaziv(), poruka);

            TablicaPomagac.prikaziTablicu(ZAGLAVLJE_SIMULACIJE, this.getTablica());
        }

        System.out.println("Simulacija za etapu je zavrsila.");
    }

    public void prekiniSimulaciju() {
        simulacijaAktivna.set(false);
    }


    @Override
    public void obavijestiZaVlak(String poruka) {
        if (observeriZaVlak.containsKey(oznakaVlaka)) {
            for (SimulacijaObserver observer : observeriZaVlak.get(oznakaVlaka)) {
                observer.azurirajZaVlak(oznakaVlaka, poruka);
            }
        }
    }

    @Override
    public void obavijestiZaVlakIStanicu(String stanica, String poruka) {
        if (observeriZaVlakIStanicu.containsKey(oznakaVlaka)) {
            if (observeriZaVlakIStanicu.get(oznakaVlaka).containsKey(stanica)) {
                List<SimulacijaObserver> observeriStanice = observeriZaVlakIStanicu.get(oznakaVlaka).get(stanica);
                for (SimulacijaObserver observer : observeriStanice) {
                    observer.azurirajZaVlakIStanicu(oznakaVlaka, stanica, poruka);
                }
            }
        }
    }

    @Override
    public void dodajObserverZaVlak(SimulacijaObserver observer, String vlak) {
        observeriZaVlak.computeIfAbsent(vlak, k -> new ArrayList<>()).add(observer);
    }

    @Override
    public void dodajObserverZaVlakIStanicu(SimulacijaObserver observer, String vlak, String stanica) {
        observeriZaVlakIStanicu
                .computeIfAbsent(vlak, k -> new HashMap<>())
                .computeIfAbsent(stanica, k -> new ArrayList<>())
                .add(observer);
    }
}
