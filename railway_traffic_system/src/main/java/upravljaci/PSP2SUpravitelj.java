package upravljaci;

import jezgra.ZeljeznickiPromet;
import modeli.pruga.Pruga;
import modeli.pruga.SegmentPruge;
import modeli.stanica.Stanica;
import pomagaci.TablicaPomagac;
import state.StatusPrugeState;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PSP2SUpravitelj extends Upravitelj {
    private static final Pattern PSP2S_PREDLOZAK = Pattern.compile(
            "^PSP2S\\s+(?<oznaka>[^-]+)\\s+-\\s+(?<polazna>.+)\\s+-\\s+(?<odredisna>.+)\\s+-\\s+(?<status>[IKTZ])$"
    );

    private final ZeljeznickiPromet zeljeznickiPromet;

    public PSP2SUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return PSP2S_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher matcher = PSP2S_PREDLOZAK.matcher(komanda);
        if (!matcher.matches()) {
            System.out.println("Neispravna komanda.");
            return;
        }

        String oznakaPruge = matcher.group("oznaka");
        String polaznaStanica = matcher.group("polazna");
        String odredisnaStanica = matcher.group("odredisna");
        String status = matcher.group("status");

        Pruga pruga = zeljeznickiPromet.getPrugaPoOznaci(oznakaPruge);
        if (pruga == null) {
            System.out.println("Pruga s oznakom " + oznakaPruge + " nije pronađena.");
            return;
        }

        Stanica polazna = pruga.getStanice().stream()
                .filter(stanica -> stanica.getNaziv().equalsIgnoreCase(polaznaStanica))
                .findFirst()
                .orElse(null);

        Stanica odredisna = pruga.getStanice().stream()
                .filter(stanica -> stanica.getNaziv().equalsIgnoreCase(odredisnaStanica))
                .findFirst()
                .orElse(null);

        if (polazna == null || odredisna == null) {
            System.out.println("Stanice " + polaznaStanica + " ili " + odredisnaStanica +
                    " nisu pronađene na pruzi " + oznakaPruge);
            return;
        }

        if (polazna.getNaziv().equals(odredisna.getNaziv())) {
            System.out.println("Status relacije između stanica " + polaznaStanica + " i " +
                    odredisnaStanica + " na pruzi " + oznakaPruge +
                    " nije moguće postaviti na " + status);
            return;
        }

        List<SegmentPruge> segmenti;
        if (polazna.getId() < odredisna.getId()) {
            segmenti = pruga.segmentiPrugeN;
        } else {
            segmenti = pruga.segmentiPrugeO;
        }

        int startIdx = -1, endIdx = -1;
        for (int i = 0; i < segmenti.size(); i++) {
            SegmentPruge seg = segmenti.get(i);
            if (seg.getPocetna().getNaziv().equalsIgnoreCase(polaznaStanica)) {
                startIdx = i;
            }
            if (seg.getZavrsna().getNaziv().equalsIgnoreCase(odredisnaStanica)) {
                endIdx = i;
            }
        }

        if (startIdx == -1 || endIdx == -1) {
            System.out.println("Nije moguće pronaći segmente za zadanu relaciju.");
            return;
        }

        List<String> zaglavlje = List.of("Polazna", "Odredišna", "Status", "Smjer");
        var tablica = new ArrayList<List<String>>();

        List<SegmentPruge> obradjeni = new ArrayList<>();
        List<StatusPrugeState> stariSts = new ArrayList<>();

        boolean allOk = true;
        for (int i = startIdx; i <= endIdx; i++) {
            SegmentPruge seg = segmenti.get(i);

            StatusPrugeState stariForward = seg.getStatus();


            StatusPrugeState zeljeniStatus = SegmentPruge.odrediStanje(status);

            boolean promjena = stariForward.evidentiraj(seg, zeljeniStatus);

            if (!promjena) {
                System.out.printf(
                        "Status relacije %s - %s nije moguće promijeniti na %s | Smjer: %s%n",
                        seg.getPocetna().getNaziv(),
                        seg.getZavrsna().getNaziv(),
                        status,
                        seg.getSmjer()
                );
                allOk = false;
                break;
            } else {
                obradjeni.add(seg);
                stariSts.add(stariForward);

                tablica.add(List.of(
                        seg.getPocetna().getNaziv(),
                        seg.getZavrsna().getNaziv(),
                        status,
                        seg.getSmjer()
                ));
            }
        }

        if (!allOk) {
            for (int j = 0; j < obradjeni.size(); j++) {
                SegmentPruge segRevert = obradjeni.get(j);
                StatusPrugeState oldSt = stariSts.get(j);

                segRevert.setStatusPruge(oldSt);
            }
        } else {
            if (!tablica.isEmpty()) {
                TablicaPomagac.prikaziTablicu(zaglavlje, tablica);
            }
        }
    }
}
