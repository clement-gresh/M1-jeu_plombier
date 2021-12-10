package projetIG.model.grapheCouleurs;

import projetIG.model.CouleurTuyau;

public class SommetGraphe {
    protected int ligne;
    protected int colonne;
    protected boolean source;
    protected CouleurTuyau couleur = CouleurTuyau.BLANC;

    public SommetGraphe(int ligne, int colonne, boolean source) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.source = source;        
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }
}
