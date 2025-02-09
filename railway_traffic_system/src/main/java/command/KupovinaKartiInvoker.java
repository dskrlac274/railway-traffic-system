package command;

import java.util.ArrayList;
import java.util.List;

public class KupovinaKartiInvoker {
    private final List<KarteCommand> povijest = new ArrayList<>();

    public void izvrsiKomandu(KarteCommand komanda) {
        povijest.add(komanda);
        komanda.izvrsi();
    }

    public void ponistiKomanduNti(int i) {
        if (i <= 0) {
            System.out.println("Parametar i mora biti > 0.");
            return;
        }
        if (i > povijest.size()) {
            System.out.printf("Nema toliko (%d) komandi u povijesti.%n", i);
            return;
        }

        int indexTrazeni = povijest.size() - i;
        KarteCommand komanda = povijest.get(indexTrazeni);

        komanda.ponisti(i);

        povijest.remove(indexTrazeni);
    }
}
