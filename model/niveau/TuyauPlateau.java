package projetIG.model.niveau;

import java.util.ArrayList;
import projetIG.model.CouleurTuyau;
import projetIG.model.Orientation;
import projetIG.model.TypeTuyau;

public class TuyauPlateau extends Tuyau {
    protected int ligne;
    protected int colonne;
    protected boolean inamovible = false;
    protected ArrayList<ArrayList<Orientation>> orientations;
    
    public TuyauPlateau(String tuyau, int ligne, int colonne) {
        super(tuyau);
        
        this.ligne = ligne;
        this.colonne = colonne;
        
        if(tuyau.startsWith("*")){ this.inamovible = true; }
        
        this.orientations = Orientation.orientations(this.nom, this.rotation);
        
        if(this.nom == TypeTuyau.SOURCE) {
            this.inamovible = true;
            this.couleur = CouleurTuyau.appartient( tuyau.substring(0, 1) );
        }
        
        else{this.couleur = CouleurTuyau.BLANC;}
    }
    
    
    public TuyauPlateau(Tuyau tuyau, int ligne, int colonne) {
        super(tuyau);
        
        this.ligne = ligne;
        this.colonne = colonne;
        
        this.orientations = Orientation.orientations(this.nom, this.rotation);
    }
    
    
    // Renvoie vrai si l'orientation est trouvée parmi les orientations du tuyau
    public boolean aUneOuverture(Orientation orientation){
        for(ArrayList<Orientation> tableauOrientations : orientations){
            if(tableauOrientations.contains(orientation)) return true;
        }
        return false;
    }
    
    
    // Renvoie la liste des ouvertures (i.e. orientations) connectees, sans l'ouverture d'entree
    public ArrayList<Orientation> ouverturesConnectees(Orientation orientation){
        ArrayList<Orientation> ouverturesConnectees = new ArrayList<>();
        
        for(ArrayList<Orientation> tableauOrientations : orientations){
            if(tableauOrientations.contains(orientation)) {
                
                for(Orientation orientationSortie : tableauOrientations){
                    if(orientationSortie != orientation){
                        ouverturesConnectees.add(orientationSortie);
                    }
                }
                break;
            }
        }
        
        return ouverturesConnectees;
    }
    

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public boolean isInamovible() {
        return inamovible;
    }
    
    public ArrayList<ArrayList<Orientation>> getOrientations() {
        return orientations;
    }
}
