package modeli.stanica;

import jezgra.OpisnaReprezentacija;

public enum VrstaStanice implements OpisnaReprezentacija {
    KOLODVOR("kol."),
    STAJALISTE("staj."),
    RASPUTNICA("rasp.");

    private final String opis;

    VrstaStanice(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}
