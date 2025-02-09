package visitor;

import composite.*;
import jezgra.ZeljeznickiPromet;
import modeli.stanica.Stanica;

import java.util.ArrayList;
import java.util.List;

public class PregledEtapaVlakaVisitor extends VozniRedVisitor {
    private final String oznakaVlaka;

    public PregledEtapaVlakaVisitor(String oznakaVlaka) {
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
        var red = this.getRed();

        List<Stanica> stanice = ZeljeznickiPromet.dohvatiInstancu()
                .dohvatiStaniceNaPrugiIzmedu(etapaList.getPolaznaStanica(), etapaList.getOdredisnaStanica(), etapaList.getSmjer());

        var staniceSmjer = ZeljeznickiPromet.dohvatiInstancu().akumulirajUdaljenostiStanica(stanice, etapaList.getPruga().getDuljina(),
                etapaList.getSmjer());

        var staniceVrijeme = ZeljeznickiPromet.dohvatiInstancu()
                .akumulirajVremenaStanica(
                        staniceSmjer, etapaList.getPruga().getUKupnoTrajanje(etapaList.getVrstaVlaka()),
                        etapaList.getVrijemePolaska(), etapaList.getVrstaVlaka(), etapaList.getSmjer());

        etapaList.getPruga().setStanice(staniceVrijeme);

        red.add(etapaList.getOznakaPruge());
        red.add(etapaList.getPolaznaStanica());
        red.add(etapaList.getOdredisnaStanica());
        red.add(etapaList.getVrijemePolaska());
        red.add(String.valueOf(etapaList.getPruga().getStanice().getLast().getVrijemeVrstaVlaka(etapaList.getVrstaVlaka())));
        red.add(String.valueOf(etapaList.getPruga().getStanice().getLast().getUdaljenostOdPocetne()));
        red.add(etapaList.getDaniUTjednu());

        this.getTablica().add(red);
    }
}
