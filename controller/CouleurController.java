package projetIG.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import projetIG.model.CouleurTuyau;
import projetIG.model.Orientation;
import projetIG.model.Rotation;
import projetIG.model.TypeTuyau;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.TuyauPlateau;

public class CouleurController extends MouseAdapter {
    protected Niveau niveauCourant;

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
        ArrayList<TuyauPlateau> sourcesVisitees = new ArrayList<>();
        
        for(TuyauPlateau tuyauSource : this.niveauCourant.getPlateauCourant()) {
            if(tuyauSource.getNom() == TypeTuyau.SOURCE && !sourcesVisitees.contains(tuyauSource)) {
                sourcesVisitees.add(tuyauSource);
                
                Orientation orientationSource = tuyauSource.getOrientations().get(0).get(0);
                
                System.out.println("Depart d'une source C/L : " + tuyauSource.getColonne() + ", " + tuyauSource.getLigne()); //debug

                connexionCaseSuivante(tuyauSource, orientationSource, tuyauSource.getCouleur().get(0), sourcesVisitees);
            }
            System.out.println(""); // debug
        }
        System.out.println(""); // debug
    }
    
    private void connexionCaseSuivante(TuyauPlateau tuyauEntrant, Orientation orientationTuyauEntrant,
                                        CouleurTuyau couleur, ArrayList<TuyauPlateau> sourcesVisitees){
        
        //System.out.print("C/L tuyau entrant : " + tuyauEntrant.getColonne()+ ", " + tuyauEntrant.getLigne()+ ", "); //debug
        //System.out.print("Orientation : " + orientationTuyauEntrant + " changement C/L : " + Orientation.changementLigne(orientationTuyauEntrant)+ ", " + Orientation.changementColonne(orientationTuyauEntrant)+ ", "); //debug

        int ligneSortie = tuyauEntrant.getLigne() + Orientation.changementLigne(orientationTuyauEntrant);
        int colonneSortie = tuyauEntrant.getColonne() + Orientation.changementColonne(orientationTuyauEntrant);
                
        for(TuyauPlateau tuyauPlateau : this.niveauCourant.getPlateauCourant()) {
            if(tuyauPlateau.getLigne() == ligneSortie && tuyauPlateau.getColonne() == colonneSortie){
                //System.out.print("C/L tuyau courant : " + colonneSortie + ", " + ligneSortie + ", "); //debug
                
                System.out.print("Nom : " + tuyauPlateau.getNom() + ", "); //debug
                
                Orientation orientationEntree = Orientation.values()[
                        (orientationTuyauEntrant.ordinal() + Rotation.DEMI_TOUR.ordinal())
                        % Orientation.values().length];
                System.out.print("Orientation d'entree : " + orientationEntree + ", "); //debug
                
                if(tuyauPlateau.aUneOuverture(orientationEntree)){
                    if(tuyauPlateau.getNom() == TypeTuyau.SOURCE){
                        System.out.print("Arrivee a une source C/L : " + tuyauPlateau.getColonne()+ ", " + tuyauPlateau.getLigne()); //debug
                        
                        sourcesVisitees.add(tuyauPlateau);
                        
                        if(couleur != CouleurTuyau.NOIR && tuyauPlateau.getCouleur().get(0) != couleur){
                            couleur = CouleurTuyau.NOIR;
                            connexionCaseSuivante(tuyauPlateau, orientationEntree, couleur, sourcesVisitees);
                        }
                    }
                    
                    else {
                        if(tuyauPlateau.getNom() == TypeTuyau.OVER
                            && (orientationEntree == Orientation.NORTH || orientationEntree == Orientation.SOUTH)){
                            
                            tuyauPlateau.setCouleur(1, couleur);
                        }
                        
                        else { tuyauPlateau.setCouleur(0, couleur); }
                        
                        System.out.print("Couleur mise : " + couleur + ", "); //debug
                        System.out.print("Orientation de sortie : " + tuyauPlateau.ouverturesConnectees(orientationEntree) + ", "); //debug
                        
                        for(Orientation orientationSortie : tuyauPlateau.ouverturesConnectees(orientationEntree)){
                            System.out.println("Orientation de sortie : " + orientationSortie + ", "); //debug

                            connexionCaseSuivante(tuyauPlateau, orientationSortie, couleur, sourcesVisitees);
                        }
                    }
                }
                
                break;
            }
        }
    }
}
