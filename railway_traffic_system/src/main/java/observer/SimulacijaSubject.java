package observer;

public interface SimulacijaSubject {
    void obavijestiZaVlak(String poruka);
    void obavijestiZaVlakIStanicu(String stanica, String poruka);
    void dodajObserverZaVlak(SimulacijaObserver observer, String vlak);
    void dodajObserverZaVlakIStanicu(SimulacijaObserver observer, String vlak, String stanica);
}
