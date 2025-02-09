package kreatori;

import jezgra.ZeljeznickiPromet;
import modeli.kompozicija.Kompozicija;
import modeli.kompozicija.UlogaKompozicije;
import modeli.vozilo.Vozilo;
import pomagaci.CsvPomagac;
import validator.KompozicijaValidator;

import java.util.*;

public class KompozicijaKreator extends UcitavacPodatakaKreator<Kompozicija> {
    private final KompozicijaValidator kompozicijaValidator = new KompozicijaValidator();

    @Override
    protected String validirajVrijednostiRetka(String[] polja) {
        return kompozicijaValidator.validirajRedak(polja);
    }

    @Override
    protected RezultatKreiranja<Kompozicija> kreirajModel(String[] polja) {
        String oznakaKompozicije = CsvPomagac.dohvatiString(polja, 0);
        String oznakaPrijevoznogSredstva = CsvPomagac.dohvatiString(polja, 1);
        UlogaKompozicije uloga = CsvPomagac.dohvatiEnum(UlogaKompozicije.class, polja, 2);

        Vozilo vozilo = ZeljeznickiPromet.dohvatiInstancu().getVoziloPoOznaci(oznakaPrijevoznogSredstva);
        if (vozilo == null) {
            return null;
        }

        Kompozicija novaKompozicija = new Kompozicija(oznakaKompozicije, oznakaPrijevoznogSredstva, uloga);

        if (!dodajKompoziciju(novaKompozicija)) {
            return null;
        }

        return RezultatKreiranja.uspjeh(novaKompozicija);
    }

    @Override
    protected void spremiModel(Kompozicija model) {
        Map<String, List<Kompozicija>> kompozicijeMap = ZeljeznickiPromet.dohvatiInstancu().getKompozicije();
        kompozicijeMap.computeIfAbsent(model.dohvatiIdentifikator(), k -> new ArrayList<>()).add(model);
    }

    @Override
    protected String zavrsnaObrada() {
        Map<String, List<Kompozicija>> sveKompozicije = ZeljeznickiPromet.dohvatiInstancu().getKompozicije();

        var nevaljanUnos = sveKompozicije.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(),
                        new AbstractMap.SimpleEntry<>(entry.getValue(),
                                kompozicijaValidator.validirajKompoziciju(entry.getKey(), entry.getValue()))))
                .filter(entry -> entry.getValue().getValue() != null)
                .findFirst()
                .orElse(null);

        if (nevaljanUnos != null) {
            sveKompozicije.remove(nevaljanUnos.getKey());
            return nevaljanUnos.getValue().getValue();
        }

        return null;
    }

    private boolean dodajKompoziciju(Kompozicija novaKompozicija) {
        if (novaKompozicija == null) return false;

        Map<String, List<Kompozicija>> kompozicijeMap = ZeljeznickiPromet.dohvatiInstancu().getKompozicije();
        List<Kompozicija> listaKompozicija = kompozicijeMap.computeIfAbsent(novaKompozicija.dohvatiIdentifikator(),
                k -> new ArrayList<>());

        listaKompozicija.add(novaKompozicija);
        return true;
    }
}
