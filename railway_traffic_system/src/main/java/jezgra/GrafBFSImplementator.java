package jezgra;

import modeli.pruga.Pruga;
import modeli.stanica.Stanica;

import java.util.*;
import java.util.stream.Collectors;

public class GrafBFSImplementator implements AlgoritamPutanjeImplementator {
    public String smjer = "N";
    private Map<String, List<Stanica>> graf;
    private final ZeljeznickiPromet zeljeznickiPromet;


    public GrafBFSImplementator(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
        this.graf = kreirajStrukturuOphodnje();
    }

    public GrafBFSImplementator(ZeljeznickiPromet zeljeznickiPromet, String smjer) {
        this.zeljeznickiPromet = zeljeznickiPromet;
        this.graf = kreirajStrukturuOphodnje();
        this.smjer = smjer;
    }

    public Map<String, List<Stanica>> kreirajStrukturuOphodnje() {
        graf = new HashMap<>();

        for (Pruga pruga : zeljeznickiPromet.getPruge()) {
            List<Stanica> staniceNaPrugi = pruga.getStanice();

            for (int i = 0; i < staniceNaPrugi.size(); i++) {
                Stanica trenutna = staniceNaPrugi.get(i);
                graf.putIfAbsent(trenutna.getNaziv(), new ArrayList<>());

                if (i > 0) {
                    Stanica prethodna = staniceNaPrugi.get(i - 1);

                    if (trenutna.getDuzina() == 0) continue;

                    if (i >= 2) {
                        Stanica prethodniOdPrethodnog = staniceNaPrugi.get(i - 2);

                        if (prethodna.getNaziv().equals(prethodniOdPrethodnog.getNaziv())) {
                            prethodna = prethodniOdPrethodnog;
                        }
                    }

                    dodajAkoNePostoji(graf, trenutna, prethodna);
                    dodajAkoNePostoji(graf, prethodna, trenutna);
                }

                if (i < staniceNaPrugi.size() - 1) {
                    Stanica sljedeca = staniceNaPrugi.get(i + 1);

                    if (sljedeca.getDuzina() == 0) continue;

                    if (i < staniceNaPrugi.size() - 2) {
                        Stanica sljedeciOdSljedeceg = staniceNaPrugi.get(i + 2);

                        if (sljedeca.getNaziv().equals(sljedeciOdSljedeceg.getNaziv())) {
                            sljedeca.setDuzina(Math.max(sljedeca.getDuzina(), sljedeciOdSljedeceg.getDuzina()));

                            sljedeca.setVrijemeNormalniVlak(
                                    Math.max(
                                            sljedeca.getVrijemeNormalniVlak() != null ? sljedeca.getVrijemeNormalniVlak() : 0,
                                            sljedeciOdSljedeceg.getVrijemeNormalniVlak() != null ? sljedeciOdSljedeceg.getVrijemeNormalniVlak() : 0
                                    )
                            );

                            sljedeca.setVrijemeUbrzaniVlak(
                                    Math.max(
                                            sljedeca.getVrijemeUbrzaniVlak() != null ? sljedeca.getVrijemeUbrzaniVlak() : 0,
                                            sljedeciOdSljedeceg.getVrijemeUbrzaniVlak() != null ? sljedeciOdSljedeceg.getVrijemeUbrzaniVlak() : 0
                                    )
                            );

                            sljedeca.setVrijemeBrziVlak(
                                    Math.max(sljedeca.getVrijemeBrziVlak() != null ? sljedeca.getVrijemeBrziVlak() : 0,
                                            sljedeciOdSljedeceg.getVrijemeBrziVlak() != null ? sljedeciOdSljedeceg.getVrijemeBrziVlak() : 0
                                    )
                            );

                            dodajAkoNePostoji(graf, trenutna, sljedeca);
                            dodajAkoNePostoji(graf, sljedeca, trenutna);

                            i += 2;
                            continue;
                        }
                    }

                    dodajAkoNePostoji(graf, trenutna, sljedeca);
                    dodajAkoNePostoji(graf, sljedeca, trenutna);
                }
            }
        }

        return graf;
    }

    private void dodajAkoNePostoji(Map<String, List<Stanica>> graf, Stanica trenutna, Stanica susjed) {
        graf.putIfAbsent(trenutna.getNaziv(), new ArrayList<>());
        var lista = graf.get(trenutna.getNaziv());
        if (lista.stream().noneMatch(el -> el.getNaziv().equals(susjed.getNaziv()))) {
            lista.add(susjed);
        }
    }

    public List<List<Stanica>> pronadjiPutanje(String pocetna, String odredisna) {
        var stanice = zeljeznickiPromet.getStanice();
        Queue<List<Stanica>> red = new LinkedList<>();
        List<List<Stanica>> svePutanje = new ArrayList<>();

        if (!stanice.containsKey(pocetna)) {
            return svePutanje;
        }

        Stanica pocetnaStanica = stanice.get(pocetna).getFirst();
        List<Stanica> pocetniPut = new ArrayList<>();
        pocetnaStanica.setUdaljenostOdPocetne(0);
        pocetniPut.add(pocetnaStanica);
        red.add(pocetniPut);

        while (!red.isEmpty()) {
            List<Stanica> trenutniPut = red.poll();
            Stanica trenutnaStanica = trenutniPut.getLast();

            if (trenutnaStanica.getNaziv().equals(odredisna)) {
                if (smjer.equals("O") && imaViseRazlicitihPruge(trenutniPut)) {
                    List<Stanica> prilagodeniPut = prilagodiPutZaSmjerO(trenutniPut);
                    if (prilagodeniPut != null) {
                        svePutanje.add(prilagodeniPut);
                    }
                } else {
                    svePutanje.add(new ArrayList<>(trenutniPut));
                }
                continue;
            }

            Set<String> lokalnoPosjecene = trenutniPut.stream()
                    .map(Stanica::getNaziv)
                    .collect(Collectors.toSet());

            List<Stanica> susjedneStanice = graf.get(trenutnaStanica.getNaziv());
            if (susjedneStanice == null) {
                continue;
            }

            for (Stanica susjedna : susjedneStanice) {
                if (lokalnoPosjecene.contains(susjedna.getNaziv())) {
                    continue;
                }

                List<Stanica> noviPutNaNovojPruzi = new ArrayList<>();
                boolean cijeliPutMoguc = true;

                for (Stanica s : stanice.get(susjedna.getNaziv())) {
                    if (susjedna.getNaziv().equals(s.getNaziv()) && s.getOznakaPruge().equals(trenutnaStanica.getOznakaPruge())) {
                        susjedna = s;
                    }
                }

                if (!trenutnaStanica.getOznakaPruge().equals(susjedna.getOznakaPruge())) {
                    String novaPruga = susjedna.getOznakaPruge();
                    var pruga = ZeljeznickiPromet.dohvatiInstancu().getPrugaPoOznaci(novaPruga);

                    if (pruga == null) continue;

                    List<Stanica> officialStaniceNaPrugi = pruga.getStanice();

                    for (Stanica staraStanica : trenutniPut) {
                        List<Stanica> candidateList = stanice.get(staraStanica.getNaziv());
                        if (candidateList == null) {
                            cijeliPutMoguc = false;
                            break;
                        }

                        Optional<Stanica> maybeNova = candidateList.stream()
                                .filter(s -> s.getOznakaPruge().equals(novaPruga))
                                .findFirst();

                        if (maybeNova.isEmpty()) {
                            cijeliPutMoguc = false;
                            break;
                        }

                        Stanica novaVarijanta = maybeNova.get().kloniraj();

                        noviPutNaNovojPruzi.add(novaVarijanta);
                    }

                    for (int i = 0; i < noviPutNaNovojPruzi.size() - 1; i++) {
                        Stanica s1 = noviPutNaNovojPruzi.get(i);
                        Stanica s2 = noviPutNaNovojPruzi.get(i + 1);

                        int idx1 = officialStaniceNaPrugi.indexOf(
                                officialStaniceNaPrugi.stream()
                                        .filter(st -> st.getNaziv().equals(s1.getNaziv())
                                                && st.getOznakaPruge().equals(s1.getOznakaPruge()))
                                        .findFirst().orElse(null)
                        );
                        int idx2 = officialStaniceNaPrugi.indexOf(
                                officialStaniceNaPrugi.stream()
                                        .filter(st -> st.getNaziv().equals(s2.getNaziv())
                                                && st.getOznakaPruge().equals(s2.getOznakaPruge()))
                                        .findFirst().orElse(null)
                        );

                        if (idx1 == -1 || idx2 == -1) {
                            cijeliPutMoguc = false;
                            break;
                        }

                        if (Math.abs(idx2 - idx1) != 1) {
                            cijeliPutMoguc = false;
                            break;
                        }
                    }

                    if (cijeliPutMoguc) {
                        for (int i = 0; i < noviPutNaNovojPruzi.size(); i++) {
                            if (i == 0) {
                                noviPutNaNovojPruzi.get(i).setUdaljenostOdPocetne(0.0);
                            } else {
                                Stanica prethodna = noviPutNaNovojPruzi.get(i - 1);
                                Stanica tekuca = noviPutNaNovojPruzi.get(i);

                                double edgeDist = (tekuca.getId() > prethodna.getId()) ? tekuca.getDuzina() : prethodna.getDuzina();

                                double novaUdaljenost = prethodna.getUdaljenostOdPocetne() + edgeDist;
                                tekuca.setUdaljenostOdPocetne(novaUdaljenost);
                            }
                        }
                        trenutniPut.clear();
                        trenutniPut.addAll(noviPutNaNovojPruzi);
                        trenutnaStanica = trenutniPut.getLast();
                    }

                    if (!trenutnaStanica.getOznakaPruge().equals(susjedna.getOznakaPruge())) {
                        for (Stanica s : stanice.get(trenutnaStanica.getNaziv())) {
                            if (s.getOznakaPruge().equals(trenutnaStanica.getOznakaPruge())) {

                                var temp = trenutnaStanica;
                                trenutnaStanica = s;

                                trenutnaStanica.setUdaljenostOdPocetne(temp.getUdaljenostOdPocetne());

                                break;
                            }
                        }
                    }
                }

                double novaUdaljenost = trenutnaStanica.getUdaljenostOdPocetne() +
                        (susjedna.getId() > trenutnaStanica.getId() ? susjedna.getDuzina() : trenutnaStanica.getDuzina());

                var susjednaCopy = susjedna.kloniraj();
                susjednaCopy.setUdaljenostOdPocetne(novaUdaljenost);

                List<Stanica> noviPut = new ArrayList<>(trenutniPut);
                noviPut.add(susjednaCopy);
                red.add(noviPut);
            }
        }

        return svePutanje;
    }

    private boolean imaViseRazlicitihPruge(List<Stanica> put) {
        Set<String> pruge = put.stream()
                .map(Stanica::getOznakaPruge)
                .collect(Collectors.toSet());
        return pruge.size() > 1;
    }

    private List<Stanica> prilagodiPutZaSmjerO(List<Stanica> put) {
        for (int i = 0; i < put.size() - 1; i++) {
            Stanica trenutna = put.get(i);
            Stanica sljedeca = put.get(i + 1);

            if (!trenutna.getOznakaPruge().equals(sljedeca.getOznakaPruge())) {
                String novaPruga = sljedeca.getOznakaPruge();

                List<Stanica> kandidati = zeljeznickiPromet.getStanice().values().stream()
                        .flatMap(List::stream)
                        .filter(s -> s.getOznakaPruge().equals(novaPruga) && s.getNaziv().equals(trenutna.getNaziv()))
                        .toList();

                if (!kandidati.isEmpty()) {
                    Stanica zamjena = kandidati.size() >= 2 ? kandidati.getLast() : kandidati.getFirst();

                    List<Stanica> prilagodeniPut = new ArrayList<>(put);
                    prilagodeniPut.set(i, zamjena);
                    return prilagodeniPut;
                } else {
                    return null;
                }
            }
        }
        return put;
    }

}
