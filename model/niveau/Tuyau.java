package projetIG.model.niveau;

import java.util.ArrayList;
import projetIG.model.enumeration.CouleurTuyau;
import projetIG.model.enumeration.Rotation;
import projetIG.model.enumeration.TypeTuyau;

public class Tuyau {
    protected TypeTuyau nom;
    protected Rotation rotation;
    protected ArrayList<CouleurTuyau> couleur = new ArrayList<>();
    
    
    // CONSTRUCTEURS
    public Tuyau(TypeTuyau typeTuyau, Rotation rotation) {
        this.nom = typeTuyau;
        this.rotation = rotation;
    }
    
    public Tuyau(Tuyau tuyau) {
        this.nom = tuyau.getNom();
        this.rotation = tuyau.getRotation();
    }

    
    // GETTERS
    public TypeTuyau getNom() {
        return nom;
    }

    public Rotation getRotation() {
        return rotation;
    }
    
    public ArrayList<CouleurTuyau> getCouleur() {
        return couleur;
    }

    // SETTERS
    public void setCouleur(int index, CouleurTuyau couleur) {
        this.couleur.set(index, couleur);
    }
}
