package projetIG.model.niveau;

import java.util.ArrayList;
import projetIG.model.enumeration.Couleur;
import projetIG.model.enumeration.Dir;
import projetIG.model.enumeration.Type;

public class Tuyau {
    protected Type type;
    protected Dir rotation;
    protected ArrayList<Couleur> couleur = new ArrayList<>();
    
    // CONSTRUCTEURS
    public Tuyau(Type type, Dir rotation) {
        this.type = type;
        this.rotation = rotation;
    }
    
    public Tuyau(Tuyau tuyau) {
        this.type = tuyau.getType();
        this.rotation = tuyau.getRotation();
    }
    
    // GETTERS
    public Type getType() {
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
