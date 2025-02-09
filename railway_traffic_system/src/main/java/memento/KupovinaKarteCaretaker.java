package memento;

import java.util.ArrayList;
import java.util.List;

public class KupovinaKarteCaretaker {
    private final List<KupovinaKarteMemento> mementi = new ArrayList<>();

    public void addMemento(KupovinaKarteMemento memento) {
        mementi.add(memento);
    }

    public KupovinaKarteMemento getMemento(int index) {
        if (index < 0 || index >= mementi.size()) {
            return null;
        }
        return mementi.get(index);
    }

    public int getBrojMementa() {
        return mementi.size();
    }

    public void removeMemento(int index) {
        if (index >= 0 && index < mementi.size()) {
            mementi.remove(index);
        }
    }

}
