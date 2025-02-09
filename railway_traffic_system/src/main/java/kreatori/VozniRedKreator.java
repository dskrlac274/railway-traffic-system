package kreatori;

import composite.EtapaList;
import composite.VlakKompozit;
import composite.VozniRedKomponenta;
import composite.VozniRedKompozit;
import jezgra.ZeljeznickiPromet;
import modeli.oznaka_dana.OznakaDana;
import modeli.pruga.Pruga;
import modeli.stanica.Stanica;
import modeli.vozni_red.SmjerVozniRed;
import pomagaci.CsvPomagac;
import pomagaci.VrijemePomagac;
import validator.VozniRedValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VozniRedKreator extends UcitavacPodatakaKreator<VozniRedKompozit> {
    private final VozniRedValidator vozniredValidator = new VozniRedValidator();
    private static int sljedeciId = 0;
    private final VozniRedKompozit vozniRedKompozit = new VozniRedKompozit(++sljedeciId);

    @Override
    protected String validirajVrijednostiRetka(String[] polja) {
        return vozniredValidator.validirajRedak(polja);
    }

    @Override
    protected String zavrsnaObrada() {
        VozniRedKompozit vozniRed = ZeljeznickiPromet.dohvatiInstancu().getVozniRedKompozit();
        List<String> kljuceviZaBrisanje = new ArrayList<>();

        if (vozniRed == null) return null;

        for (Map.Entry<String, VozniRedKomponenta> ulaz : vozniRed.dohvatiJedinstvenuDjecu().entrySet()) {
            String oznakaVlaka = ulaz.getKey();
            VlakKompozit vlak = (VlakKompozit) ulaz.getValue();

            if (!vozniredValidator.provjeriSekvencuEtapa(vlak.dohvatiDjecu(), oznakaVlaka) ||
                    !vozniredValidator.provjeriTipoveVlakova(Map.of(oznakaVlaka, vlak))) {
                kljuceviZaBrisanje.add(oznakaVlaka);
                continue;
            }

            for (VozniRedKomponenta komponenta : vlak.dohvatiDjecu()) {
                if (komponenta instanceof EtapaList etapa && !vozniredValidator.provjeriEtapu(etapa, oznakaVlaka)) {
                    kljuceviZaBrisanje.add(oznakaVlaka);
                    break;
                }
            }
        }

        for (String kljuc : kljuceviZaBrisanje) {
            vozniRed.obrisi(kljuc);
        }

        return null;
    }

    @Override
    protected RezultatKreiranja<VozniRedKompozit> kreirajModel(String[] polja) {
        String oznakaVlaka = CsvPomagac.dohvatiString(polja, 4);
        String vrstaVlaka = CsvPomagac.dohvatiString(polja, 5);
        String oznakaPruge = CsvPomagac.dohvatiString(polja, 0);
        String smjer = CsvPomagac.dohvatiString(polja, 1);
        String polaznaStanica = CsvPomagac.dohvatiString(polja, 2);
        String odredisnaStanica = CsvPomagac.dohvatiString(polja, 3);
        String vrijemePolaska = CsvPomagac.dohvatiString(polja, 6);
        Integer oznakaDanaBroj = CsvPomagac.dohvatiInteger(polja, 8);
        String oznakaDana = "";

        Pruga pruga = ZeljeznickiPromet.dohvatiInstancu().getPrugaPoOznaci(oznakaPruge);

        if (pruga == null) return RezultatKreiranja.neuspjeh("Pruga nije valjana");

        if (polaznaStanica.isEmpty())
            polaznaStanica = "N".equals(smjer) ? pruga.getPocetnaStanica() : pruga.getZavrsnaStanica();

        if (odredisnaStanica.isEmpty())
            odredisnaStanica = "N".equals(smjer) ? pruga.getZavrsnaStanica() : pruga.getPocetnaStanica();

        if (oznakaDanaBroj != null) {
            oznakaDana = ZeljeznickiPromet.dohvatiInstancu()
                    .getOznakeDana().stream()
                    .filter(d -> d.getOznakaDana() == oznakaDanaBroj)
                    .map(OznakaDana::getDaniVoznje)
                    .findFirst()
                    .orElse("PoUSrČPeSuN");
        } else {
            oznakaDana = "PoUSrČPeSuN";
        }

        List<Stanica> stanice = ZeljeznickiPromet.dohvatiInstancu()
                .dohvatiStaniceNaPrugiIzmedu(polaznaStanica, odredisnaStanica, smjer);

        if (stanice.isEmpty() || stanice.size() < 2) return RezultatKreiranja.neuspjeh("Pruga nije valjana");

        List<Stanica> procesiranaLista = procesirajListu(smjer, stanice);

        double ukupnaDuzina = procesiranaLista.stream()
                .mapToDouble(Stanica::getDuzina)
                .sum();

        int v1 = ZeljeznickiPromet.dohvatiInstancu()
                .izracunajUkupnoVrijemeStanica(procesiranaLista, Stanica::getVrijemeNormalniVlak);

        int v2 = ZeljeznickiPromet.dohvatiInstancu()
                .izracunajUkupnoVrijemeStanica(procesiranaLista, Stanica::getVrijemeUbrzaniVlak);

        int v3 = ZeljeznickiPromet.dohvatiInstancu()
                .izracunajUkupnoVrijemeStanica(procesiranaLista, Stanica::getVrijemeBrziVlak);

        var novaPruga = new Pruga(oznakaPruge, polaznaStanica, odredisnaStanica, ukupnaDuzina, v1, v2, v3);

        var staniceSmjer = ZeljeznickiPromet.dohvatiInstancu().akumulirajUdaljenostiStanica(stanice, ukupnaDuzina,
                smjer);

        novaPruga.setStanice(ZeljeznickiPromet.dohvatiInstancu()
                .akumulirajVremenaStanica(
                        staniceSmjer, novaPruga.getUKupnoTrajanje(vrstaVlaka), vrijemePolaska, vrstaVlaka, smjer));

        EtapaList etapa = new EtapaList(oznakaPruge, polaznaStanica, odredisnaStanica, vrijemePolaska,
                VrijemePomagac.getVrijemeKaoString(novaPruga.getStanice().getLast().getVrijemeVrstaVlaka(vrstaVlaka)),
                oznakaDana, smjer, vrstaVlaka, novaPruga);

        VlakKompozit vlak = new VlakKompozit(oznakaVlaka, vrstaVlaka);

        vozniRedKompozit.dodaj(vlak, oznakaVlaka).dodaj(etapa);

        return RezultatKreiranja.uspjeh(vozniRedKompozit);
    }

    public static List<Stanica> procesirajListu(String smjer, List<Stanica> stanice) {
        if (smjer.equals("N")) {
            return stanice.stream().skip(1).collect(Collectors.toList());
        } else {
            List<Stanica> reversedList = new ArrayList<>(stanice);
            Collections.reverse(reversedList);
            return reversedList.stream().skip(1).collect(Collectors.toList());
        }
    }

    @Override
    protected void spremiModel(VozniRedKompozit model) {
        ZeljeznickiPromet.dohvatiInstancu().setVozniRedKompozit(model);
    }
}
