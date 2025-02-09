package mediator;

public interface UpraviteljObavijestiMediator {
    void dodajKorisnika(KorisnikColleague korisnik);
    void zapamtiObavijest(String ime, String prezime, String obavijest);
    void prikaziSveObavijesti(String ime, String prezime);
}
