package composite;

import modeli.pruga.Pruga;
import visitor.VozniRedVisitor;

public class EtapaList extends VozniRedKomponenta {
    private final String oznakaPruge;
    private final String polaznaStanica;
    private final String odredisnaStanica;
    private final String vrijemePolaska;
    private final String vrijemeDolaska;
    private final String daniUTjednu;
    private final String smjer;
    private final String vrstaVlaka;
    private final Pruga pruga;

    public EtapaList(String oznakaPruge, String polaznaStanica, String odredisnaStanica, String vrijemePolaska, String vrijemeDolaska,
                     String daniUTjednu, String smjer, String vrstaVlaka, Pruga pruga) {
        this.oznakaPruge = oznakaPruge;
        this.polaznaStanica = polaznaStanica;
        this.odredisnaStanica = odredisnaStanica;
        this.vrijemePolaska = vrijemePolaska;
        this.vrijemeDolaska = vrijemeDolaska;
        this.daniUTjednu = daniUTjednu;
        this.smjer = smjer;
        this.vrstaVlaka = vrstaVlaka;
        this.pruga = pruga;
    }

    public String getPolaznaStanica() {
        return polaznaStanica;
    }

    public String getOdredisnaStanica() {
        return odredisnaStanica;
    }

    public String getVrijemePolaska() {
        return vrijemePolaska;
    }

    public String getVrijemeDolaska() {
        return vrijemeDolaska;
    }

    public String getOznakaPruge() {
        return oznakaPruge;
    }

    public String getDaniUTjednu() {
        return daniUTjednu;
    }

    @Override
    public void prihvati(VozniRedVisitor visitor) {
        visitor.posjeti(this);
    }

    @Override
    public String dohvatiSvojstvoSortiranja() {
        return vrijemePolaska;
    }

    @Override
    public String dohvatiSekundarnoSvojstvoSortiranja() {
        return vrijemeDolaska;
    }

    public String getSmjer() {
        return smjer;
    }

    public String getVrstaVlaka() {
        return vrstaVlaka;
    }

    public Pruga getPruga() {
        return pruga;
    }
}
