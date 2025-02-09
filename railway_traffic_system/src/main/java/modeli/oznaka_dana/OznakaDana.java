package modeli.oznaka_dana;

import jezgra.Zeljeznica;

public class OznakaDana implements Zeljeznica {
    private final int oznakaDana;
    private final String daniVoznje;

    public OznakaDana(int oznakaDana, String daniVoznje) {
        this.oznakaDana = oznakaDana;
        this.daniVoznje = daniVoznje;
    }

    public int getOznakaDana() { return oznakaDana; }
    public String getDaniVoznje() { return daniVoznje; }

    @Override
    public String dohvatiIdentifikator() {
        return String.valueOf(oznakaDana);
    }
}
