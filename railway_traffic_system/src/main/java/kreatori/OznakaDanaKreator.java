package kreatori;

import jezgra.ZeljeznickiPromet;
import modeli.oznaka_dana.OznakaDana;
import pomagaci.CsvPomagac;
import validator.OznakaDanaValidator;

public class OznakaDanaKreator extends UcitavacPodatakaKreator<OznakaDana> {
    private final OznakaDanaValidator oznakaDanaValidator = new OznakaDanaValidator();

    @Override
    protected String validirajVrijednostiRetka(String[] polja) {
        return oznakaDanaValidator.validirajRedak(polja);
    }

    @Override
    protected RezultatKreiranja<OznakaDana> kreirajModel(String[] polja) {
        return RezultatKreiranja.uspjeh(new OznakaDana(
                CsvPomagac.dohvatiInt(polja, 0),
                CsvPomagac.dohvatiString(polja, 1)
        ));
    }

    @Override
    protected void spremiModel(OznakaDana model) {
        ZeljeznickiPromet.dohvatiInstancu().getOznakeDana().add(model);
    }
}
