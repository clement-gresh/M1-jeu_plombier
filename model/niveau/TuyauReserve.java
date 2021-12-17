package projetIG.model.niveau;

import projetIG.model.enumeration.CouleurTuyau;

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
        
        for(int i = 0; i < this.couleur.size(); i++){
            this.couleur.set(i, CouleurTuyau.BLANC);
        }
    }
    
    public void diminuerNombre() {
        this.nombre = this.nombre - 1;
        
        if(this.nombre < 1) {
            for(int i = 0; i < this.couleur.size(); i++){
                this.couleur.set(i, CouleurTuyau.NOIR);
            }
        }
    }
}
