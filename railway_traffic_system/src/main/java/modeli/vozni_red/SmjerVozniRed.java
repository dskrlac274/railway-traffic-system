package modeli.vozni_red;

import jezgra.OpisnaReprezentacija;

public enum SmjerVozniRed implements OpisnaReprezentacija {
    NORMALNI("N"),
    OBRNUTI("O");

    private final String opis;

    SmjerVozniRed(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}
