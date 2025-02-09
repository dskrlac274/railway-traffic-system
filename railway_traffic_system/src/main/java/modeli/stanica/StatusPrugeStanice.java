package modeli.stanica;

import jezgra.OpisnaReprezentacija;

public enum StatusPrugeStanice implements OpisnaReprezentacija {
    ISPRAVNA("I"),
    U_KVARU("K"),
    ZATVORENA("Z"),
    U_TESTIRANJU("T");


    private final String opis;

    StatusPrugeStanice(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}
