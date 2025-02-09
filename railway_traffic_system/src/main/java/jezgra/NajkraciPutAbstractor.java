package jezgra;

import modeli.stanica.Stanica;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NajkraciPutAbstractor extends AlgoritamPutanjeAbstractor {
    public NajkraciPutAbstractor(AlgoritamPutanjeImplementator implementacija) {
        super(implementacija);
    }

    public List<Stanica> pronadjiNajkraciPut(String pocetna, String odredisna) {
        List<List<Stanica>> svePutanje = pronadjiPutanje(pocetna, odredisna);

        if (svePutanje.isEmpty()) {
            return new ArrayList<>();
        }

        return svePutanje.stream()
                .min(Comparator.comparingDouble(put -> put.getLast().getUdaljenostOdPocetne()))
                .orElse(new ArrayList<>());
    }
}
