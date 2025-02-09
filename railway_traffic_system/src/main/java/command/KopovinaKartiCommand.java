package command;

public class KopovinaKartiCommand implements KarteCommand {
    private final KarteReceiver prijemnik;
    private final String komanda;

    public KopovinaKartiCommand(KarteReceiver prijemnik, String komanda) {
        this.prijemnik = prijemnik;
        this.komanda = komanda;
    }

    @Override
    public void izvrsi() {
        prijemnik.kupiKartu(komanda);
    }

    @Override
    public void ponisti(int i) {
        prijemnik.ponistiKupnju(i);
    }
}
