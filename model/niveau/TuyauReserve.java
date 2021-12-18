package projetIG.model.niveau;

import projetIG.model.enumeration.CouleurTuyau;
import projetIG.model.enumeration.Rotation;
import projetIG.model.enumeration.TypeTuyau;

public class TuyauReserve extends Tuyau {
    protected int nombre = 0;  // Nombre de tuyaux disponibles dans la reserve

    public TuyauReserve(TypeTuyau typeTuyau, Rotation rotation) {
        super(typeTuyau, rotation);
        
        this.couleur.add(CouleurTuyau.BLANC);
        if(this.nom == TypeTuyau.OVER) this.couleur.add(CouleurTuyau.BLANC);
    }
    
    
    // METHODES
    public void augmenterNombre() {
        this.nombre = this.nombre + 1;
    }
    
    public void diminuerNombre() {
        this.nombre = this.nombre - 1;
    }

    
    // GETTERS
    public int getNombre() {
        return nombre;
    }
}
