package memento;

import modeli.KupovinaKarte;

public class KupovinaKarteMemento {
    private final KupovinaKarte snapshot;

    public KupovinaKarteMemento(KupovinaKarte original) {
        this.snapshot = kopiraj(original);
    }

    public KupovinaKarte getStanjeKupnje() {
        return kopiraj(snapshot);
    }

    private KupovinaKarte kopiraj(KupovinaKarte k) {
        KupovinaKarte nova = new KupovinaKarte();
        nova.setOznakaVlaka(k.getOznakaVlaka());
        nova.setPolaznaStanica(k.getPolaznaStanica());
        nova.setOdredisnaStanica(k.getOdredisnaStanica());
        nova.setDatumPutovanja(k.getDatumPutovanja());
        nova.setVrijemePolaska(k.getVrijemePolaska());
        nova.setVrijemeDolaska(k.getVrijemeDolaska());
        nova.setIzvornaCijena(k.getIzvornaCijena());
        nova.setPopustiUkupno(k.getPopustiUkupno());
        nova.setKonacnaCijena(k.getKonacnaCijena());
        nova.setNacinKupovine(k.getNacinKupovine());
        nova.setDatumVrijemeKupovine(k.getDatumVrijemeKupovine());
        return nova;
    }
}
