package composite;

import pomagaci.VrijemePomagac;
import visitor.VozniRedVisitor;

import java.util.ArrayList;
import java.util.List;

public class VlakKompozit extends VozniRedKomponenta {
    private final String oznaka;
    private final String vrstaVlaka;

    private final List<VozniRedKomponenta> etape = new ArrayList<>();

    public VlakKompozit(String oznaka, String vrstaVlaka) {
        this.oznaka = oznaka;
        this.vrstaVlaka = vrstaVlaka;
    }

    public String getOznaka() {
        return oznaka;
    }

    @Override
    public void dodaj(VozniRedKomponenta komponenta) {
        etape.add(komponenta);
        sortirajKronoloskiDatumPolaskaIDolaska();
    }

    @Override
    public List<VozniRedKomponenta> dohvatiDjecu() {
        return etape;
    }

    @Override
    public void prihvati(VozniRedVisitor visitor) {
        visitor.posjeti(this);
    }

    public void sortirajKronoloskiDatumPolaskaIDolaska() {
        etape.sort((o1, o2) -> {
            String primarnoVrijeme1 = o1.dohvatiSvojstvoSortiranja();
            String primarnoVrijeme2 = o2.dohvatiSvojstvoSortiranja();

            String sekundarnoVrijeme1 = o1.dohvatiSekundarnoSvojstvoSortiranja();
            String sekundarnoVrijeme2 = o2.dohvatiSekundarnoSvojstvoSortiranja();

            int rezultatPrimarno = usporediVrijeme(primarnoVrijeme1, primarnoVrijeme2);

            int rezultatSekundarno = usporediVrijeme(sekundarnoVrijeme1, sekundarnoVrijeme2);

            return rezultatPrimarno != 0 ? rezultatPrimarno : rezultatSekundarno;
        });
    }

    private int usporediVrijeme(String vrijeme1, String vrijeme2) {
        if (vrijeme1 != null && vrijeme2 != null && !vrijeme1.isEmpty() && !vrijeme2.isEmpty()) {
            return VrijemePomagac.getVrijemeKaoTime(vrijeme1)
                    .compareTo(VrijemePomagac.getVrijemeKaoTime(vrijeme2));
        }
        return 0;
    }

    public String getVrstaVlaka() {
        return vrstaVlaka;
    }
}
