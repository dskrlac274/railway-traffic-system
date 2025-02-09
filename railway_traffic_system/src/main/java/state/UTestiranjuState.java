package state;

import modeli.stanica.StatusPrugeStanice;

public class UTestiranjuState extends StatusPrugeState {
    @Override
    protected boolean dozvoljenPrijelaz(StatusPrugeState noviStatus) {
        return noviStatus instanceof IspravnaState;
    }

    @Override
    public StatusPrugeStanice getStatus() {
        return StatusPrugeStanice.U_TESTIRANJU;
    }
}
