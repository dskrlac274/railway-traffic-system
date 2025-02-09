package upravljaci;

import jezgra.ZeljeznickiPromet;
import memento.KupovinaKarteMemento;
import memento.KupovinaKarteOriginator;
import modeli.KupovinaKarte;
import pomagaci.TablicaPomagac;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IKKPVUpravitelj extends Upravitelj {
    private static final Pattern IKKPV_PREDLOZAK = Pattern.compile(
            "^IKKPV(?:\\s+(?<index>\\d+))?$"
    );

    private static final List<String> ZAGLAVLJE_KARTE = List.of(
            "Rbr",
            "Vlak",
            "Relacija",
            "Datum",
            "Vrijeme polaska",
            "Vrijeme dolaska",
            "Izvorna cijena",
            "Popust (€)",
            "Konačna cijena",
            "Način kupnje",
            "Vrijeme kupnje karte"
    );

    private final ZeljeznickiPromet zeljeznickiPromet;

    public IKKPVUpravitelj(ZeljeznickiPromet zeljeznickiPromet) {
        this.zeljeznickiPromet = zeljeznickiPromet;
    }

    @Override
    protected boolean podudaraSe(String komanda) {
        return IKKPV_PREDLOZAK.matcher(komanda).matches();
    }

    @Override
    protected void izvrsi(String komanda) {
        Matcher m = IKKPV_PREDLOZAK.matcher(komanda);
        if (!m.matches()) {
            System.out.println("Neispravna komanda.");
            return;
        }

        String indexGroup = m.group("index");
        if (indexGroup == null) {
            ispisiSveKarte();
        } else {
            int brojKarte;
            try {
                brojKarte = Integer.parseInt(indexGroup);
            } catch (NumberFormatException e) {
                System.out.println("Neispravan broj za index karte.");
                return;
            }
            ispisiJednuKartu(brojKarte);
        }
    }

    private void ispisiSveKarte() {
        var caretaker = zeljeznickiPromet.getCaretakerKupnja();
        int brojMementa = caretaker.getBrojMementa();

        if (brojMementa == 0) {
            System.out.println("Nema kupljenih karata u evidenciji.");
            return;
        }

        var tablica = new ArrayList<List<String>>();
        int rbr = 1;

        for (int i = 0; i < brojMementa; i++) {
            KupovinaKarteMemento memento = caretaker.getMemento(i);
            if (memento == null) {
                break;
            }
            var kupnja = memento.getStanjeKupnje();
            tablica.add(kreirajRedak(kupnja, rbr));
            rbr++;
        }

        TablicaPomagac.prikaziTablicu(ZAGLAVLJE_KARTE, tablica);
    }


    private void ispisiJednuKartu(int redniBroj) {
        var caretaker = zeljeznickiPromet.getCaretakerKupnja();
        var memento = caretaker.getMemento(redniBroj-1);

        if (memento == null) {
            System.out.println("Karta sa nevedenim rednim brojem ne postoji.");
            return;
        }

        KupovinaKarteOriginator kupovinaKarteOriginator = new KupovinaKarteOriginator();
        kupovinaKarteOriginator.restore(memento);
        KupovinaKarte kupnja = kupovinaKarteOriginator.getStanje();

        var tablica = new ArrayList<List<String>>();
        tablica.add(kreirajRedak(kupnja, redniBroj));

        TablicaPomagac.prikaziTablicu(ZAGLAVLJE_KARTE, tablica);
    }

    private List<String> kreirajRedak(KupovinaKarte kupnja, int rbr) {
        DateTimeFormatter formatterPutovanje = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        String kupnjaFormatted = kupnja.getDatumPutovanja().format(formatterPutovanje);
        var dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        String vrijemeKupnje = kupnja.getDatumVrijemeKupovine().format(dtf);

        return List.of(
                String.valueOf(rbr),
                kupnja.getOznakaVlaka(),
                kupnja.getPolaznaStanica() + "-" + kupnja.getOdredisnaStanica(),
                kupnjaFormatted,
                (kupnja.getVrijemePolaska() != null) ? kupnja.getVrijemePolaska().toString() : "",
                (kupnja.getVrijemeDolaska() != null) ? kupnja.getVrijemeDolaska().toString() : "",
                String.format("%.2f €", kupnja.getIzvornaCijena()),
                String.format("%.2f €", kupnja.getPopustiUkupno()),
                String.format("%.2f €", kupnja.getKonacnaCijena()),
                kupnja.getNacinKupovine(),
                vrijemeKupnje
        );
    }
}
