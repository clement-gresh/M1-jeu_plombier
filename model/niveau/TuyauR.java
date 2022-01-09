package projetIG.model.niveau;

import static projetIG.model.enumeration.Couleur.BLANC;
import projetIG.model.enumeration.Dir;
import projetIG.model.enumeration.TypeTuyau;
import static projetIG.model.enumeration.TypeTuyau.OVER;

public class TuyauR extends Tuyau {
    private int nombre = 0;  // Nombre de tuyaux disponibles dans la reserve

    public TuyauR(TypeTuyau type, Dir rotation) {
        super(type, rotation);
        this.couleur.add(BLANC);
        if(this.type == OVER) this.couleur.add(BLANC);
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
