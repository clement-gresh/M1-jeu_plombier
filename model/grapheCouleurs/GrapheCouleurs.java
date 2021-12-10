package projetIG.model.grapheCouleurs;

import java.util.ArrayList;
import projetIG.model.CouleurTuyau;

public class GrapheCouleurs {
    protected int nbrCasesPlateauHauteur;
    protected int nbrCasesPlateauLargeur;
    protected ArrayList<ArrayList<SommetGraphe>> sommetsGraphe = new ArrayList<>();
    protected ArrayList<ArrayList<ArcGraphe>> arcsGraphe = new ArrayList<>();
    protected ArrayList<ArrayList<CouleurTuyau>> plateauCouleursTuyaux = new ArrayList<>();

    public GrapheCouleurs(int nbrCasesPlateauHauteur, int nbrCasesPlateauLargeur) {
        this.nbrCasesPlateauHauteur = nbrCasesPlateauHauteur;
        this.nbrCasesPlateauLargeur = nbrCasesPlateauLargeur;
        
        for(int ligne = 1; ligne < this.nbrCasesPlateauHauteur - 1; ligne++){
            
            // On ajoute deux lignes a la matrice contenant les sommets
            this.sommetsGraphe.add(new ArrayList<SommetGraphe>());
            this.sommetsGraphe.add(new ArrayList<SommetGraphe>());
            
            for(int colonne = 1; colonne < this.nbrCasesPlateauLargeur - 1; colonne++){
                
            }
            
        }
        
        
    }
    
    
}
