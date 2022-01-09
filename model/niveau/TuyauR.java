package projetIG.model.niveau;

import projetIG.model.enumeration.Couleur;
import projetIG.model.enumeration.Dir;
import projetIG.model.enumeration.TypeTuyau;

public class TuyauR extends Tuyau {
    private int nombre = 0;  // Nombre de tuyaux disponibles dans la reserve

    public TuyauR(TypeTuyau typeTuyau, Dir rotation) {
        super(typeTuyau, rotation);
        
        this.couleur.add(Couleur.BLANC);
        if(this.nom == TypeTuyau.OVER) this.couleur.add(Couleur.BLANC);
    }
    
    
    // METHODES
    public void augmenter() {
        this.nombre = this.nombre + 1;
    }
    
    public void diminuer() {
        this.nombre = this.nombre - 1;
    }

    
    // GETTERS
    public int getNombre() {
        return nombre;
    }
}
