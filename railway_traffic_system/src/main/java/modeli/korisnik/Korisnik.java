package modeli.korisnik;

import mediator.KorisnikColleague;
import mediator.UpraviteljObavijestiMediator;
import observer.SimulacijaObserver;

public class Korisnik implements SimulacijaObserver, KorisnikColleague {
    private final String ime;
    private final String prezime;
    private UpraviteljObavijestiMediator mediator;

    public Korisnik(String ime, String prezime, UpraviteljObavijestiMediator mediator) {
        this.ime = ime;
        this.prezime = prezime;
        this.mediator = mediator;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    @Override
    public String toString() {
        return ime + " " + prezime;
    }

    @Override
    public void azurirajZaVlak(String vlak, String poruka) {
        String obavijest = "Vlak " + vlak + ": " + poruka;
        System.out.println("Obavijest za korisnika " + ime + " " + prezime + ": " + poruka);
        mediator.zapamtiObavijest(ime, prezime, obavijest);
    }

    @Override
    public void azurirajZaVlakIStanicu(String vlak, String stanica, String poruka) {
        String obavijest = "Vlak " + vlak + " na stanici " + stanica + ": " + poruka;
        System.out.println("Obavijest za korisnika " + ime + " " + prezime + ": " + poruka);
        mediator.zapamtiObavijest(ime, prezime, obavijest);
    }
}
