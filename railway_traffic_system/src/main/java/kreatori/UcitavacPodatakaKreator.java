package kreatori;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import jezgra.Zeljeznica;
import pomagaci.GreskaIspis;

public abstract class UcitavacPodatakaKreator<T extends Zeljeznica> {
    public void ucitajPodatke(String putanjaDatoteke) {
        int redniBroj = 2;
        String zadnjaLinija = null;

        try (BufferedReader br = new BufferedReader(new FileReader(putanjaDatoteke))) {
            br.readLine();

            String linija;
            while ((linija = br.readLine()) != null) {
                if (linija.trim().isEmpty() || linija.matches("^;+") || linija.startsWith("#")) {
                    redniBroj++;
                    continue;
                }

                String[] polja = linija.split(";");

                String greska = validirajVrijednostiRetka(polja);
                if (greska != null) {
                    GreskaIspis.ispisiGresku(redniBroj, linija, greska);
                } else {
                    RezultatKreiranja<T> rezultat = kreirajModel(polja);
                    if (rezultat.jeUspjeh()) {
                        spremiModel(rezultat.dohvatiModel());
                    } else {
                        GreskaIspis.ispisiGresku(redniBroj, linija, rezultat.dohvatiGresku());
                    }
                }

                redniBroj++;
                zadnjaLinija = linija;
            }

            ispisiZavrsneGreske(redniBroj - 1, zadnjaLinija);
        } catch (IOException e) {
            System.out.println("Ne mogu učitati datoteku: " + putanjaDatoteke + " - " + e.getMessage());
        }
    }

    private void ispisiZavrsneGreske(int redniBroj, String zadnjaLinija) {
        String zavrsnaGreska = zavrsnaObrada();
        if (zavrsnaGreska != null) {
            GreskaIspis.ispisiGresku(redniBroj, zadnjaLinija, zavrsnaGreska);
        }
    }

    protected abstract void spremiModel(T model);

    protected String zavrsnaObrada() {
        return null;
    }

    protected abstract String validirajVrijednostiRetka(String[] polja);

    protected abstract RezultatKreiranja<T> kreirajModel(String[] polja);
}
