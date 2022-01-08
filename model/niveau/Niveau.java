package projetIG.model.niveau;

import java.util.ArrayList;
import projetIG.model.enumeration.Couleur;
import static projetIG.model.enumeration.Couleur.BLANC;
import static projetIG.model.enumeration.Couleur.NOIR;
import projetIG.model.enumeration.Dir;
import static projetIG.model.enumeration.Dir.E;
import static projetIG.model.enumeration.Dir.N;
import static projetIG.model.enumeration.Dir.O;
import static projetIG.model.enumeration.Dir.S;
import projetIG.model.enumeration.TypeTuyau;
import static projetIG.model.enumeration.TypeTuyau.AJOUT;
import static projetIG.model.enumeration.TypeTuyau.OVER;
import static projetIG.model.enumeration.TypeTuyau.SOURCE;

public class Niveau {
    protected int nbrCasesPlateauHauteur;
    protected int nbrCasesPlateauLargeur;
    protected ArrayList<ArrayList<TuyauPlateau>> plateauCourant = new ArrayList<>();
    protected ArrayList<ArrayList<TuyauReserve>> tuyauxReserve = new ArrayList<>();
    protected boolean victoire = false;

    // Constructeur
    public Niveau(int nbrCasesPlateauHauteur, int nbrCasesPlateauLargeur,
            ArrayList<ArrayList<TuyauPlateau>> plateauCourant,
            ArrayList<ArrayList<TuyauReserve>> tuyauxReserve) {
        
        this.nbrCasesPlateauHauteur = nbrCasesPlateauHauteur;
        this.nbrCasesPlateauLargeur = nbrCasesPlateauLargeur;
        this.plateauCourant = plateauCourant;
        this.tuyauxReserve = tuyauxReserve;
    }
    
    
    // Met a jour les couleurs des tuyaux et determine s'il y a victoire
    public boolean majCouleurs(){
        this.victoire = true;
        
        // Pour tous les tuyaux, on indique qu'ils n'ont pas ete visites 
        // et que (sauf pour les sources) ils sont blancs
        for(ArrayList<TuyauPlateau> lignePlateau : this.plateauCourant) {
            for(TuyauPlateau tuyauPlateau : lignePlateau) {
                if(tuyauPlateau != null) {
                    for(int i = 0; i < tuyauPlateau.getDejaVisite().size(); i++){
                        tuyauPlateau.setDejaVisite(i, false);

                        if(tuyauPlateau.getNom() != SOURCE) tuyauPlateau.setCouleur(i, BLANC);
                    }
                }
            }
        }
        
        int colonne = 0;
        int ligne = 0;
        
        // On parcourt les tuyaux en partant des sources (qui n'ont pas encore ete visitee)
        for(ArrayList<TuyauPlateau> lignePlateau : this.plateauCourant) {
            for(TuyauPlateau tuyauSource : lignePlateau) {
                if(tuyauSource != null
                        && tuyauSource.getNom() == SOURCE
                        && !tuyauSource.getDejaVisite().get(0)) {
                    
                    tuyauSource.setDejaVisite(0, true);
                    
                    // On determine l'ouverture de la source a partir de celle du modele
                    Dir ouvertureModel = TypeTuyau.ouvertures.get(SOURCE.ordinal()).get(0).get(0);
                    Dir ouvertureSource = ouvertureModel.rotation(tuyauSource.getRotation(), AJOUT);
                    
                    // On applique le traitement a la case a laquelle la source est connectee
                    connexionCaseSuivante(ligne, colonne, ouvertureSource,
                                          tuyauSource.getCouleur().get(0));
                }
                colonne = colonne + 1;
            }
            colonne = 0;
            ligne = ligne + 1;
        }
        return this.victoire;
    }
    
    
    // Fonction recursive qui trouve les cases auxquelles sont connectees les tuyaux du plateau pour mettre a jour
    // les couleurs et determiner s'il y a victoire ou non
    private void connexionCaseSuivante(int ligne, int colonne, Dir ouvertureTuyauEntrant, Couleur couleur){
        
        int ligneSortie = ligne + Dir.nvlLigne(ouvertureTuyauEntrant);
        int colonneSortie = colonne + Dir.nvlColonne(ouvertureTuyauEntrant);
        
        TuyauPlateau tuyauPlateau = this.plateauCourant.get(ligneSortie).get(colonneSortie);
        
        // Si le tuyau donne sur une case vide, il n'y a pas victoire
        if(tuyauPlateau == null) this.victoire = false;
        
        else{
            // On determine par l'ouverture d'entree
            Dir ouvertureEntree = ouvertureTuyauEntrant.rotation(S, AJOUT);
            
            // Si le tuyau n'a pas d'ouverture correspondant, il n'y a pas victoire
            if( !tuyauPlateau.getNom().aOuverture(ouvertureEntree, tuyauPlateau.getRotation()) ){
                this.victoire = false;
            }
            
            // On execute cette partie du code si :
            // - le tuyau a une ouverture correspondant a l'entree
            // - ET la composante du tuyau n'est pas noire
            // - ET (la composante du tuyau n'a pas deja ete visite 
            //       OU est peinte avec une couleur differente de celle actuellement utilisee)
            else if(
                    ((tuyauPlateau.getNom() == OVER
                        && (ouvertureEntree == N || ouvertureEntree == S)
                        && tuyauPlateau.getCouleur().get(1) != NOIR
                        && (!tuyauPlateau.getDejaVisite().get(1)
                            || tuyauPlateau.getCouleur().get(1) != couleur)
                        )

                    ||

                    // Composante E-W d'un OVER
                    (tuyauPlateau.getNom() == OVER
                        && (ouvertureEntree == E || ouvertureEntree == O)
                        && tuyauPlateau.getCouleur().get(0) != NOIR
                        && (!tuyauPlateau.getDejaVisite().get(0)
                            || tuyauPlateau.getCouleur().get(0) != couleur)
                        )

                    ||

                    // Tuyaux autres que OVER
                    (tuyauPlateau.getNom() != OVER
                        && tuyauPlateau.getCouleur().get(0) != NOIR
                        && (!tuyauPlateau.getDejaVisite().get(0)
                            || tuyauPlateau.getCouleur().get(0) != couleur)
                        )
                )
            ){
                
                // Si on arrive a une source et qu'elle est de couleur differente, la couleur courante devient noire
                if(tuyauPlateau.getNom() == SOURCE){
                    tuyauPlateau.setDejaVisite(0, true);
                    
                    if(couleur != NOIR && tuyauPlateau.getCouleur().get(0) != couleur){
                        couleur = NOIR;
                        this.victoire = false;
                        connexionCaseSuivante(ligneSortie, colonneSortie, ouvertureEntree, couleur);
                    }
                }
                
                // Sinon on met a jour la couleur du tuyau avec la couleur courante
                else {
                    // Composante N-S d'un OVER
                    if(tuyauPlateau.getNom() == OVER
                        && (ouvertureEntree == N || ouvertureEntree == S)){

                        tuyauPlateau.setCouleur(1, couleur);
                        tuyauPlateau.setDejaVisite(1, true);
                    }
                    
                    // Autres tuyaux (y compris composante E-O d'un over)
                    else {
                        tuyauPlateau.setCouleur(0, couleur);
                        tuyauPlateau.setDejaVisite(0, Boolean.TRUE);
                    }
                    
                    // On applique le meme traitement a toutes les cases auxquelles le tuyau est connecte
                    // (i.e. toutes celles sur lesquelles il a une ouverture, sauf pour l'ouverture d'entree)
                    for(Dir orientationSortie : 
                            tuyauPlateau.getNom().dirSorties(
                                    ouvertureEntree, tuyauPlateau.getRotation())){
                        
                        connexionCaseSuivante(ligneSortie, colonneSortie, orientationSortie, couleur);
                    }
                }
            }
        }
    }
    
    
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
