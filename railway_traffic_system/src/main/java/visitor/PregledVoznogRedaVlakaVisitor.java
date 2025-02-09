package visitor;

import composite.EtapaList;
import composite.VlakKompozit;
import composite.VozniRedKomponenta;
import jezgra.ZeljeznickiPromet;
import modeli.stanica.Stanica;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PregledVoznogRedaVlakaVisitor extends VozniRedVisitor {

    private final String oznakaVlaka;
    private double akumuliranaUdaljenost = 0.0;

    public final List<EtapaList> etape = new ArrayList<>();

    public PregledVoznogRedaVlakaVisitor(String oznakaVlaka) {
        this.oznakaVlaka = oznakaVlaka;
    }

    @Override
    public void posjeti(VlakKompozit vlakKompozit) {
        if (vlakKompozit.getOznaka().equals(oznakaVlaka)) {
            for (VozniRedKomponenta etapa : vlakKompozit.dohvatiDjecu()) {
                List<String> red = new ArrayList<>();
                this.setRed(red);

                red.add(vlakKompozit.getOznaka());
                etapa.prihvati(this);
            }
        }
    }

    @Override
    public void posjeti(EtapaList etapaList) {


        var vlakOznaka = this.getRed().getLast();

        List<Stanica> stanice = ZeljeznickiPromet.dohvatiInstancu()
                .dohvatiStaniceNaPrugiIzmedu(etapaList.getPolaznaStanica(), etapaList.getOdredisnaStanica(), etapaList.getSmjer());

        var staniceSmjer = ZeljeznickiPromet.dohvatiInstancu().akumulirajUdaljenostiStanica(stanice, etapaList.getPruga().getDuljina(),
                etapaList.getSmjer());

        var staniceVrijeme = ZeljeznickiPromet.dohvatiInstancu()
                .akumulirajVremenaStanica(
                        staniceSmjer, etapaList.getPruga().getUKupnoTrajanje(etapaList.getVrstaVlaka()),
                        etapaList.getVrijemePolaska(), etapaList.getVrstaVlaka(), etapaList.getSmjer());

        etapaList.getPruga().setStanice(staniceVrijeme);

        double akumuliranaUdaljenost = getAkumuliranaUdaljenost();

        List<String> prazanRed = new ArrayList<>();

        for (Stanica stanica : etapaList.getPruga().getStanice()) {
            LocalTime vrijemeVrstaVlaka = stanica.getVrijemeVrstaVlaka(etapaList.getVrstaVlaka());

            if (vrijemeVrstaVlaka == null) {
                continue;
            }

            List<String> red = new ArrayList<>();
            red.add(vlakOznaka);
            red.add(etapaList.getOznakaPruge());
            red.add(stanica.getNaziv());
            red.add(String.valueOf(vrijemeVrstaVlaka));

            double ukupnaUdaljenost = akumuliranaUdaljenost + stanica.getUdaljenostOdPocetne();
            red.add(String.valueOf(ukupnaUdaljenost));

            this.getTablica().add(red);
        }

        akumuliranaUdaljenost += etapaList.getPruga().getStanice()
                .stream()
                .mapToDouble(Stanica::getUdaljenostOdPocetne)
                .max()
                .orElse(0.0);

        this.setAkumuliranaUdaljenost(akumuliranaUdaljenost);

        this.getTablica().add(prazanRed);
        etape.add(etapaList);
    }

    public double getAkumuliranaUdaljenost() {
        return akumuliranaUdaljenost;
    }

    public void setAkumuliranaUdaljenost(double udaljenost) {
        this.akumuliranaUdaljenost = udaljenost;
    }
}
