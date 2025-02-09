package modeli.stanica;

import jezgra.OpisnaReprezentacija;

public enum VrstaPrugeStanice implements OpisnaReprezentacija {
    KLASICNO("K"),
    ELEKTRICNA("E");

    private final String opis;

    VrstaPrugeStanice(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}
