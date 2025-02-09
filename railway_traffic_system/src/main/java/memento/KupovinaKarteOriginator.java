package memento;

import modeli.KupovinaKarte;

public class KupovinaKarteOriginator {
    private KupovinaKarte stanje;

    public void setStanje(KupovinaKarte kupnja) {
        this.stanje = kupnja;
    }

    public KupovinaKarteMemento save() {
        return new KupovinaKarteMemento(stanje);
    }

    public void restore(KupovinaKarteMemento memento) {
        this.stanje = memento.getStanjeKupnje();
    }

    public KupovinaKarte getStanje() {
        return stanje;
    }
}
