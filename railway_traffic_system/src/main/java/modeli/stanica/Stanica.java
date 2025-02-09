package modeli.stanica;

import jezgra.Prototype;
import jezgra.Zeljeznica;
import state.*;

import java.time.LocalTime;

import static modeli.vozni_red.VrstaVlakaVozniRed.*;

public class Stanica implements Prototype<Stanica>, Zeljeznica {
    private final int id;
    private final String naziv;
    private final String oznakaPruge;
    private final StatusStanice statusStanice;
    private final VrstaStanice vrstaStanice;
    private final boolean putniciUlIz;
    private final boolean robaUtIst;
    private final KategorijaPrugeStanice kategorijaPruge;
    private final int brojPerona;
    private final VrstaPrugeStanice vrstaPruge;
    private final int brojKolosjeka;
    private final double doPoOsovini;
    private final double doPoDuznomM;
    private StatusPrugeStanice statusPruge;
    private double duzina;
    private Integer vrijemeNormalniVlak;
    private Integer vrijemeUbrzaniVlak;
    private Integer vrijemeBrziVlak;
    private double udaljenostOdPocetne = 0;
    private LocalTime vrijemeNormalniOdPocetne = LocalTime.ofSecondOfDay(0);
    private LocalTime vrijemeUbrzaniOdPocetne = LocalTime.ofSecondOfDay(0);
    private LocalTime vrijemeBrziOdPocetne = LocalTime.ofSecondOfDay(0);

    public Stanica(int id, String naziv, String oznakaPruge, VrstaStanice vrstaStanice,
                   StatusStanice statusStanice, String putniciUlIz, String robaUtIst,
                   KategorijaPrugeStanice kategorijaPruge, int brojPerona, VrstaPrugeStanice vrstaPruge,
                   int brojKolosjeka, double doPoOsovini, double doPoDuznomM, StatusPrugeStanice statusPruge,
                   double duzina, Integer vrijemeNormalniVlak, Integer vrijemeUbrzaniVlak, Integer vrijemeBrziVlak) {
        this.id = id;
        this.naziv = naziv;
        this.oznakaPruge = oznakaPruge;
        this.vrstaStanice = vrstaStanice;
        this.putniciUlIz = putniciUlIz.equals("DA");
        this.robaUtIst = robaUtIst.equals("DA");
        this.statusStanice = statusStanice;
        this.kategorijaPruge = kategorijaPruge;
        this.brojPerona = brojPerona;
        this.vrstaPruge = vrstaPruge;
        this.brojKolosjeka = brojKolosjeka;
        this.doPoOsovini = doPoOsovini;
        this.doPoDuznomM = doPoDuznomM;
        this.statusPruge = statusPruge;
        this.duzina = duzina;
        this.vrijemeNormalniVlak = vrijemeNormalniVlak;
        this.vrijemeUbrzaniVlak = vrijemeUbrzaniVlak;
        this.vrijemeBrziVlak = vrijemeBrziVlak;
    }

    public Stanica(Stanica stanica) {
        this.id = stanica.id;
        this.naziv = stanica.naziv;
        this.oznakaPruge = stanica.oznakaPruge;
        this.vrstaStanice = stanica.vrstaStanice;
        this.putniciUlIz = stanica.putniciUlIz;
        this.robaUtIst = stanica.robaUtIst;
        this.statusStanice = stanica.statusStanice;
        this.kategorijaPruge = stanica.kategorijaPruge;
        this.brojPerona = stanica.brojPerona;
        this.vrstaPruge = stanica.vrstaPruge;
        this.brojKolosjeka = stanica.brojKolosjeka;
        this.doPoOsovini = stanica.doPoOsovini;
        this.doPoDuznomM = stanica.doPoDuznomM;
        this.statusPruge = stanica.statusPruge;
        this.duzina = stanica.duzina;
        this.udaljenostOdPocetne = stanica.udaljenostOdPocetne;
        this.vrijemeNormalniOdPocetne = stanica.vrijemeNormalniOdPocetne;
        this.vrijemeUbrzaniOdPocetne = stanica.vrijemeUbrzaniOdPocetne;
        this.vrijemeBrziOdPocetne = stanica.vrijemeBrziOdPocetne;
        this.vrijemeNormalniVlak = stanica.vrijemeNormalniVlak;
        this.vrijemeUbrzaniVlak = stanica.vrijemeUbrzaniVlak;
        this.vrijemeBrziVlak = stanica.vrijemeBrziVlak;
    }

    public void setVrijemeBrziVlak(Integer vrijemeBrziVlak) {
        this.vrijemeBrziVlak = vrijemeBrziVlak;
    }

    public void setVrijemeUbrzaniVlak(Integer vrijemeUbrzaniVlak) {
        this.vrijemeUbrzaniVlak = vrijemeUbrzaniVlak;
    }

    public void setVrijemeNormalniVlak(Integer vrijemeNormalniVlak) {
        this.vrijemeNormalniVlak = vrijemeNormalniVlak;
    }

    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getOznakaPruge() {
        return oznakaPruge;
    }

    public StatusStanice getStatusStanice() {
        return statusStanice;
    }

    public VrstaStanice getVrstaStanice() {
        return vrstaStanice;
    }

    public boolean isPutniciUlIz() {
        return putniciUlIz;
    }

    public boolean isRobaUtIst() {
        return robaUtIst;
    }

    public KategorijaPrugeStanice getKategorijaPruge() {
        return kategorijaPruge;
    }

    public int getBrojPerona() {
        return brojPerona;
    }

    public VrstaPrugeStanice getVrstaPruge() {
        return vrstaPruge;
    }

    public int getBrojKolosjeka() {
        return brojKolosjeka;
    }

    public double getDoPoOsovini() {
        return doPoOsovini;
    }

    public double getDoPoDuznomM() {
        return doPoDuznomM;
    }

    public StatusPrugeStanice getStatusPruge() {
        return statusPruge;
    }

    public void setStatusPruge(StatusPrugeStanice statusPruge) {
        this.statusPruge = statusPruge;
    }

    public double getDuzina() {
        return duzina;
    }

    public void setDuzina(double duzina) {
        this.duzina = duzina;
    }

    public double getUdaljenostOdPocetne() {
        return udaljenostOdPocetne;
    }

    public void setUdaljenostOdPocetne(double udaljenostOdPocetne) {
        this.udaljenostOdPocetne = udaljenostOdPocetne;
    }

    public Integer getVrijemeNormalniVlak() {
        return vrijemeNormalniVlak;
    }

    public Integer getVrijemeUbrzaniVlak() {
        return vrijemeUbrzaniVlak;
    }

    public Integer getVrijemeBrziVlak() {
        return vrijemeBrziVlak;
    }

    @Override
    public String dohvatiIdentifikator() {
        return naziv;
    }

    @Override
    public Stanica kloniraj() {
        return new Stanica(this);
    }

    public LocalTime getVrijemeNormalniOdPocetne() {
        return vrijemeNormalniOdPocetne;
    }

    public void setVrijemeNormalniOdPocetne(LocalTime vrijemeNormalniOdPocetne) {
        this.vrijemeNormalniOdPocetne = vrijemeNormalniOdPocetne;
    }

    public LocalTime getVrijemeUbrzaniOdPocetne() {
        return vrijemeUbrzaniOdPocetne;
    }

    public void setVrijemeUbrzaniOdPocetne(LocalTime vrijemeUbrzaniOdPocetne) {
        this.vrijemeUbrzaniOdPocetne = vrijemeUbrzaniOdPocetne;
    }

    public LocalTime getVrijemeBrziOdPocetne() {
        return vrijemeBrziOdPocetne;
    }

    public void setVrijemeBrziOdPocetne(LocalTime vrijemeBrziOdPocetne) {
        this.vrijemeBrziOdPocetne = vrijemeBrziOdPocetne;
    }

    public Integer getPojedinacinoVrijemeVrstaVlaka(String vrstaVlaka) {
        if (vrstaVlaka.equals(NORMALNI.getOpis()))
            return this.getVrijemeNormalniVlak() != null ? this.getVrijemeNormalniVlak() : null;
        else if (vrstaVlaka.equals(UBRZANI.getOpis()))
            return this.getVrijemeUbrzaniVlak() != null ? this.getVrijemeUbrzaniVlak() : null;
        else if (vrstaVlaka.equals(BRZI.getOpis()))
            return this.getVrijemeBrziVlak() != null ? this.getVrijemeBrziVlak() : null;
        else return null;
    }

    public LocalTime getVrijemeVrstaVlaka(String vrstaVlaka) {
        if (vrstaVlaka.equals(NORMALNI.getOpis()))
            return this.getVrijemeNormalniOdPocetne();
        else if (vrstaVlaka.equals(UBRZANI.getOpis()))
            return this.getVrijemeUbrzaniOdPocetne();
        else return this.getVrijemeBrziOdPocetne();
    }

    public void setVrijemeVrstaVlaka(LocalTime vrijeme, String vrstaVlaka) {
        if (vrstaVlaka.equals(NORMALNI.getOpis()))
            this.setVrijemeNormalniOdPocetne(vrijeme);
        else if (vrstaVlaka.equals(UBRZANI.getOpis()))
            this.setVrijemeUbrzaniOdPocetne(vrijeme);
        else this.setVrijemeBrziOdPocetne(vrijeme);
    }
}
