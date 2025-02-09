package strategy;

import modeli.CijenaKonfiguracija;

import java.time.LocalDate;

public interface ICijenaStrategy {
    double izracunajCijenu(double brojKilometara, LocalDate datumPutovanja,
                           double osnovnaCijenaPoKm, CijenaKonfiguracija cfg);
}
