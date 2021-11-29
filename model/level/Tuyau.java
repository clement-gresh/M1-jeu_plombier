package projetIG.model.level;

public class Tuyau {
    protected String nom;
    protected int nombre;

    public Tuyau(String nom, int nombre) {
        this.nom = nom;
        this.nombre = nombre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }
}
