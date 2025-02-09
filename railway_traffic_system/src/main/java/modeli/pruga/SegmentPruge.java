package modeli.pruga;

import modeli.stanica.Stanica;
import state.*;

import java.util.List;

public class SegmentPruge {
    private final Stanica pocetna;
    private final Stanica zavrsna;
    private StatusPrugeState statusPruge;
    private String smjer;
    private final Pruga pruga;

    public SegmentPruge(Stanica pocetna, Stanica zavrsna, StatusPrugeState status, String smjer, Pruga pruga) {
        this.pocetna = pocetna;
        this.zavrsna = zavrsna;
        this.statusPruge = status;
        this.smjer = smjer;
        this.pruga = pruga;
    }

    public static StatusPrugeState odrediStanje(String noviStatusOpis) {
        StatusPrugeState noviStatusState;
        switch (noviStatusOpis) {
            case "I":
                noviStatusState = new IspravnaState();
                break;
            case "K":
                noviStatusState = new UKvaruState();
                break;
            case "T":
                noviStatusState = new UTestiranjuState();
                break;
            case "Z":
                noviStatusState = new ZatvorenaState();
                break;
            default:
                return new IspravnaState();
        }
        return noviStatusState;
    }

    public SegmentPruge dohvatiDrugiSegment() {
        var segmenti = this.dohvatiObrnutiSegment();

        for (SegmentPruge segment : segmenti) {
            if (segmentPoklapanje(segment, zavrsna, pocetna)) {
                return segment;
            }
        }

        return null;
    }

    public int dohvatiBrojKolosjeka(){
        return this.getSmjer().equals("N") ? zavrsna.getBrojKolosjeka(): pocetna.getBrojKolosjeka();
    }

    public void setStatusPruge(StatusPrugeState statusPruge) {
        this.statusPruge = statusPruge;
    }

    public Stanica getPocetna() {
        return pocetna;
    }

    public Stanica getZavrsna() {
        return zavrsna;
    }

    public StatusPrugeState getStatus() {
        return statusPruge;
    }

    public String getSmjer() {
        return smjer;
    }

    public void setSmjer(String smjer) {
        this.smjer = smjer;
    }

    public Pruga getPruga() {
        return pruga;
    }

    public boolean segmentPoklapanje(SegmentPruge segment, Stanica pocetna, Stanica zavrsna) {
        return segment.getPocetna().getNaziv().equals(pocetna.getNaziv())
                && segment.getZavrsna().getNaziv().equals(zavrsna.getNaziv());
    }

    private List<SegmentPruge> dohvatiObrnutiSegment() {
        return this.getSmjer().equals("N") ? pruga.segmentiPrugeO : pruga.segmentiPrugeN;
    }
}

