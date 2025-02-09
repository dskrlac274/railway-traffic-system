package composite;

import visitor.VozniRedVisitor;

import java.util.List;
import java.util.Map;

public abstract class VozniRedKomponenta {
    public VozniRedKomponenta dodaj(VozniRedKomponenta komponenta, String kljuc) {
        throw new UnsupportedOperationException("Ova komponenta je list.");
    }

    public void dodaj(VozniRedKomponenta komponenta) {
        throw new UnsupportedOperationException("Ova komponenta je list.");
    }

    public void obrisi(String kljuc) {
        throw new UnsupportedOperationException("Ova komponenta ne podržava brisanje.");
    }

    public List<VozniRedKomponenta> dohvatiDjecu() {
        throw new UnsupportedOperationException("Ova komponenta nema djece ili pristup do njih nije moguc.");
    }

    public Map<String, VozniRedKomponenta> dohvatiJedinstvenuDjecu() {
        throw new UnsupportedOperationException("Ova komponenta nema jedinstvenu djecu ili pristup do njih nije moguc.");
    }

    public String dohvatiSvojstvoSortiranja() {
        throw new UnsupportedOperationException("Ova komponenta nema definirano svojstvo sortiranja.");
    }

    public String dohvatiSekundarnoSvojstvoSortiranja() {
        throw new UnsupportedOperationException("Ova komponenta nema definirano svojstvo sortiranja.");
    }

    public abstract void prihvati(VozniRedVisitor visitor);
}
