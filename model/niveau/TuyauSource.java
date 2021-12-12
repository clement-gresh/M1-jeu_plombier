package projetIG.model.niveau;

import projetIG.model.CouleurTuyau;

public class TuyauSource extends TuyauPlateau {
    protected CouleurTuyau couleur;

    public TuyauSource(String tuyau, int ligne, int colonne) {
        super(tuyau, ligne, colonne);
        
        this.inamovible = true;
        this.couleur = CouleurTuyau.appartient( tuyau.substring(0, 1) );
    }

    public CouleurTuyau getCouleur() {
        return couleur;
    }
}
