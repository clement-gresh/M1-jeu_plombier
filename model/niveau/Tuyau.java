package projetIG.model.niveau;

public class Tuyau {
    protected String nom;
    protected int rotation; // Rotation en quarts de tours
    protected int nombre;  // Nombre de tuyaux disponibles dans la reserve
    protected int colonne; // Colonne du tuyau dans pipes.gif (de 0 à 6)

    public Tuyau(String nom, int rotation, int nombre, int colonne) {
        this.nom = nom;
        this.rotation = rotation;
        this.nombre = nombre;
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

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public int getColonne() {
        return colonne;
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
    }
    
    
}
