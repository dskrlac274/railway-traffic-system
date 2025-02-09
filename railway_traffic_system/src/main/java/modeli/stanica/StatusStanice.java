package modeli.stanica;

import jezgra.OpisnaReprezentacija;

public enum StatusStanice implements OpisnaReprezentacija {
    OTVORENA("O"),
    ZATVORENA("Z");

    private final String opis;

    StatusStanice(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}
