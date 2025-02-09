package pomagaci;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class VrijemePomagac {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

    public static String izracunajVrijemeDolaska(String vrijemePolaska, String trajanjeVoznje) {
        LocalTime polazak = LocalTime.parse(vrijemePolaska, formatter);

        String[] trajanjeDijelovi = trajanjeVoznje.split(":");
        int sati = Integer.parseInt(trajanjeDijelovi[0]);
        int minute = Integer.parseInt(trajanjeDijelovi[1]);

        LocalTime dolazak = polazak.plusHours(sati).plusMinutes(minute);

        return dolazak.format(formatter);
    }

    public static LocalTime getVrijemeKaoTime(String vrijeme) {
        if (vrijeme == null || vrijeme.trim().isEmpty()) {
            return null;
        }
        return LocalTime.parse(vrijeme, formatter);
    }

    public static String getVrijemeKaoString(LocalTime vrijeme) {
        if (vrijeme == null) {
            return null;
        }
        return vrijeme.format(formatter);
    }

    public static LocalTime zbrojiVrijeme(LocalTime vrijeme, int sati, int minute) {
        return vrijeme.plusHours(sati).plusMinutes(minute);
    }

    public static LocalTime oduzmiVrijeme(LocalTime vrijeme, int sati, int minute) {
        return vrijeme.minusHours(sati).minusMinutes(minute);
    }

    public static LocalTime pretvoriMinuteUVrijeme(Integer minute) {
        if (minute == null) return LocalTime.ofSecondOfDay(0);
        int sati = minute / 60;
        int minutePreostale = minute % 60;
        return LocalTime.of(sati, minutePreostale);
    }
}
