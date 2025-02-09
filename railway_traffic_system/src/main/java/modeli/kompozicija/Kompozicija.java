package modeli.kompozicija;

import jezgra.Zeljeznica;

public class Kompozicija implements Zeljeznica {
    private final String oznaka;
    private final String oznakaPrijevoznogSredstva;
    private final UlogaKompozicije uloga;

    public Kompozicija(String oznaka, String oznakaPrijevoznogSredstva, UlogaKompozicije uloga) {
        this.oznaka = oznaka;
        this.oznakaPrijevoznogSredstva = oznakaPrijevoznogSredstva;
        this.uloga = uloga;
    }

    public String getOznakaPrijevoznogSredstva() {
        return oznakaPrijevoznogSredstva;
    }

    public UlogaKompozicije getUloga() {
        return uloga;
    }

    @Override
    public String dohvatiIdentifikator() {
        return oznaka;
    }
}
