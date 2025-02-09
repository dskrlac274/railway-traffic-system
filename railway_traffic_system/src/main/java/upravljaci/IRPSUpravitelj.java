package upravljaci;

import jezgra.ZeljeznickiPromet;
import modeli.pruga.Pruga;
import modeli.pruga.SegmentPruge;
import pomagaci.TablicaPomagac;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IRPSUpravitelj extends Upravitelj {
    private static final Pattern IRPS_PREDLOZAK = Pattern.compile(
            "^IRPS\\s+(?<status>[IKTZ])(?:\\s+(?<oznaka>\\S+))?$"
    );

    private static final List<String> ZAGLAVLJE_RELACIJA = List.of(
            "Pruge oznaka",
            "Polazna stanica",
            "Odredišna stanica",
            "Status pruge",
            "Smjer"
    );

    private final ZeljeznickiPromet zeljeznickiPromet;

    public IRPSUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return IRPS_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher m = IRPS_PREDLOZAK.matcher(komanda);
        if (!m.matches()) {
            System.out.println("Neispravna komanda.");
            return;
        }

        String status = m.group("status");
        String oznakaPruge = m.group("oznaka");

        if (oznakaPruge == null) {
            ispisiSveRelacije(status);
        } else {
            ispisiRelacijeZaPruge(status, oznakaPruge);
        }
    }

    private void ispisiRelacijeZaPruge(String trazeniStatus, String oznakaPruge) {
        var pruge = zeljeznickiPromet.getPruge();
        var tablica = new ArrayList<List<String>>();

        pruge.stream()
                .filter(pruga -> pruga.getOznaka().equals(oznakaPruge))
                .forEach(pruga -> {
                    for (SegmentPruge seg : pruga.segmentiPrugeN) {
                        if (seg.getStatus().getStatus().getOpis().equals(trazeniStatus)) {
                            tablica.add(List.of(
                                    pruga.getOznaka(),
                                    seg.getPocetna().getNaziv(),
                                    seg.getZavrsna().getNaziv(),
                                    trazeniStatus,
                                    seg.getSmjer()
                            ));
                        }
                    }

                    for (SegmentPruge seg : pruga.segmentiPrugeO) {
                        if (seg.getStatus().getStatus().getOpis().equals(trazeniStatus)) {
                            tablica.add(List.of(
                                    pruga.getOznaka(),
                                    seg.getPocetna().getNaziv(),
                                    seg.getZavrsna().getNaziv(),
                                    trazeniStatus,
                                    seg.getSmjer()
                            ));
                        }
                    }
                });

        if (tablica.isEmpty()) {
            System.out.println("Nema relacija sa statusom " + trazeniStatus +
                    " na pruzi " + oznakaPruge + ".");
        } else {
            TablicaPomagac.prikaziTablicu(ZAGLAVLJE_RELACIJA, tablica);
        }
    }

    private void ispisiSveRelacije(String trazeniStatus) {
        var pruge = zeljeznickiPromet.getPruge();
        var tablica = new ArrayList<List<String>>();

        for (Pruga pruga : pruge) {
            for (SegmentPruge seg : pruga.segmentiPrugeN) {
                if (seg.getStatus().getStatus().getOpis().equals(trazeniStatus)) {
                    tablica.add(List.of(
                            pruga.getOznaka(),
                            seg.getPocetna().getNaziv(),
                            seg.getZavrsna().getNaziv(),
                            trazeniStatus,
                            seg.getSmjer()
                    ));
                }
            }

            for (SegmentPruge seg : pruga.segmentiPrugeO) {
                if (seg.getStatus().getStatus().getOpis().equals(trazeniStatus)) {
                    tablica.add(List.of(
                            pruga.getOznaka(),
                            seg.getPocetna().getNaziv(),
                            seg.getZavrsna().getNaziv(),
                            trazeniStatus,
                            seg.getSmjer()
                    ));
                }
            }
        }

        if (tablica.isEmpty()) {
            System.out.println("Nema relacija sa statusom " + trazeniStatus + " na svim prugama.");
        } else {
            TablicaPomagac.prikaziTablicu(ZAGLAVLJE_RELACIJA, tablica);
        }
    }
}
