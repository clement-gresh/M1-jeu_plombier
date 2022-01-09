package projetIG.model.niveau;

import java.util.ArrayList;
import projetIG.model.enumeration.Couleur;
import projetIG.model.enumeration.Dir;
import projetIG.model.enumeration.TypeTuyau;

public class Tuyau {
    protected TypeTuyau type;
    protected Dir rotation;
    protected ArrayList<Couleur> couleur = new ArrayList<>();
    
    // CONSTRUCTEURS
    public Tuyau(TypeTuyau type, Dir rotation) {
        this.type = type;
        this.rotation = rotation;
    }
    
    public Tuyau(Tuyau tuyau) {
        this.type = tuyau.getType();
        this.rotation = tuyau.getRotation();
    }
    
    // GETTERS
    public TypeTuyau getType() {
        return type;
    }

    public Dir getRotation() {
        return rotation;
    }
    
    public ArrayList<Couleur> getCouleur() {
        return couleur;
    }

    // SETTERS
    public void setCouleur(int index, Couleur couleur) {
        this.couleur.set(index, couleur);
    }
}
