package modeli.vozilo;

public class VoziloTipFlyweight {
    private final String proizvodac;
    private final VrstaPogonaVozila vrstaPogona;
    private final NamjenaVozila namjenaVozila;

    public VoziloTipFlyweight(String proizvodac, NamjenaVozila namjenaVozila, VrstaPogonaVozila vrstaPogona) {
        this.proizvodac = proizvodac;
        this.namjenaVozila = namjenaVozila;
        this.vrstaPogona = vrstaPogona;
    }

    public String getProizvodac() {
        return proizvodac;
    }

    public VrstaPogonaVozila getVrstaPogona() {
        return vrstaPogona;
    }

    public NamjenaVozila getNamjenaVozila() {
        return namjenaVozila;
    }
}
