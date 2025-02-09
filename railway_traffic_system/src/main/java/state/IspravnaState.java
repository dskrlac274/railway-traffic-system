package state;

import modeli.stanica.StatusPrugeStanice;

public class IspravnaState extends StatusPrugeState {
    @Override
    protected boolean dozvoljenPrijelaz(StatusPrugeState noviStatus) {
        return (noviStatus instanceof ZatvorenaState ||
                noviStatus instanceof UKvaruState ||
                noviStatus instanceof UTestiranjuState);
    }

    @Override
    public StatusPrugeStanice getStatus() {
        return StatusPrugeStanice.ISPRAVNA;
    }
}
