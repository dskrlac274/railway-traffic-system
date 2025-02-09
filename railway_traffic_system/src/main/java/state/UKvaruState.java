package state;

import modeli.stanica.StatusPrugeStanice;

public class UKvaruState extends StatusPrugeState {
    @Override
    protected boolean dozvoljenPrijelaz(StatusPrugeState noviStatus) {
        return noviStatus instanceof UTestiranjuState;
    }

    @Override
    public StatusPrugeStanice getStatus() {
        return StatusPrugeStanice.U_KVARU;
    }
}
