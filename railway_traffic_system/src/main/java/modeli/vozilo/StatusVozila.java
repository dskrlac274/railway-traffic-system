package modeli.vozilo;

import jezgra.OpisnaReprezentacija;

public enum StatusVozila implements OpisnaReprezentacija {
    ISPRAVNO("I"),
    U_KVARU("K");

    private final String opis;

    StatusVozila(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}

