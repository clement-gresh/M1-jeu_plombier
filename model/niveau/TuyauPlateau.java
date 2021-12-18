package projetIG.model.niveau;

import java.util.ArrayList;
import java.util.Arrays;
import projetIG.model.enumeration.CouleurTuyau;
import projetIG.model.enumeration.Ouverture;
import projetIG.model.enumeration.Rotation;
import projetIG.model.enumeration.TypeTuyau;

public class TuyauPlateau extends Tuyau {
    protected boolean inamovible = false;
    protected boolean[][] ouvertures;
    protected ArrayList<Boolean> dejaVisite = new ArrayList<>();
    
    
    // CONSTRUCTEURS
    public TuyauPlateau(TypeTuyau typeTuyau, Rotation rotation, boolean inamovible, CouleurTuyau couleur) {
        super(typeTuyau, rotation);
        this.inamovible = inamovible;
        this.ouvertures = TypeTuyau.ouverturesBool[this.nom.ordinal()];
        
        
        this.couleur.add(couleur);
        if(this.nom == TypeTuyau.OVER) this.couleur.add(couleur);
        
        this.dejaVisite.add(Boolean.FALSE);
        if(this.nom == TypeTuyau.OVER) this.dejaVisite.add(Boolean.FALSE);
    }
    
    
    public TuyauPlateau(Tuyau tuyau) {
        super(tuyau);
        
        this.ouvertures = TypeTuyau.ouverturesBool[this.nom.ordinal()];
        
        this.couleur.add(CouleurTuyau.BLANC);
        if(tuyau.getNom() == TypeTuyau.OVER) this.couleur.add(CouleurTuyau.BLANC);
        
        this.dejaVisite.add(false);
        if(this.nom == TypeTuyau.OVER) this.dejaVisite.add(false);
    }
    
    
    // Renvoie vrai si l'orientation est trouvée parmi les orientations du tuyau
    public boolean aUneOuverture(Ouverture ouvertureTuyau, Rotation rotation){
        int nbrOuvertures = Ouverture.values().length;
        
        // Utilise la formule (a % b + b) % b pour obtenir un modulo toujours positif
        int ouvertureModel = ((ouvertureTuyau.ordinal() - rotation.ordinal()) 
                % nbrOuvertures + nbrOuvertures) % nbrOuvertures;
        
        for(boolean[] tableauOrientations : ouvertures){
            if( tableauOrientations[ouvertureModel] == true ) return true;
        }
        return false;
    }
    
    /*
    // METHODES
    // Renvoie la liste des ouvertures (i.e. orientations) connectees, sans l'ouverture d'entree
    public ArrayList<Ouverture> ouverturesConnectees(Ouverture orientation){
        ArrayList<Ouverture> ouverturesConnectees = new ArrayList<>();
        
        for(ArrayList<Ouverture> tableauOrientations : ouvertures){
            if(tableauOrientations.contains(orientation)) {
                
                for(Ouverture orientationSortie : tableauOrientations){
                    if(orientationSortie != orientation){
                        ouverturesConnectees.add(orientationSortie);
                    }
                }
                break;
            }
        }
        
        return ouverturesConnectees;
    }
    */
    
    // GETTERS
    public boolean isInamovible() {
        return inamovible;
    }
    
    public boolean[][] getOuvertures() {
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
