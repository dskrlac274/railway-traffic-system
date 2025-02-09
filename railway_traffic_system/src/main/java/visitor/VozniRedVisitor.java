package visitor;

import composite.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class VozniRedVisitor {
    private final List<List<String>> tablica = new ArrayList<>();
    private List<String> red;

    public List<List<String>> getTablica() {
        return tablica;
    }

    public List<String> getRed() {
        return red;
    }

    public void setRed(List<String> red) {
        this.red = red;
    }

    public void posjeti(VozniRedKompozit vozniRedKompozit) {
        for (Map.Entry<String, VozniRedKomponenta> vlak : vozniRedKompozit.dohvatiJedinstvenuDjecu().entrySet()) {
            vlak.getValue().prihvati(this);
        }
    }

    public abstract void posjeti(VlakKompozit vlakKompozit);

    public abstract void posjeti(EtapaList etapaList);
}
