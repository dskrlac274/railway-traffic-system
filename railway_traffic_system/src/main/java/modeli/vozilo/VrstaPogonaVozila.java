package modeli.vozilo;

import jezgra.OpisnaReprezentacija;

public enum VrstaPogonaVozila implements OpisnaReprezentacija {
    DIZEL("D"),
    EELEKTRICNA("E"),
    NEMA("N");

    private final String opis;

    VrstaPogonaVozila(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}
