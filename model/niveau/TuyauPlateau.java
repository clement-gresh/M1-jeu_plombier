package projetIG.model.niveau;

import java.util.ArrayList;
import projetIG.model.enumeration.Couleur;
import projetIG.model.enumeration.Dir;
import projetIG.model.enumeration.TypeTuyau;

public class TuyauPlateau extends Tuyau {
    private boolean fixe = false;
    private final ArrayList<Boolean> visite = new ArrayList<>();
    
    
    // CONSTRUCTEURS
    public TuyauPlateau(TypeTuyau typeTuyau, Dir rotation, boolean fixe, Couleur couleur) {
        super(typeTuyau, rotation);
        this.fixe = fixe;
        
        
        this.couleur.add(couleur);
        if(this.nom == TypeTuyau.OVER) this.couleur.add(couleur);
        
        this.visite.add(Boolean.FALSE);
        if(this.nom == TypeTuyau.OVER) this.visite.add(Boolean.FALSE);
    }
    
    
    public TuyauPlateau(Tuyau tuyau) {
        super(tuyau);
        
        this.couleur.add(Couleur.BLANC);
        if(tuyau.getNom() == TypeTuyau.OVER) this.couleur.add(Couleur.BLANC);
        
        this.visite.add(false);
        if(this.nom == TypeTuyau.OVER) this.visite.add(false);
    }
    
    
    
    // GETTERS
    public boolean isFixe() {
        return fixe;
    }

    public ArrayList<Boolean> getVisite() {
        return visite;
    }
    
    
    // SETTERS
    public void setVisite(int index, Boolean dejaVisite) {
        this.visite.set(index, dejaVisite);
    }
}
