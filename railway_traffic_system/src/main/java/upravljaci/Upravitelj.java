package upravljaci;

public abstract class Upravitelj {
    private Upravitelj sljedeci;

    public Upravitelj postaviSljedeci(Upravitelj sljedeci) {
        this.sljedeci = sljedeci;
        return sljedeci;
    }

    public void obradi(String komanda) {
        if (!podudaraSe(komanda)) {
            if (sljedeci != null) {
                sljedeci.obradi(komanda);
            } else {
                System.out.println("Nepoznata komanda.");
            }
        } else {
            izvrsi(komanda);
        }
    }

    protected abstract boolean podudaraSe(String komanda);

    protected abstract void izvrsi(String komanda);
}
