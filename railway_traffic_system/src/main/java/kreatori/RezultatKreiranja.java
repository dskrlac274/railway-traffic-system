package kreatori;

public class RezultatKreiranja<T> {
    private final T model;
    private final String greska;

    private RezultatKreiranja(T model, String greska) {
        this.model = model;
        this.greska = greska;
    }

    public static <T> RezultatKreiranja<T> uspjeh(T model) {
        return new RezultatKreiranja<>(model, null);
    }

    public static <T> RezultatKreiranja<T> neuspjeh(String greska) {
        return new RezultatKreiranja<>(null, greska);
    }

    public boolean jeUspjeh() {
        return model != null;
    }

    public T dohvatiModel() {
        return model;
    }

    public String dohvatiGresku() {
        return greska;
    }
}
