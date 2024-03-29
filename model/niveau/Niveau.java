package projetIG.model.niveau;

import projetIG.model.enumeration.Couleur;
import static projetIG.model.enumeration.Couleur.BLANC;
import static projetIG.model.enumeration.Couleur.NOIR;
import projetIG.model.enumeration.Dir;
import static projetIG.model.enumeration.Dir.E;
import static projetIG.model.enumeration.Dir.N;
import static projetIG.model.enumeration.Dir.O;
import static projetIG.model.enumeration.Dir.S;
import projetIG.model.enumeration.Type;
import static projetIG.model.enumeration.Type.AJOUT;
import static projetIG.model.enumeration.Type.OVER;
import static projetIG.model.enumeration.Type.SOURCE;

public class Niveau {
    private final int hauteur;
    private final int largeur;
    private final TuyauP[][] plateau;
    private final TuyauR[][] reserve;
    private boolean victoire = false;

    // Constructeur
    public Niveau(int hauteur, int largeur, TuyauP[][] plateau, TuyauR[][] reserve) {
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.plateau = plateau;
        this.reserve = reserve;
    }
    
    // Met a jour les couleurs des tuyaux et determine s'il y a victoire
    public boolean majCouleurs(){
        this.victoire = true;
        // Reinitialisation : pour tous les tuyaux, on indique qu'ils n'ont pas 
        // ete visites et que (sauf pour les sources) ils sont blancs
        for(int l = 0; l < this.hauteur; l++) {
            for(int c = 0; c < this.largeur; c++) {
                TuyauP tuyau = plateau[l][c];
                
                if(tuyau != null) {
                    for(int i = 0; i < tuyau.getVisite().size(); i++){
                        tuyau.setVisite(i, false);
                        if(tuyau.getType() != SOURCE) tuyau.setCouleur(i, BLANC);
                    }
                }
            }
        }
        // On parcourt les tuyaux en partant des sources non deja visitees
        for(int l = 0; l < this.hauteur; l++) {
            for(int c = 0; c < this.largeur; c++) {
                TuyauP tuyau = plateau[l][c];
                
                if(tuyau != null
                        && tuyau.getType() == SOURCE
                        && !tuyau.getVisite().get(0)) {
                    tuyau.setVisite(0, true);
                    // On determine l'ouverture de la source a partir du modele
                    Dir dirModel = Type.ouvertures[SOURCE.ordinal()][0][0];
                    Dir dirSource = dirModel.rotation(tuyau.getRotation(),
                                                      AJOUT);
                    // On passe a la case a laquelle la source est connectee
                    connexionCaseSuivante(l, c, dirSource,
                                          tuyau.getCouleur().get(0));
                }
            }
        }
        return this.victoire;
    }
    
    // (recursion) Trouve les cases auxquelles sont connectees les tuyaux du 
    // plateau, met a jour les couleurs et determiner s'il y a victoire ou non
    private void connexionCaseSuivante(int ligne, int colonne, Dir entree,
                                       Couleur couleur){
        // Determine les coordonnees de la case suivante
        int l = ligne + entree.ligne();
        int c = colonne + entree.colonne();
        TuyauP tuyau = this.plateau[l][c];
        
        // Si le tuyau donne sur une case vide, il n'y a pas victoire
        if(tuyau == null) this.victoire = false;
        else{
            // On determine la direction d'entree
            // (demi-tour par rapport � sortie de la case precedente)
            Dir dirEntree = entree.rotation(S, AJOUT);
            
            // Si le tuyau n'a pas d'ouverture correspondant, pas de victoire
            if( !tuyau.getType().aOuverture(dirEntree, tuyau.getRotation()) ){
                this.victoire = false;
            }
            // On execute cette partie du code si :
            // - le tuyau a une ouverture correspondant a l'entree
            // - ET la composante du tuyau n'est pas noire
            // - ET (la composante du tuyau n'a pas deja ete visite 
            //       OU est peinte avec une couleur differente de celle
            //          actuellement utilisee)
            else if(
                    ((tuyau.getType() == OVER
                        && (dirEntree == N || dirEntree == S)
                        && tuyau.getCouleur().get(1) != NOIR
                        && (!tuyau.getVisite().get(1)
                            || tuyau.getCouleur().get(1) != couleur)
                        )
                    ||
                    // Composante E-W d'un OVER
                    (tuyau.getType() == OVER
                        && (dirEntree == E || dirEntree == O)
                        && tuyau.getCouleur().get(0) != NOIR
                        && (!tuyau.getVisite().get(0)
                            || tuyau.getCouleur().get(0) != couleur)
                        )
                    ||
                    // Tuyaux autres que OVER
                    (tuyau.getType() != OVER
                        && tuyau.getCouleur().get(0) != NOIR
                        && (!tuyau.getVisite().get(0)
                            || tuyau.getCouleur().get(0) != couleur)
                        )
                )
            ){
                // Si on arrive a une source et qu'elle est de couleur
                // differente, la couleur courante devient noire
                if(tuyau.getType() == SOURCE){
                    tuyau.setVisite(0, true);
                    
                    if(couleur != NOIR && tuyau.getCouleur().get(0) != couleur){
                        couleur = NOIR;
                        this.victoire = false;
                        connexionCaseSuivante(l, c, dirEntree, couleur);
                    }
                }
                // Sinon on met a jour le tuyau avec la couleur courante
                else {
                    // Composante N-S d'un OVER
                    if(tuyau.getType() == OVER
                        && (dirEntree == N || dirEntree == S)){
                        tuyau.setCouleur(1, couleur);
                        tuyau.setVisite(1, true);
                    }
                    // Autres tuyaux (y compris composante E-O d'un over)
                    else {
                        tuyau.setCouleur(0, couleur);
                        tuyau.setVisite(0, Boolean.TRUE);
                    }
                    // On applique le meme traitement a toutes les autres cases
                    // auxquelles le tuyau est connecte
                    for(Dir dirSortie : tuyau.getType().dirSorties(
                                              dirEntree, tuyau.getRotation())){
                        connexionCaseSuivante(l, c, dirSortie, couleur);
                    }
                }
            }
        }
    }
    
    
    //Getters
    public int getHauteur() {
        return hauteur;
    }

    public int getLargeur() {
        return largeur;
    }
    public TuyauP[][] getPlateau() {
        return plateau;
    }

    public TuyauR[][] getReserve() {
        return reserve;
    }
    
}
