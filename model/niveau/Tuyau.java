package projetIG.model.niveau;

import projetIG.model.CouleurTuyau;
import projetIG.model.Rotation;
import projetIG.model.TypeTuyau;

public class Tuyau {
    protected TypeTuyau nom;
    protected Rotation rotation = Rotation.PAS_DE_ROTATION;
    protected CouleurTuyau couleur = CouleurTuyau.NOIR;

    public Tuyau(String tuyau) {
        if(tuyau.startsWith("*")){ tuyau = tuyau.substring(1); }
        
        this.nom = TypeTuyau.appartient( tuyau.substring(0, 1) );
        
        if(!(this.nom == TypeTuyau.CROSS || this.nom == TypeTuyau.OVER)){
            int nbrRotations = Integer.parseInt(tuyau.substring(1, 2));
            this.rotation = Rotation.values()[nbrRotations];
        }
    }
    
    public Tuyau(Tuyau tuyau) {
        this.nom = tuyau.getNom();
        this.rotation = tuyau.getRotation();
        this.couleur = CouleurTuyau.BLANC;
    }

    public TypeTuyau getNom() {
        return nom;
    }

    public Rotation getRotation() {
        return rotation;
    }
    
    public CouleurTuyau getCouleur() {
        return couleur;
    }

    public void setCouleur(CouleurTuyau couleur) {
        this.couleur = couleur;
    }
}
