package modeli.vozni_red;

import jezgra.OpisnaReprezentacija;

public enum VrstaVlakaVozniRed implements OpisnaReprezentacija {
    NORMALNI(""),
    UBRZANI("U"),
    BRZI("B");

    private final String opis;

    VrstaVlakaVozniRed(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}
