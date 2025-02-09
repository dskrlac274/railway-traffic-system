package modeli;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class KupovinaKarte {
    private String oznakaVlaka;
    private String polaznaStanica;
    private String odredisnaStanica;
    private LocalDate datumPutovanja;
    private LocalTime vrijemePolaska;
    private LocalTime vrijemeDolaska;
    private double izvornaCijena;
    private double popustiUkupno;
    private double konacnaCijena;
    private String nacinKupovine;
    private LocalDateTime datumVrijemeKupovine;

    public String getOznakaVlaka() {
        return oznakaVlaka;
    }

    public void setOznakaVlaka(String oznakaVlaka) {
        this.oznakaVlaka = oznakaVlaka;
    }

    public String getPolaznaStanica() {
        return polaznaStanica;
    }

    public void setPolaznaStanica(String polaznaStanica) {
        this.polaznaStanica = polaznaStanica;
    }

    public String getOdredisnaStanica() {
        return odredisnaStanica;
    }

    public void setOdredisnaStanica(String odredisnaStanica) {
        this.odredisnaStanica = odredisnaStanica;
    }

    public LocalDate getDatumPutovanja() {
        return datumPutovanja;
    }

    public void setDatumPutovanja(LocalDate datumPutovanja) {
        this.datumPutovanja = datumPutovanja;
    }

    public LocalTime getVrijemePolaska() {
        return vrijemePolaska;
    }

    public void setVrijemePolaska(LocalTime vrijemePolaska) {
        this.vrijemePolaska = vrijemePolaska;
    }

    public LocalTime getVrijemeDolaska() {
        return vrijemeDolaska;
    }

    public void setVrijemeDolaska(LocalTime vrijemeDolaska) {
        this.vrijemeDolaska = vrijemeDolaska;
    }

    public double getIzvornaCijena() {
        return izvornaCijena;
    }

    public void setIzvornaCijena(double izvornaCijena) {
        this.izvornaCijena = izvornaCijena;
    }

    public double getPopustiUkupno() {
        return popustiUkupno;
    }

    public void setPopustiUkupno(double popustiUkupno) {
        this.popustiUkupno = popustiUkupno;
    }

    public double getKonacnaCijena() {
        return konacnaCijena;
    }

    public void setKonacnaCijena(double konacnaCijena) {
        this.konacnaCijena = konacnaCijena;
    }

    public LocalDateTime getDatumVrijemeKupovine() {
        return datumVrijemeKupovine;
    }

    public void setDatumVrijemeKupovine(LocalDateTime datumVrijemeKupovine) {
        this.datumVrijemeKupovine = datumVrijemeKupovine;
    }

    public String getNacinKupovine() {
        return nacinKupovine;
    }

    public void setNacinKupovine(String nacinKupovine) {
        this.nacinKupovine = nacinKupovine;
    }
}
