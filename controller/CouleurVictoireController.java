package projetIG.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import javax.swing.SwingUtilities;
import projetIG.Plombier;
import projetIG.model.enumeration.Couleur;
import static projetIG.model.enumeration.Couleur.BLANC;
import static projetIG.model.enumeration.Couleur.NOIR;
import projetIG.model.enumeration.Dir;
import projetIG.model.enumeration.TypeTuyau;
import static projetIG.model.enumeration.TypeTuyau.OVER;
import static projetIG.model.enumeration.TypeTuyau.SOURCE;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.TuyauPlateau;
import projetIG.view.FenetreJeu;
import projetIG.view.PanelFenetreJeu;
import static projetIG.model.enumeration.Dir.N;
import static projetIG.model.enumeration.Dir.E;
import static projetIG.model.enumeration.Dir.S;
import static projetIG.model.enumeration.Dir.O;
import static projetIG.model.enumeration.TypeTuyau.AJOUT;

public class CouleurVictoireController extends MouseAdapter {
    protected PanelFenetreJeu panelCourant;
    protected FenetreJeu fenetreJeu;
    protected Niveau niveauCourant;
    
    protected boolean victoire;

    public CouleurVictoireController(PanelFenetreJeu panelCourant, FenetreJeu fenetreJeu, Niveau niveauCourant) {
        this.panelCourant = panelCourant;
        this.fenetreJeu = fenetreJeu;
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
    
    // Met a jour les couleurs des tuyaux et determine s'il y a victoire
    public void majCouleurs(){
        this.victoire = true;
        
        // Pour tous les tuyaux, on indique qu'ils n'ont pas ete visites 
        // et que (sauf pour les sources) ils sont blancs
        for(ArrayList<TuyauPlateau> lignePlateau : this.niveauCourant.getPlateauCourant()) {
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
        for(ArrayList<TuyauPlateau> lignePlateau : this.niveauCourant.getPlateauCourant()) {
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
        
        if(victoire) {
            // On met a jour la vue avant d'afficher une fenetre de dialogue
            this.fenetreJeu.paintImmediately(0, 0, this.fenetreJeu.getPanelParent().getTaillePixelLargeur(),
                                             this.fenetreJeu.getPanelParent().getTaillePixelHauteur());
            
            int numeroBanque = this.panelCourant.getPanelPlumber().getNumeroBanque();
            int numeroNiveau = this.panelCourant.getPanelPlumber().getNumeroNiveau();
            
            
            // Passer au niveau suivant s'il existe
            if(this.panelCourant.getPanelPlumber().isThereNextLevel()){
                Plombier.pressAlt();
                this.panelCourant.getPanelPlumber().getFrameParent().setAlwaysOnTop(true);
                
                int clickButton = JOptionPane.showConfirmDialog(this.panelCourant, 
                                            "VICTOIRE ! Passer au niveau suivant ?", 
                                            "Victoire", YES_NO_OPTION);
        
                if(clickButton == YES_OPTION) {
                    this.panelCourant.getPanelPlumber().afficherNiveau(numeroBanque, numeroNiveau + 1);
                }
                
                this.panelCourant.getPanelPlumber().getFrameParent().setAlwaysOnTop(false);
                Plombier.releaseAlt();
            }
            
            
            // Revenir à l'accueil sinon
            else {
                Plombier.pressAlt();
                this.panelCourant.getPanelPlumber().getFrameParent().setAlwaysOnTop(true);
                
                int clickButton = JOptionPane.showConfirmDialog(this.panelCourant, 
                "VICTOIRE ! Revenir à l'accueil ?", 
                "Victoire", YES_NO_OPTION);
        
                if(clickButton == YES_OPTION) {
                    this.panelCourant.getPanelPlumber().afficherPnlBanques();
                }
                
                this.panelCourant.getPanelPlumber().getFrameParent().setAlwaysOnTop(false);
                Plombier.releaseAlt();
            }
        }
    }
    
    
    // Fonction recursive qui trouve les cases auxquelles sont connectees les tuyaux du plateau pour mettre a jour
    // les couleurs et determiner s'il y a victoire ou non
    private void connexionCaseSuivante(int ligne, int colonne, Dir ouvertureTuyauEntrant, Couleur couleur){
        
        int ligneSortie = ligne + Dir.nvlLigne(ouvertureTuyauEntrant);
        int colonneSortie = colonne + Dir.nvlColonne(ouvertureTuyauEntrant);
        
        TuyauPlateau tuyauPlateau = this.niveauCourant.getPlateauCourant().get(ligneSortie).get(colonneSortie);
        
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
}
