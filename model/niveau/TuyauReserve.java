package projetIG.model.niveau;

public class TuyauReserve extends Tuyau {
    protected int nombre;  // Nombre de tuyaux disponibles dans la reserve

    public TuyauReserve(String nom, int rotation, int nombre, int colonne) {
        super(nom, rotation, colonne);
        this.nombre = nombre;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }
}
