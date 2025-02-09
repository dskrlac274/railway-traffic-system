package modeli;

public class CijenaKonfiguracija {
    private double cijenaNormalni;
    private double cijenaUbrzani;
    private double cijenaBrzi;
    private double popustVikend;
    private double popustWeb;
    private double uvecanjeVlak;

    public CijenaKonfiguracija(double cijenaNormalni, double cijenaUbrzani, double cijenaBrzi,
                               double popustVikend, double popustWeb, double uvecanjeVlak) {
        this.cijenaNormalni = cijenaNormalni;
        this.cijenaUbrzani = cijenaUbrzani;
        this.cijenaBrzi = cijenaBrzi;
        this.popustVikend = popustVikend;
        this.popustWeb = popustWeb;
        this.uvecanjeVlak = uvecanjeVlak;
    }

    public double getCijenaNormalni() {
        return cijenaNormalni;
    }

    public void setCijenaNormalni(double cijenaNormalni) {
        this.cijenaNormalni = cijenaNormalni;
    }

    public double getCijenaUbrzani() {
        return cijenaUbrzani;
    }

    public void setCijenaUbrzani(double cijenaUbrzani) {
        this.cijenaUbrzani = cijenaUbrzani;
    }

    public double getCijenaBrzi() {
        return cijenaBrzi;
    }

    public void setCijenaBrzi(double cijenaBrzi) {
        this.cijenaBrzi = cijenaBrzi;
    }

    public double getPopustVikend() {
        return popustVikend;
    }

    public void setPopustVikend(double popustVikend) {
        this.popustVikend = popustVikend;
    }

    public double getPopustWeb() {
        return popustWeb;
    }

    public void setPopustWeb(double popustWeb) {
        this.popustWeb = popustWeb;
    }

    public double getUvecanjeVlak() {
        return uvecanjeVlak;
    }

    public void setUvecanjeVlak(double uvecanjeVlak) {
        this.uvecanjeVlak = uvecanjeVlak;
    }
}
