package strategy;

import modeli.CijenaKonfiguracija;

import java.time.LocalDate;

public class CjenikContext {
    private ICijenaStrategy strategija;

    public CjenikContext(ICijenaStrategy strategija) {
        this.strategija = strategija;
    }

    public void setCjenik(ICijenaStrategy strategija) {
        this.strategija = strategija;
    }

    public double izracunajCijenu(double brojKilometara, LocalDate datumPutovanja,
                             double osnovnaCijenaPoKm, CijenaKonfiguracija cfg) {
        return strategija.izracunajCijenu(brojKilometara, datumPutovanja, osnovnaCijenaPoKm, cfg);
    }
}
