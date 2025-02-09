package state;

import modeli.stanica.StatusPrugeStanice;

public class ZatvorenaState extends StatusPrugeState {
    @Override
    protected boolean dozvoljenPrijelaz(StatusPrugeState noviStatus) {
        return noviStatus instanceof UTestiranjuState;
    }

    @Override
    public StatusPrugeStanice getStatus() {
        return StatusPrugeStanice.ZATVORENA;
    }
}
