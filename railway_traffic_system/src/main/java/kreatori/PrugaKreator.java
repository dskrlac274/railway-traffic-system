package kreatori;

import jezgra.ZeljeznickiPromet;
import modeli.pruga.Pruga;
import modeli.stanica.Stanica;

import java.util.*;

public class PrugaKreator {
    public void kreirajPruge(Map<String, List<Stanica>> staniceGroupedByPruga) {
        staniceGroupedByPruga.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 2)
                .forEach(entry -> {
                    String oznakaPruge = entry.getKey();
                    List<Stanica> staniceNaPrugi = entry.getValue().stream()
                            .sorted(Comparator.comparingInt(Stanica::getId))
                            .map(Stanica::kloniraj)
                            .toList();

                    dodajPrugu(oznakaPruge, staniceNaPrugi);
                });
    }

    private void dodajPrugu(String oznakaPruge, List<Stanica> staniceNaPrugi) {
        double ukupnaDuzina = staniceNaPrugi.stream().mapToDouble(Stanica::getDuzina).sum();

        if (!staniceNaPrugi.isEmpty()) {
            double kumulativnaUdaljenost = 0.0;
            staniceNaPrugi.getFirst().setUdaljenostOdPocetne(0.0);

            for (int i = 1; i < staniceNaPrugi.size(); i++) {
                Stanica stanica = staniceNaPrugi.get(i);
                kumulativnaUdaljenost += stanica.getDuzina();
                stanica.setUdaljenostOdPocetne(kumulativnaUdaljenost);
            }
        }

        var pruga = new Pruga(
                oznakaPruge,
                staniceNaPrugi.getFirst().getNaziv(),
                staniceNaPrugi.getLast().getNaziv(),
                ukupnaDuzina,
                staniceNaPrugi
        );
        ZeljeznickiPromet.dohvatiInstancu().getPruge().add(pruga);

        pruga.evidentirajStatuseRelacija();
    }
}

