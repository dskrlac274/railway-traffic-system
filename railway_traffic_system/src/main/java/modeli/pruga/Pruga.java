package modeli.pruga;

import modeli.stanica.Stanica;
import state.*;

import java.util.ArrayList;
import java.util.List;

import static modeli.vozni_red.VrstaVlakaVozniRed.*;

public class Pruga {
    private final String oznaka;
    private final String pocetnaStanica;
    private final String zavrsnaStanica;
    private final double duljina;
    private int ukupnoTrajanjeNormalni;
    private int ukupnoTrajanjeUbrzani;
    private int ukupnoTrajanjeBrzi;
    private List<Stanica> stanice;

    public List<SegmentPruge> segmentiPrugeN = new ArrayList<>();
    public List<SegmentPruge> segmentiPrugeO = new ArrayList<>();

    public Pruga(String oznaka, String pocetnaStanica, String zavrsnaStanica, double duljina,
                 int ukupnoTrajanjeNormalni, int ukupnoTrajanjeUbrzani, int ukupnoTrajanjeBrzi) {
        this.oznaka = oznaka;
        this.pocetnaStanica = pocetnaStanica;
        this.zavrsnaStanica = zavrsnaStanica;
        this.duljina = duljina;
        this.ukupnoTrajanjeNormalni = ukupnoTrajanjeNormalni;
        this.ukupnoTrajanjeUbrzani = ukupnoTrajanjeUbrzani;
        this.ukupnoTrajanjeBrzi = ukupnoTrajanjeBrzi;
    }

    public Pruga(String oznaka, String pocetnaStanica, String zavrsnaStanica, double duljina, List<Stanica> stanice) {
        this.oznaka = oznaka;
        this.pocetnaStanica = pocetnaStanica;
        this.zavrsnaStanica = zavrsnaStanica;
        this.duljina = duljina;
        this.stanice = stanice;
    }

    private List<SegmentPruge> dohvatiNormalneSegmente() {
        if (stanice == null || stanice.size() < 2) {
            return segmentiPrugeN;
        }

        for (int i = 0; i < stanice.size() - 1; i++) {
            Stanica pocetna = stanice.get(i);
            Stanica zavrsna = stanice.get(i + 1);

            if (pocetna.getNaziv().equals(zavrsna.getNaziv())) continue;

            StatusPrugeState statusPrugeState;
            var pocetniStatus = zavrsna.getStatusPruge();
            switch (pocetniStatus) {
                case U_KVARU -> statusPrugeState = new UKvaruState();
                case ZATVORENA -> statusPrugeState = new ZatvorenaState();
                case U_TESTIRANJU -> statusPrugeState = new UTestiranjuState();
                default -> statusPrugeState = new IspravnaState();
            }

            segmentiPrugeN.add(new SegmentPruge(pocetna, zavrsna, statusPrugeState, "N", this));
        }
        return segmentiPrugeN;
    }

    public void evidentirajStatuseRelacija() {
        List<SegmentPruge> normalniSegmenti = dohvatiNormalneSegmente();
        List<SegmentPruge> obrnutiKopije = new ArrayList<>();

        for (int i = normalniSegmenti.size() - 1; i >= 0; i--) {
            SegmentPruge original = normalniSegmenti.get(i);

            Stanica novaPocetna = original.getZavrsna();
            Stanica novaZavrsna = original.getPocetna();

            StatusPrugeState odabranoStanje = original.getStatus();

            SegmentPruge kopija = new SegmentPruge(
                    novaPocetna,
                    novaZavrsna,
                    odabranoStanje,
                    "O",
                    this
            );

            obrnutiKopije.add(kopija);
        }

        this.segmentiPrugeO = obrnutiKopije;
    }

    public String getOznaka() {
        return oznaka;
    }

    public String getPocetnaStanica() {
        return pocetnaStanica;
    }

    public String getZavrsnaStanica() {
        return zavrsnaStanica;
    }

    public double getDuljina() {
        return duljina;
    }

    public List<Stanica> getStanice() {
        return stanice;
    }

    public void setStanice(List<Stanica> stanice) {
        this.stanice = stanice;
    }

    public int getUkupnoTrajanjeNormalni() {
        return ukupnoTrajanjeNormalni;
    }

    public int getUkupnoTrajanjeUbrzani() {
        return ukupnoTrajanjeUbrzani;
    }

    public int getUkupnoTrajanjeBrzi() {
        return ukupnoTrajanjeBrzi;
    }

    public int getUKupnoTrajanje(String vrstaVlaka) {
        if (vrstaVlaka.equals(NORMALNI.getOpis()))
            return this.getUkupnoTrajanjeNormalni();
        else if (vrstaVlaka.equals(UBRZANI.getOpis()))
            return this.getUkupnoTrajanjeUbrzani();
        else if (vrstaVlaka.equals(BRZI.getOpis()))
            return this.getUkupnoTrajanjeBrzi();
        else return 0;
    }
}
