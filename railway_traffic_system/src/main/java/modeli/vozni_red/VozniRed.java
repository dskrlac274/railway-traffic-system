package modeli.vozni_red;

import jezgra.Zeljeznica;

public class VozniRed implements Zeljeznica {
    private final String oznakaPruge;
    private final SmjerVozniRed smjerVozniRed;
    private final String polaznaStanica;
    private final String odredisnaStanica;
    private final String oznakaVlaka;
    private final VrstaVlakaVozniRed vrstaVlakaVozniRed;
    private final String vrijemePolaska;
    private final String trajanjeVoznje;
    private final Integer oznakaDana;

    public VozniRed(String oznakaPruge, SmjerVozniRed smjerVozniRed, String polaznaStanica, String odredisnaStanica,
                    String oznakaVlaka, VrstaVlakaVozniRed vrstaVlakaVozniRed, String vrijemePolaska,
                    String trajanjeVoznje, Integer oznakaDana) {
        this.oznakaPruge = oznakaPruge;
        this.smjerVozniRed = smjerVozniRed;
        this.polaznaStanica = polaznaStanica;
        this.odredisnaStanica = odredisnaStanica;
        this.oznakaVlaka = oznakaVlaka;
        this.vrstaVlakaVozniRed = vrstaVlakaVozniRed;
        this.vrijemePolaska = vrijemePolaska;
        this.trajanjeVoznje = trajanjeVoznje;
        this.oznakaDana = oznakaDana;
    }

    public String getOznakaPruge() {
        return oznakaPruge;
    }

    public SmjerVozniRed getSmjerVozniRed() {
        return smjerVozniRed;
    }

    public String getPolaznaStanica() {
        return polaznaStanica;
    }

    public String getOdredisnaStanica() {
        return odredisnaStanica;
    }

    public String getOznakaVlaka() {
        return oznakaVlaka;
    }

    public VrstaVlakaVozniRed getVrstaVlakaVozniRed() {
        return vrstaVlakaVozniRed;
    }

    public String getVrijemePolaska() {
        return vrijemePolaska;
    }

    public String getTrajanjeVoznje() {
        return trajanjeVoznje;
    }

    public int getOznakaDana() {
        return oznakaDana;
    }

    @Override
    public String dohvatiIdentifikator() {
        return oznakaVlaka;
    }
}
