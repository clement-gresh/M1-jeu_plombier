package projetIG.model.niveau;

import java.util.ArrayList;

public class Niveau {
    protected int nbrCasesPlateauHauteur;
    protected int nbrCasesPlateauLargeur;
    protected ArrayList<ArrayList<TuyauPlateau>> plateauCourant = new ArrayList<>();
    protected ArrayList<ArrayList<TuyauReserve>> tuyauxReserve = new ArrayList<>();

    public Niveau(int nbrCasesPlateauHauteur, int nbrCasesPlateauLargeur,
            ArrayList<ArrayList<TuyauPlateau>> plateauCourant,
            ArrayList<ArrayList<TuyauReserve>> tuyauxReserve) {
        this.nbrCasesPlateauHauteur = nbrCasesPlateauHauteur;
        this.nbrCasesPlateauLargeur = nbrCasesPlateauLargeur;
        this.plateauCourant = plateauCourant;
        this.tuyauxReserve = tuyauxReserve;
    }
    
    
    // Enregistre la hauteur et la largeur du plateau,
    // initialise le plateau (avec les sources et tuyaux inamovibles)
    // ainsi que la reserve
    
    //Getters
    public int getNbrCasesPlateauHauteur() {
        return nbrCasesPlateauHauteur;
    }

    public int getNbrCasesPlateauLargeur() {
        return nbrCasesPlateauLargeur;
    }
    public ArrayList<ArrayList<TuyauPlateau>> getPlateauCourant() {
        return plateauCourant;
    }

    public ArrayList<ArrayList<TuyauReserve>> getTuyauxReserve() {
        return tuyauxReserve;
    }
    
}
