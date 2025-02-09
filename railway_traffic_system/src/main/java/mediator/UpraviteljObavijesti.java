package mediator;

import java.util.*;

public class UpraviteljObavijesti implements UpraviteljObavijestiMediator {
    public static final Map<KorisnikColleague, List<String>> povijestObavijesti = new HashMap<>();

    @Override
    public void dodajKorisnika(KorisnikColleague korisnik) {
        povijestObavijesti.putIfAbsent(korisnik, new ArrayList<>());
    }

    @Override
    public void zapamtiObavijest(String ime, String prezime, String obavijest) {
        for (KorisnikColleague korisnik : povijestObavijesti.keySet()) {
            if (korisnik.getIme().equals(ime) && korisnik.getPrezime().equals(prezime)) {
                povijestObavijesti.get(korisnik).add(obavijest);
                break;
            }
        }
    }

    @Override
    public void prikaziSveObavijesti(String ime, String prezime) {
        Optional<KorisnikColleague> korisnik = povijestObavijesti.keySet().stream()
                .filter(k -> k.getIme().equals(ime) && k.getPrezime().equals(prezime))
                .findFirst();

        if (korisnik.isEmpty()) {
            System.out.println("Korisnik " + ime + " " + prezime + " nije registriran.");
            return;
        }

        List<String> obavijesti = povijestObavijesti.get(korisnik.get());
        if (obavijesti == null || obavijesti.isEmpty()) {
            System.out.println("Korisnik " + ime + " " + prezime + " nema obavijesti.");
            return;
        }

        System.out.println("Povijest obavijesti za " + ime + " " + prezime + ":");
        for (String obavijest : obavijesti) {
            System.out.println(obavijest);
        }
    }
}
