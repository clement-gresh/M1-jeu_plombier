package projetIG.model.niveau;

import java.util.ArrayList;
import projetIG.model.enumeration.CouleurTuyau;
import projetIG.model.enumeration.Ouverture;
import projetIG.model.enumeration.TypeTuyau;

public class TuyauPlateau extends Tuyau {
    protected int ligne;
    protected int colonne;
    protected boolean inamovible = false;
    protected ArrayList<ArrayList<Ouverture>> ouvertures;
    protected ArrayList<Boolean> dejaVisite = new ArrayList<>();
    
    public TuyauPlateau(String tuyau, int ligne, int colonne) {
        super(tuyau);
        
        this.ligne = ligne;
        this.colonne = colonne;
        
        if(tuyau.startsWith("*")){ this.inamovible = true; }
        
        this.ouvertures = Ouverture.ouvertures(this.nom, this.rotation);
        
        if(this.nom == TypeTuyau.SOURCE) {
            this.inamovible = true;
            this.couleur.set(0, CouleurTuyau.appartient( tuyau.substring(0, 1) ));
        }
        
        this.dejaVisite.add(Boolean.FALSE);
        if(this.nom == TypeTuyau.OVER) this.dejaVisite.add(Boolean.FALSE);
    }
    
    
    public TuyauPlateau(Tuyau tuyau, int ligne, int colonne) {
        super(tuyau);
        
        this.ligne = ligne;
        this.colonne = colonne;
        
        this.ouvertures = Ouverture.ouvertures(this.nom, this.rotation);
        
        this.dejaVisite.add(false);
        if(this.nom == TypeTuyau.OVER) this.dejaVisite.add(false);
    }
    
    
    // Renvoie vrai si l'orientation est trouvée parmi les orientations du tuyau
    public boolean aUneOuverture(Ouverture orientation){
        for(ArrayList<Ouverture> tableauOrientations : ouvertures){
            if(tableauOrientations.contains(orientation)) return true;
        }
        return false;
    }
    
    
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
    

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public boolean isInamovible() {
        return inamovible;
    }
    
    public ArrayList<ArrayList<Ouverture>> getOuvertures() {
        return ouvertures;
    }

    public ArrayList<Boolean> getDejaVisite() {
        return dejaVisite;
    }

    public void setDejaVisite(int index, Boolean dejaVisite) {
        this.dejaVisite.set(index, dejaVisite);
    }
}
