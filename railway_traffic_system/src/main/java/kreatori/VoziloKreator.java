package kreatori;

import jezgra.ZeljeznickiPromet;
import modeli.vozilo.*;
import pomagaci.CsvPomagac;
import validator.VoziloValidator;

public class VoziloKreator extends UcitavacPodatakaKreator<Vozilo> {
    private final VoziloValidator voziloValidator = new VoziloValidator();

    @Override
    protected String validirajVrijednostiRetka(String[] polja) {
        return voziloValidator.validirajRedak(polja);
    }

    @Override
    protected RezultatKreiranja<Vozilo> kreirajModel(String[] polja) {
        return RezultatKreiranja.uspjeh(new Vozilo(
                CsvPomagac.dohvatiString(polja, 0),
                CsvPomagac.dohvatiString(polja, 1),
                CsvPomagac.dohvatiString(polja, 2),
                CsvPomagac.dohvatiInt(polja, 3),
                CsvPomagac.dohvatiEnum(NamjenaVozila.class, polja, 4),
                CsvPomagac.dohvatiEnum(VrstaPrijevozaVozila.class, polja, 5),
                CsvPomagac.dohvatiEnum(VrstaPogonaVozila.class, polja, 6),
                CsvPomagac.dohvatiDouble(polja, 7),
                CsvPomagac.dohvatiDouble(polja, 8),
                CsvPomagac.dohvatiInt(polja, 9),
                CsvPomagac.dohvatiInt(polja, 10),
                CsvPomagac.dohvatiInt(polja, 11),
                CsvPomagac.dohvatiInt(polja, 12),
                CsvPomagac.dohvatiInt(polja, 13),
                CsvPomagac.dohvatiDouble(polja, 14),
                CsvPomagac.dohvatiDouble(polja, 15),
                CsvPomagac.dohvatiDouble(polja, 16),
                CsvPomagac.dohvatiEnum(StatusVozila.class, polja, 17)
        ));
    }

    @Override
    protected void spremiModel(Vozilo model) {
        ZeljeznickiPromet.dohvatiInstancu().getVozila().put(model.getOznaka(), model);
    }
}
