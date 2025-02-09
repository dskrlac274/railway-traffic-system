package strategy;

import modeli.CijenaKonfiguracija;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class KupovinaUVlakuCijenaStrategy implements ICijenaStrategy {

    @Override
    public double izracunajCijenu(double brojKilometara, LocalDate datumPutovanja, double osnovnaCijenaPoKm, CijenaKonfiguracija cfg) {
        double baza = brojKilometara * osnovnaCijenaPoKm;

        DayOfWeek dan = datumPutovanja.getDayOfWeek();
        if (dan == DayOfWeek.SATURDAY || dan == DayOfWeek.SUNDAY) {
            double popVikend = cfg.getPopustVikend();
            baza -= (baza * popVikend / 100.0);
        }

        double uvecanje = cfg.getUvecanjeVlak();
        baza += (baza * uvecanje / 100.0);

        return baza;
    }
}
