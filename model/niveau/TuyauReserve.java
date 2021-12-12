package projetIG.model.niveau;

import projetIG.model.CouleurTuyau;

public class TuyauReserve extends Tuyau {
    protected int nombre = 0;  // Nombre de tuyaux disponibles dans la reserve

    public TuyauReserve(String tuyau) {
        super(tuyau);
    }

    public int getNombre() {
        return nombre;
    }

    public void augmenterNombre() {
        this.nombre = this.nombre + 1;
        this.couleur = CouleurTuyau.BLANC;
    }
    
    public void diminuerNombre() {
        this.nombre = this.nombre - 1;
        if(this.nombre < 1) { this.couleur = CouleurTuyau.NOIR; }
    }
}
