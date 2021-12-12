package projetIG.model.niveau;

import java.util.ArrayList;
import projetIG.model.CouleurTuyau;
import projetIG.model.Orientation;
import projetIG.model.TypeTuyau;

public class TuyauPlateau extends Tuyau {
    protected int ligne;
    protected int colonne;
    protected boolean inamovible = false;
    protected ArrayList<Orientation> orientations;
    
    public TuyauPlateau(String tuyau, int ligne, int colonne) {
        super(tuyau);
        
        this.ligne = ligne;
        this.colonne = colonne;
        
        if(tuyau.startsWith("*")){ this.inamovible = true; }
        
        this.orientations = Orientation.orientations(this.nom);
        
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
        
        this.orientations = Orientation.orientations(this.nom);
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
    
    public ArrayList<Orientation> getOrientations() {
        return orientations;
    }
}
