package projetIG.model.niveau;

import static java.lang.Boolean.FALSE;
import java.util.ArrayList;
import projetIG.model.enumeration.Couleur;
import static projetIG.model.enumeration.Couleur.BLANC;
import projetIG.model.enumeration.Dir;
import projetIG.model.enumeration.TypeTuyau;
import static projetIG.model.enumeration.TypeTuyau.OVER;

public class TuyauP extends Tuyau {
    private boolean fixe = false;
    private final ArrayList<Boolean> visite = new ArrayList<>();
    
    public TuyauP(TypeTuyau type, Dir rotation, boolean fixe,
            Couleur couleur) {
        super(type, rotation);
        this.fixe = fixe;
        this.couleur.add(couleur);
        this.visite.add(FALSE);
        // On ajoute une 2eme composante aux OVER
        if(this.type == OVER) {
            this.couleur.add(couleur);
            this.visite.add(FALSE);
        }
    }
    
    public TuyauP(Tuyau tuyau) {
        super(tuyau);
        this.couleur.add(Couleur.BLANC);
        this.visite.add(false);
        // On ajoute une 2eme composante aux OVER
        if(this.type == OVER) {
            this.couleur.add(BLANC);
            this.visite.add(false);
        }
    }
    
    // GETTERS
    public boolean isFixe() {
        return fixe;
    }

    public ArrayList<Boolean> getVisite() {
        return visite;
    }
    
    // SETTERS
    public void setVisite(int index, Boolean dejaVisite) {
        this.visite.set(index, dejaVisite);
    }
}
