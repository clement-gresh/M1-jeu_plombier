package projetIG.model.niveau;

public class Tuyau {
    protected String nom;
    protected int rotation; // Rotation en quarts de tours en sens horaire
    protected int colonne; // Colonne du tuyau dans pipes.gif (de 0 à 6)

    public Tuyau(String nom, int rotation, int colonne) {
        this.nom = nom;
        this.rotation = rotation;
        this.colonne = colonne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getColonne() {
        return colonne;
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
    }
}
