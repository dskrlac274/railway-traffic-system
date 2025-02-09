package modeli.vozilo;

import java.util.HashMap;
import java.util.Map;

public class VoziloFlyweightTvornica {
    private static final Map<String, VoziloTipFlyweight> flyweights = new HashMap<>();

    public static VoziloTipFlyweight getFlyweight(String proizvodac, NamjenaVozila namjenaVozila, VrstaPogonaVozila vrstaPogona) {
        String key = proizvodac + "_" + namjenaVozila + "_" + vrstaPogona.getOpis();
        if (!flyweights.containsKey(key)) {
            flyweights.put(key, new VoziloTipFlyweight(proizvodac, namjenaVozila, vrstaPogona));
        }
        return flyweights.get(key);
    }
}
