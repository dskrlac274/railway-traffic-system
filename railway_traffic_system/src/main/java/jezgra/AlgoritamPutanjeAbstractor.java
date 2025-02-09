package jezgra;

import modeli.stanica.Stanica;

import java.util.List;

public class AlgoritamPutanjeAbstractor {
    protected AlgoritamPutanjeImplementator implementacija;

    public AlgoritamPutanjeAbstractor(AlgoritamPutanjeImplementator implementacija) {
        this.implementacija = implementacija;
    }

    public List<List<Stanica>> pronadjiPutanje(String pocetna, String odredisna) {
        return implementacija.pronadjiPutanje(pocetna, odredisna);
    }
}
