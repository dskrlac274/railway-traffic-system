package jezgra;

import modeli.stanica.Stanica;

import java.util.List;

public interface AlgoritamPutanjeImplementator {
    List<List<Stanica>> pronadjiPutanje(String pocetna, String odredisna);
}
