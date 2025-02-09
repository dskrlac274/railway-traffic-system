package composite;

import jezgra.Zeljeznica;
import visitor.VozniRedVisitor;

import java.util.HashMap;
import java.util.Map;

public class VozniRedKompozit extends VozniRedKomponenta implements Zeljeznica {
    private final int id;
    private final Map<String, VozniRedKomponenta> vlakovi = new HashMap<>();

    public VozniRedKompozit(int id) {
        this.id = id;
    }

    @Override
    public VozniRedKomponenta dodaj(VozniRedKomponenta komponenta, String kljuc) {
        if (!vlakovi.containsKey(kljuc)) {
            vlakovi.put(kljuc, komponenta);
            return komponenta;
        }
        return vlakovi.get(kljuc);
    }

    @Override
    public void obrisi(String kljuc) {
        vlakovi.remove(kljuc);
    }

    @Override
    public Map<String, VozniRedKomponenta> dohvatiJedinstvenuDjecu() {
        return vlakovi;
    }

    @Override
    public String dohvatiIdentifikator() {
        return String.valueOf(id);
    }

    @Override
    public void prihvati(VozniRedVisitor visitor) {
        visitor.posjeti(this);
    }
}
