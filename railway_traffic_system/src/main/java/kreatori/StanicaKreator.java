package kreatori;

import jezgra.ZeljeznickiPromet;
import modeli.stanica.Stanica;
import modeli.stanica.*;
import pomagaci.CsvPomagac;
import state.*;
import validator.StanicaValidator;

import java.util.ArrayList;

import static modeli.stanica.StatusPrugeStanice.*;

public class StanicaKreator extends UcitavacPodatakaKreator<Stanica> {
    private static int sljedeciId = 0;
    private final StanicaValidator stanicaValidator = new StanicaValidator();

    @Override
    protected String validirajVrijednostiRetka(String[] polja) {
        return stanicaValidator.validirajRedak(polja);
    }

    @Override
    protected RezultatKreiranja<Stanica> kreirajModel(String[] polja) {
        return RezultatKreiranja.uspjeh(new Stanica(
                sljedeciId++,
                CsvPomagac.dohvatiString(polja, 0),
                CsvPomagac.dohvatiString(polja, 1),
                CsvPomagac.dohvatiEnum(VrstaStanice.class, polja, 2),
                CsvPomagac.dohvatiEnum(StatusStanice.class, polja, 3),
                CsvPomagac.dohvatiString(polja, 4),
                CsvPomagac.dohvatiString(polja, 5),
                CsvPomagac.dohvatiEnum(KategorijaPrugeStanice.class, polja, 6),
                CsvPomagac.dohvatiInt(polja, 7),
                CsvPomagac.dohvatiEnum(VrstaPrugeStanice.class, polja, 8),
                CsvPomagac.dohvatiInt(polja, 9),
                CsvPomagac.dohvatiDouble(polja, 10),
                CsvPomagac.dohvatiDouble(polja, 11),
                CsvPomagac.dohvatiEnum(StatusPrugeStanice.class, polja, 12),
                CsvPomagac.dohvatiDouble(polja, 13),
                CsvPomagac.dohvatiInteger(polja, 14),
                CsvPomagac.dohvatiInteger(polja, 15),
                CsvPomagac.dohvatiInteger(polja, 16)
        ));
    }

    @Override
    protected void spremiModel(Stanica model) {
        ZeljeznickiPromet.dohvatiInstancu().getStanice()
                .computeIfAbsent(model.dohvatiIdentifikator(), k -> new ArrayList<>()).add(model);
    }
}
