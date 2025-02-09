package state;

import modeli.pruga.SegmentPruge;
import modeli.stanica.StatusPrugeStanice;

public abstract class StatusPrugeState {
    public boolean evidentiraj(SegmentPruge segmentPruge, StatusPrugeState noviStatus) {
        if (!dozvoljenPrijelaz(noviStatus)) {
            return false;
        }

        segmentPruge.setStatusPruge(noviStatus);

        int brojKolosjeka = segmentPruge.dohvatiBrojKolosjeka();

        SegmentPruge obrnutiSegment = segmentPruge.dohvatiDrugiSegment();

        if (obrnutiSegment == null) return false;

        if (brojKolosjeka == 1) {
            obrnutiSegment.setStatusPruge(noviStatus);
        }

        return true;
    }

    protected abstract boolean dozvoljenPrijelaz(StatusPrugeState noviStatus);

    public abstract StatusPrugeStanice getStatus();
}
