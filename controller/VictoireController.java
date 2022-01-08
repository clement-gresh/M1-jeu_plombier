package projetIG.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static javax.swing.JOptionPane.YES_OPTION;
import javax.swing.SwingUtilities;
import projetIG.Plombier;
import projetIG.model.niveau.Niveau;
import projetIG.view.PanelJeu;

public class VictoireController extends MouseAdapter {
    protected PanelJeu fenetreJeu;
    protected Niveau niveauCourant;
    
    protected boolean victoire;

    public VictoireController(PanelJeu fenetreJeu, Niveau niveauCourant) {
        this.fenetreJeu = fenetreJeu;
        this.niveauCourant = niveauCourant;
    }
    
    @Override
    public void mousePressed(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            this.niveauCourant.majCouleurs();
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            boolean victoire = this.niveauCourant.majCouleurs();

            if(victoire) {
                // On met a jour la vue avant d'afficher une fenetre de dialogue
                this.fenetreJeu.paintImmediately(0, 0, this.fenetreJeu.getTaillePixelLargeur(),
                                                 this.fenetreJeu.getTaillePixelHauteur());

                int numeroBanque = this.fenetreJeu.getPanelPlombier().getNumeroBanque();
                int numeroNiveau = this.fenetreJeu.getPanelPlombier().getNumeroNiveau();


                // Passer au niveau suivant s'il existe
                if(this.fenetreJeu.getPanelPlombier().isThereNextLevel()){
                    
                    int clicBouton = Plombier.fenetreConfirmation(this.fenetreJeu.getPanelPlombier().getFrameParent(),
                                                 "Victoire", "VICTOIRE ! Passer au niveau suivant ?");
                    
                    if(clicBouton == YES_OPTION) {
                        this.fenetreJeu.getPanelPlombier().afficherNiveau(numeroBanque, numeroNiveau + 1);
                    }
                }


                // Revenir à l'accueil sinon
                else {
                    int clicBouton = Plombier.fenetreConfirmation(this.fenetreJeu.getPanelPlombier().getFrameParent(),
                                                 "Victoire", "VICTOIRE ! Revenir à l'accueil ?");
                    
                    if(clicBouton == YES_OPTION) {
                        this.fenetreJeu.getPanelPlombier().afficherPnlBanques();
                    }
                }
            }
        }
    }
}
