package strategy;

import modeli.CijenaKonfiguracija;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class BlagajnaCijenaStrategy implements ICijenaStrategy {
    @Override
    public double izracunajCijenu(double brojKilometara, LocalDate datumPutovanja, double osnovnaCijenaPoKm, CijenaKonfiguracija cfg) {
        double baza = brojKilometara * osnovnaCijenaPoKm;

        DayOfWeek dan = datumPutovanja.getDayOfWeek();
        if (dan == DayOfWeek.SATURDAY || dan == DayOfWeek.SUNDAY) {
            double postotak = cfg.getPopustVikend();
            baza -= (baza * postotak / 100.0);
        }
        return baza;
    }
}
