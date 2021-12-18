package projetIG.model.niveau;

import java.util.ArrayList;
import projetIG.model.enumeration.CouleurTuyau;
import projetIG.model.enumeration.Rotation;
import projetIG.model.enumeration.TypeTuyau;

public class TuyauPlateau extends Tuyau {
    protected boolean inamovible = false;
    protected ArrayList<Boolean> dejaVisite = new ArrayList<>();
    
    
    // CONSTRUCTEURS
    public TuyauPlateau(TypeTuyau typeTuyau, Rotation rotation, boolean inamovible, CouleurTuyau couleur) {
        super(typeTuyau, rotation);
        this.inamovible = inamovible;
        
        
        this.couleur.add(couleur);
        if(this.nom == TypeTuyau.OVER) this.couleur.add(couleur);
        
        this.dejaVisite.add(Boolean.FALSE);
        if(this.nom == TypeTuyau.OVER) this.dejaVisite.add(Boolean.FALSE);
    }
    
    
    public TuyauPlateau(Tuyau tuyau) {
        super(tuyau);
        
        this.couleur.add(CouleurTuyau.BLANC);
        if(tuyau.getNom() == TypeTuyau.OVER) this.couleur.add(CouleurTuyau.BLANC);
        
        this.dejaVisite.add(false);
        if(this.nom == TypeTuyau.OVER) this.dejaVisite.add(false);
    }
    
    
    
    // GETTERS
    public boolean isInamovible() {
        return inamovible;
    }

    public ArrayList<Boolean> getDejaVisite() {
        return dejaVisite;
    }
    
    
    // SETTERS
    public void setDejaVisite(int index, Boolean dejaVisite) {
        this.dejaVisite.set(index, dejaVisite);
    }
}
