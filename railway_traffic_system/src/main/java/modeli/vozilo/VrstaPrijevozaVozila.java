package modeli.vozilo;

import jezgra.OpisnaReprezentacija;

public enum VrstaPrijevozaVozila implements OpisnaReprezentacija {
    NEMA("N"),
    PUTNICKO__SREDSTVO("P"),
    TERETNO_AUTOMOBILI("TA"),
    TERETNO_KONTEJNER("TK"),
    TERETNO_ROBA_RASUTO("TRS"),
    TERETNO_ROBA_TEKUCE("TTS");

    private final String opis;

    VrstaPrijevozaVozila(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}