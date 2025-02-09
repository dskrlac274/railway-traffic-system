package visitor;

import composite.EtapaList;
import jezgra.ZeljeznickiPromet;
import modeli.stanica.Stanica;
import composite.VlakKompozit;
import composite.VozniRedKomponenta;

import java.util.ArrayList;
import java.util.List;

public class PregledEtapaVlakaDaniVisitor extends VozniRedVisitor {
    private final String dani;

    public PregledEtapaVlakaDaniVisitor(String dani) {
        this.dani = dani;
    }

    @Override
    public void posjeti(VlakKompozit vlakKompozit) {
        for (VozniRedKomponenta etapa : vlakKompozit.dohvatiDjecu()) {
            List<String> red = new ArrayList<>();
            this.setRed(red);

            red.add(vlakKompozit.getOznaka());
            etapa.prihvati(this);
        }
    }

    @Override
    public void posjeti(EtapaList etapaList) {
        if (etapaList.getDaniUTjednu().contains(dani)) {
            var red = this.getRed();

            List<Stanica> stanice = ZeljeznickiPromet.dohvatiInstancu()
                    .dohvatiStaniceNaPrugiIzmedu(etapaList.getPolaznaStanica(), etapaList.getOdredisnaStanica(), etapaList.getSmjer());

            var staniceVrijeme = ZeljeznickiPromet.dohvatiInstancu()
                    .akumulirajVremenaStanica(
                            stanice, etapaList.getPruga().getUKupnoTrajanje(etapaList.getVrstaVlaka()),
                            etapaList.getVrijemePolaska(), etapaList.getVrstaVlaka(), etapaList.getSmjer());

            etapaList.getPruga().setStanice(staniceVrijeme);

            red.add(etapaList.getOznakaPruge());
            red.add(etapaList.getPolaznaStanica());
            red.add(etapaList.getOdredisnaStanica());
            red.add(etapaList.getVrijemePolaska());
            red.add(String.valueOf(etapaList.getPruga().getStanice().getLast().getVrijemeVrstaVlaka(etapaList.getVrstaVlaka())));
            red.add(etapaList.getDaniUTjednu());

            this.getTablica().add(red);
        }
    }
}