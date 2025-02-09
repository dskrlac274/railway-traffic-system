package modeli.stanica;

import jezgra.OpisnaReprezentacija;

public enum KategorijaPrugeStanice implements OpisnaReprezentacija {
    LOKALNA("L"),
    MEDUNARODNA("M"),
    REGIONALNA("R");

    private final String opis;

    KategorijaPrugeStanice(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}
