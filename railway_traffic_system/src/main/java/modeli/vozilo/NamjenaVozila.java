package modeli.vozilo;

import jezgra.OpisnaReprezentacija;

public enum NamjenaVozila implements OpisnaReprezentacija {
    PRIJEVOZNO_SREDSTVO_S_VLASTITIOM_POGONOM_ZA_VUCU("PSVPVK"),
    PRIJEVOZNO_SREDSTVO_S_VLASTITIOM_POGONOM("PSVP"),
    PRIJEVOZNO_SREDSTVO_BEZ_POGONA("PSBP");

    private final String opis;

    NamjenaVozila(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}
