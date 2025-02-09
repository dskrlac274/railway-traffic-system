package modeli.kompozicija;

import jezgra.OpisnaReprezentacija;

public enum UlogaKompozicije implements OpisnaReprezentacija {
    POGON("P"),
    VAGON("V");

    private final String opis;

    UlogaKompozicije(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}