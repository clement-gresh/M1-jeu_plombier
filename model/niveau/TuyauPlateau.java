package projetIG.model.niveau;

import java.util.ArrayList;
import projetIG.model.enumeration.CouleurTuyau;
import projetIG.model.enumeration.Ouverture;
import projetIG.model.enumeration.Rotation;
import projetIG.model.enumeration.TypeTuyau;

public class TuyauPlateau extends Tuyau {
    protected boolean inamovible = false;
    protected ArrayList<ArrayList<Ouverture>> ouvertures;
    protected ArrayList<Boolean> dejaVisite = new ArrayList<>();
    
    
    // CONSTRUCTEURS
    public TuyauPlateau(TypeTuyau typeTuyau, Rotation rotation, boolean inamovible, CouleurTuyau couleur) {
        super(typeTuyau, rotation);
        this.inamovible = inamovible;
        this.ouvertures = TypeTuyau.ouvertures.get(this.nom.ordinal());
        
        
        this.couleur.add(couleur);
        if(this.nom == TypeTuyau.OVER) this.couleur.add(couleur);
        
        this.dejaVisite.add(Boolean.FALSE);
        if(this.nom == TypeTuyau.OVER) this.dejaVisite.add(Boolean.FALSE);
    }
    
    
    public TuyauPlateau(Tuyau tuyau) {
        super(tuyau);
        
        this.ouvertures = TypeTuyau.ouvertures.get(this.nom.ordinal());
        
        this.couleur.add(CouleurTuyau.BLANC);
        if(tuyau.getNom() == TypeTuyau.OVER) this.couleur.add(CouleurTuyau.BLANC);
        
        this.dejaVisite.add(false);
        if(this.nom == TypeTuyau.OVER) this.dejaVisite.add(false);
    }
    
    
    // Renvoie vrai si l'orientation est trouvée parmi les orientations du tuyau
    public boolean aUneOuverture(Ouverture ouverture, Rotation rotation){
        for(ArrayList<Ouverture> tableauOuverture : ouvertures){
            if(tableauOuverture.contains(ouverture)) return true;
        }
        return false;
    }
    
    
    // METHODES
    // Renvoie la liste des ouvertures (i.e. orientations) connectees, sans l'ouverture d'entree
    public ArrayList<Ouverture> ouverturesConnectees(Ouverture ouverture, Rotation rotation){
        ArrayList<Ouverture> ouverturesConnectees = new ArrayList<>();
        
        for(ArrayList<Ouverture> tableauOuvertures : ouvertures){
            if(tableauOuvertures.contains(ouverture)) {
                
                for(Ouverture orientationSortie : tableauOuvertures){
                    if(orientationSortie != ouverture){
                        ouverturesConnectees.add(orientationSortie);
                    }
                }
                break;
            }
        }
        
        return ouverturesConnectees;
    }
    
    
    // GETTERS
    public boolean isInamovible() {
        return inamovible;
    }
    
    public ArrayList<ArrayList<Ouverture>> getOuvertures() {
        return ouvertures;
    }

    public ArrayList<Boolean> getDejaVisite() {
        return dejaVisite;
    }
    
    // SETTERS
    public void setDejaVisite(int index, Boolean dejaVisite) {
        this.dejaVisite.set(index, dejaVisite);
    }
}
