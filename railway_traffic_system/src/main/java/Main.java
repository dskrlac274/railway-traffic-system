import kreatori.*;
import jezgra.ZeljeznickiPromet;
import jezgra.ArgumentParser;
import mediator.UpraviteljObavijesti;
import upravljaci.*;

import java.util.*;

public class Main {
    public static void main(String[] argumenti) {
        ArgumentParser parser = new ArgumentParser();

        if (!parser.parsiraj(argumenti) || !validirajArgumente(parser, argumenti)) {
            ispisNedostatnihDatoteka();
            return;
        }

        ZeljeznickiPromet zeljeznickiPromet = inicijalizirajZeljeznickiPromet(parser);
        interaktivniNacinRada(zeljeznickiPromet);
    }

    private static boolean validirajArgumente(ArgumentParser parser, String[] args) {
        return parser.getStaniceDatoteka() != null && parser.getVozilaDatoteka() != null &&
                parser.getKompozicijeDatoteka() != null && parser.getVozniRedDatoteka() != null &&
                parser.getOznakeDanaDatoteka() != null;
    }

    private static void ispisNedostatnihDatoteka() {
        System.out.println("Navedena ulazna komanda nije ispravna. " +
                "Koristite argumente: --zs <datoteka_stanica> --zps <datoteka_vozila> --zk <datoteka_kompozicija> " +
                "--zvr <datoteka_vozni_red> --zod <datoteka_oznake_dana>");
    }

    private static ZeljeznickiPromet inicijalizirajZeljeznickiPromet(ArgumentParser parser) {
        StanicaKreator stanicaKreator = new StanicaKreator();
        PrugaKreator prugaKreator = new PrugaKreator();
        VoziloKreator voziloKreator = new VoziloKreator();
        KompozicijaKreator kompozicijaKreator = new KompozicijaKreator();
        OznakaDanaKreator oznakaDanaKreator = new OznakaDanaKreator();
        VozniRedKreator vozniRedKreator = new VozniRedKreator();

        ZeljeznickiPromet zeljeznickiPromet = ZeljeznickiPromet.dohvatiInstancu();

        stanicaKreator.ucitajPodatke(parser.getStaniceDatoteka());
        voziloKreator.ucitajPodatke(parser.getVozilaDatoteka());
        kompozicijaKreator.ucitajPodatke(parser.getKompozicijeDatoteka());
        prugaKreator.kreirajPruge(zeljeznickiPromet.getStaniceFlat());
        oznakaDanaKreator.ucitajPodatke(parser.getOznakeDanaDatoteka());
        vozniRedKreator.ucitajPodatke(parser.getVozniRedDatoteka());

        return zeljeznickiPromet;
    }

    private static void interaktivniNacinRada(ZeljeznickiPromet zeljeznickiPromet) {
        IPUpravitelj ipUpravitelj = new IPUpravitelj(zeljeznickiPromet);
        ISPUpravitelj ispUpravitelj = new ISPUpravitelj(zeljeznickiPromet);
        IKUpravitelj ikUpravitelj = new IKUpravitelj(zeljeznickiPromet);
        ISI2SUpravitelj isi2sUpravitelj = new ISI2SUpravitelj(zeljeznickiPromet);
        IVUpravitelj ivUpravitelj = new IVUpravitelj(zeljeznickiPromet);
        IEVUpravitelj ievUpravitelj = new IEVUpravitelj(zeljeznickiPromet);
        IEVDUpravitelj ievdUpravitelj = new IEVDUpravitelj(zeljeznickiPromet);
        IVRVUpravitelj ivrvUpravitelj = new IVRVUpravitelj(zeljeznickiPromet);
        IVI2SUpravitelj ivi2SUpravitelj = new IVI2SUpravitelj(zeljeznickiPromet);
        DKUpravitelj dkUpravitelj = new DKUpravitelj(zeljeznickiPromet);
        PKUpravitelj pkUpravitelj = new PKUpravitelj(zeljeznickiPromet);
        DPKUpravitelj dpkUpravitelj = new DPKUpravitelj(zeljeznickiPromet);
        SVVUpravitelj svvUpravitelj = new SVVUpravitelj(zeljeznickiPromet);
        var upraviteljObavijest = new UpraviteljObavijesti();
        POUpravitelj poUpravitelj = new POUpravitelj(upraviteljObavijest);
        CVPUpravitelj cvpUpravitelj = new CVPUpravitelj(zeljeznickiPromet);
        KKPV2SUpravitelj kkpv2SUpravitelj = new KKPV2SUpravitelj(zeljeznickiPromet);
        IKKPVUpravitelj ikkpvUpravitelj = new IKKPVUpravitelj(zeljeznickiPromet);
        UKP2SUpravitelj ukp2SUpravitelj = new UKP2SUpravitelj(zeljeznickiPromet);
        PSP2SUpravitelj psp2SUpravitelj = new PSP2SUpravitelj(zeljeznickiPromet);
        IRPSUpravitelj irpsUpravitelj = new IRPSUpravitelj(zeljeznickiPromet);
        UNDOKKUpravitelj undokkUpravitelj = new UNDOKKUpravitelj(zeljeznickiPromet);

        ipUpravitelj.postaviSljedeci(ispUpravitelj)
                .postaviSljedeci(ikUpravitelj)
                .postaviSljedeci(isi2sUpravitelj)
                .postaviSljedeci(ivUpravitelj)
                .postaviSljedeci(ievUpravitelj)
                .postaviSljedeci(ievdUpravitelj)
                .postaviSljedeci(ivrvUpravitelj)
                .postaviSljedeci(ivi2SUpravitelj)
                .postaviSljedeci(dkUpravitelj)
                .postaviSljedeci(pkUpravitelj)
                .postaviSljedeci(dpkUpravitelj)
                .postaviSljedeci(svvUpravitelj)
                .postaviSljedeci(poUpravitelj)
                .postaviSljedeci(cvpUpravitelj)
                .postaviSljedeci(kkpv2SUpravitelj)
                .postaviSljedeci(ikkpvUpravitelj)
                .postaviSljedeci(ukp2SUpravitelj)
                .postaviSljedeci(psp2SUpravitelj)
                .postaviSljedeci(irpsUpravitelj)
                .postaviSljedeci(undokkUpravitelj);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Interaktivni način rada. Unesite komande (Q za izlaz).");
        while (true) {
            System.out.print("> ");
            String komanda = scanner.nextLine().trim();

            if (komanda.equals("Q")) {
                prekiniProgram();
                break;
            }

            ipUpravitelj.obradi(komanda);

        }
        scanner.close();
    }

    private static void prekiniProgram() {
        System.out.println("Izlazak iz programa.");
    }
}
