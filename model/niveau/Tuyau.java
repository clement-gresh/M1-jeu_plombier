package projetIG.model.niveau;

import java.util.ArrayList;
import projetIG.model.enumeration.CouleurTuyau;
import projetIG.model.enumeration.Rotation;
import projetIG.model.enumeration.TypeTuyau;

public class Tuyau {
    protected TypeTuyau nom;
    protected Rotation rotation;
    protected ArrayList<CouleurTuyau> couleur = new ArrayList<>();

    public Tuyau(String tuyau) {
        if(tuyau.startsWith("*")){ tuyau = tuyau.substring(1); }
        
        this.nom = TypeTuyau.appartient( tuyau.substring(0, 1) );
        
        if(!(this.nom == TypeTuyau.CROSS) && !(this.nom == TypeTuyau.OVER)){
            int nbrRotations = Integer.parseInt(tuyau.substring(1, 2));
            this.rotation = Rotation.values()[nbrRotations];
        }
        
        else{ this.rotation = Rotation.PAS_DE_ROTATION; }
        
        this.couleur.add(CouleurTuyau.NOIR);
        if(this.nom == TypeTuyau.OVER) this.couleur.add(CouleurTuyau.NOIR);
    }
    
    public Tuyau(Tuyau tuyau) {
        this.nom = tuyau.getNom();
        this.rotation = tuyau.getRotation();
        
        this.couleur.add(CouleurTuyau.BLANC);
        if(tuyau.getNom() == TypeTuyau.OVER) this.couleur.add(CouleurTuyau.BLANC);
    }

    public TypeTuyau getNom() {
        return nom;
    }

    public Rotation getRotation() {
        return rotation;
    }
    
    public ArrayList<CouleurTuyau> getCouleur() {
        return couleur;
    }

    public void setCouleur(int index, CouleurTuyau couleur) {
        this.couleur.set(index, couleur);
    }
}
