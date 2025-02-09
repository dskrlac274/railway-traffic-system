package modeli.vozilo;

import jezgra.Zeljeznica;

public class Vozilo implements Zeljeznica {
    private final VoziloTipFlyweight voziloTip;
    private final String oznaka;
    private final String opis;
    private final String proizvodac;
    private final int godina;
    private final NamjenaVozila namjena;
    private final VrstaPrijevozaVozila vrstaPrijevoza;
    private final VrstaPogonaVozila vrstaPogona;
    private final double maksBrzina;
    private final double maksSnaga;
    private final int brojSjedecihMjesta;
    private final int brojStajacihMjesta;
    private final int brojBicikala;
    private final int brojKreveta;
    private final int brojAutomobila;
    private final double nosivost;
    private final double povrsina;
    private final double zapremina;
    private final StatusVozila status;

    public Vozilo(String oznaka, String opis, String proizvodac, int godina, NamjenaVozila namjena,
                  VrstaPrijevozaVozila vrstaPrijevoza, VrstaPogonaVozila vrstaPogona, double maksBrzina, double maksSnaga,
                  int brojSjedecihMjesta, int brojStajacihMjesta, int brojBicikala, int brojKreveta,
                  int brojAutomobila, double nosivost, double povrsina, double zapremina, StatusVozila status) {
        this.voziloTip = VoziloFlyweightTvornica.getFlyweight(proizvodac, namjena, vrstaPogona);
        this.oznaka = oznaka;
        this.opis = opis;
        this.proizvodac = proizvodac;
        this.godina = godina;
        this.namjena = namjena;
        this.vrstaPrijevoza = vrstaPrijevoza;
        this.vrstaPogona = vrstaPogona;
        this.maksBrzina = maksBrzina;
        this.maksSnaga = maksSnaga;
        this.brojSjedecihMjesta = brojSjedecihMjesta;
        this.brojStajacihMjesta = brojStajacihMjesta;
        this.brojBicikala = brojBicikala;
        this.brojKreveta = brojKreveta;
        this.brojAutomobila = brojAutomobila;
        this.nosivost = nosivost;
        this.povrsina = povrsina;
        this.zapremina = zapremina;
        this.status = status;
    }

    public String getOznaka() {
        return oznaka;
    }

    public String getOpis() {
        return opis;
    }

    public String getProizvodac() {
        return voziloTip.getProizvodac();
    }

    public int getGodina() {
        return godina;
    }

    public NamjenaVozila getNamjena() {
        return voziloTip.getNamjenaVozila();
    }

    public VrstaPrijevozaVozila getVrstaPrijevoza() {
        return vrstaPrijevoza;
    }

    public VrstaPogonaVozila getVrstaPogona() {
        return voziloTip.getVrstaPogona();
    }

    public double getMaksBrzina() {
        return maksBrzina;
    }

    public double getMaksSnaga() {
        return maksSnaga;
    }

    public int getBrojSjedecihMjesta() {
        return brojSjedecihMjesta;
    }

    public int getBrojStajacihMjesta() {
        return brojStajacihMjesta;
    }

    public int getBrojBicikala() {
        return brojBicikala;
    }

    public int getBrojKreveta() {
        return brojKreveta;
    }

    public int getBrojAutomobila() {
        return brojAutomobila;
    }

    public double getNosivost() {
        return nosivost;
    }

    public double getPovrsina() {
        return povrsina;
    }

    public double getZapremina() {
        return zapremina;
    }

    public StatusVozila getStatus() {
        return status;
    }

    @Override
    public String dohvatiIdentifikator() {
        return oznaka;
    }
}
