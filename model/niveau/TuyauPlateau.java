package projetIG.model.niveau;

import java.util.ArrayList;
import projetIG.model.Orientation;

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
