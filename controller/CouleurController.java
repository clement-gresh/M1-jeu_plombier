package projetIG.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import projetIG.model.enumeration.CouleurTuyau;
import projetIG.model.enumeration.Ouverture;
import projetIG.model.enumeration.Rotation;
import projetIG.model.enumeration.TypeTuyau;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.TuyauPlateau;

public class CouleurController extends MouseAdapter {
    protected Niveau niveauCourant;
    protected boolean victoire;

    public CouleurController(Niveau niveauCourant) {
        this.niveauCourant = niveauCourant;
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            majCouleurs();
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            majCouleurs();
        }
    }
    
    private void majCouleurs(){
        this.victoire = true;
        
        // Pour tous les tuyaux, on indique qu'ils n'ont pas ete visites 
        // et que (sauf pour les sources) ils sont blancs
        for(ArrayList<TuyauPlateau> lignePlateau : this.niveauCourant.getPlateauCourant()) {
            for(TuyauPlateau tuyauPlateau : lignePlateau) {
                if(tuyauPlateau != null) {
                    for(int i = 0; i < tuyauPlateau.getDejaVisite().size(); i++){
                        tuyauPlateau.setDejaVisite(i, Boolean.FALSE);

                        if(tuyauPlateau.getNom() != TypeTuyau.SOURCE) tuyauPlateau.setCouleur(i, CouleurTuyau.BLANC);
                    }
                }
            }
        }
        
        int colonne = 0;
        int ligne = 0;
        
        // On parcourt les tuyaux en partant des sources
        for(ArrayList<TuyauPlateau> lignePlateau : this.niveauCourant.getPlateauCourant()) {
            for(TuyauPlateau tuyauSource : lignePlateau) {
                if(tuyauSource != null
                        && tuyauSource.getNom() == TypeTuyau.SOURCE
                        && !tuyauSource.getDejaVisite().get(0)) {
                    
                    tuyauSource.setDejaVisite(0, Boolean.TRUE);

                    Ouverture ouvertureModel = TypeTuyau.ouvertures
                                               .get(TypeTuyau.SOURCE.ordinal()).get(0).get(0);
                    Ouverture ouvertureSource = TypeTuyau.ouvertureAvecRotation(
                                                ouvertureModel, tuyauSource.getRotation(),
                                                TypeTuyau.AJOUTER_ROTATION);
                    
                    System.out.println("Depart d'une source. Orientation : " + TypeTuyau.ouvertures.get(TypeTuyau.SOURCE.ordinal()).get(0).get(0)); //debug

                    connexionCaseSuivante(ligne, colonne, ouvertureSource,
                                          tuyauSource.getCouleur().get(0));
                }
                colonne = colonne + 1;
            }
            colonne = 0;
            ligne = ligne + 1;
            System.out.println(""); // debug
        }
        
        if(victoire) System.out.println("Gagne !"); // debug
        else System.out.println("Perdu..."); // debug
        
    }
    
    private void connexionCaseSuivante(int ligne, int colonne, Ouverture ouvertureTuyauEntrant, CouleurTuyau couleur){
        
        //System.out.print("C/L tuyau entrant : " + tuyauEntrant.getColonne()+ ", " + tuyauEntrant.getLigne()+ ", "); //debug
        //System.out.print("Orientation : " + orientationTuyauEntrant + " changement C/L : " + Orientation.changementLigne(orientationTuyauEntrant)+ ", " + Orientation.changementColonne(orientationTuyauEntrant)+ ", "); //debug

        int ligneSortie = ligne + Ouverture.changementLigne(ouvertureTuyauEntrant);
        int colonneSortie = colonne + Ouverture.changementColonne(ouvertureTuyauEntrant);
        
        TuyauPlateau tuyauPlateau = this.niveauCourant.getPlateauCourant().get(ligneSortie).get(colonneSortie);
        
        if(tuyauPlateau == null) this.victoire = false;
        
        else{
            //System.out.print("C/L tuyau courant : " + colonneSortie + ", " + ligneSortie + ", "); //debug

            System.out.print("Nom : " + tuyauPlateau.getNom() + ", "); //debug

            Ouverture ouvertureEntree = Ouverture.values()[
                    (ouvertureTuyauEntrant.ordinal() + Rotation.DEMI_TOUR.ordinal())
                    % Ouverture.values().length];
            System.out.print("Orientation d'entree : " + ouvertureEntree + ", "); //debug

            // On execute cette partie du code si :
            // - le tuyau a une ouverture
            // - ET la composante du tuyau n'est pas noire
            // - ET (la composante du tuyau n'a pas deja ete visite 
            //       OU a deja ete visitee mais peinte avec une couleur differente de celle actuellement utilisee)
            if(TypeTuyau.aUneOuverture(tuyauPlateau.getNom(), ouvertureEntree, tuyauPlateau.getRotation())

                && 
                    // Composante N-S d'un OVER
                    ((tuyauPlateau.getNom() == TypeTuyau.OVER
                        && (ouvertureEntree == Ouverture.NORTH || ouvertureEntree == Ouverture.SOUTH)
                        && tuyauPlateau.getCouleur().get(1) != CouleurTuyau.NOIR
                        && (!tuyauPlateau.getDejaVisite().get(1)
                            || tuyauPlateau.getCouleur().get(1) != couleur)
                        )

                    ||

                    // Composante E-W d'un OVER
                    (tuyauPlateau.getNom() == TypeTuyau.OVER
                        && (ouvertureEntree == Ouverture.EAST || ouvertureEntree == Ouverture.WEST)
                        && tuyauPlateau.getCouleur().get(0) != CouleurTuyau.NOIR
                        && (!tuyauPlateau.getDejaVisite().get(0)
                            || tuyauPlateau.getCouleur().get(0) != couleur)
                        )

                    ||

                    // Tuyaux autre que OVER
                    (tuyauPlateau.getNom() != TypeTuyau.OVER
                        && tuyauPlateau.getCouleur().get(0) != CouleurTuyau.NOIR
                        && (!tuyauPlateau.getDejaVisite().get(0)
                            || tuyauPlateau.getCouleur().get(0) != couleur)
                        )
                )
            ){

                if(tuyauPlateau.getNom() == TypeTuyau.SOURCE){
                    System.out.print("Arrivee a une source. "); //debug
                    tuyauPlateau.setDejaVisite(0, Boolean.TRUE);

                    if(couleur != CouleurTuyau.NOIR && tuyauPlateau.getCouleur().get(0) != couleur){
                        couleur = CouleurTuyau.NOIR;
                        this.victoire = false;
                        connexionCaseSuivante(ligneSortie, colonneSortie, ouvertureEntree, couleur);
                    }
                }

                else {
                    if(tuyauPlateau.getNom() == TypeTuyau.OVER
                        && (ouvertureEntree == Ouverture.NORTH || ouvertureEntree == Ouverture.SOUTH)){

                        tuyauPlateau.setCouleur(1, couleur);
                        tuyauPlateau.setDejaVisite(1, Boolean.TRUE);
                    }

                    else {
                        tuyauPlateau.setCouleur(0, couleur);
                        tuyauPlateau.setDejaVisite(0, Boolean.TRUE);
                    }

                    System.out.print("Couleur mise : " + couleur + ", "); //debug
                    System.out.print("Orientation de sortie : " + TypeTuyau.ouverturesConnectees(tuyauPlateau.getNom(), ouvertureEntree, tuyauPlateau.getRotation()) + ", "); //debug

                    for(Ouverture orientationSortie : TypeTuyau.ouverturesConnectees(tuyauPlateau.getNom(), ouvertureEntree, tuyauPlateau.getRotation())){
                        System.out.println("Orientation de sortie : " + orientationSortie + ", "); //debug

                        connexionCaseSuivante(ligneSortie, colonneSortie, orientationSortie, couleur);
                    }
                }
            }
        }
    }
}
