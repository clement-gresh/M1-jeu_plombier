package projetIG.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import projetIG.model.niveau.Niveau;
import projetIG.view.PanelJeu;

public class VictoireController extends MouseAdapter {
    protected PanelJeu fenetreJeu;
    protected Niveau niveau;

    public VictoireController(PanelJeu fenetreJeu, Niveau niveau) {
        this.fenetreJeu = fenetreJeu;
        this.niveau = niveau;
    }
    
    @Override
    public void mousePressed(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            boolean victoire = this.niveau.majCouleurs();
            if(victoire) this.fenetreJeu.victoire();
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            boolean victoire = this.niveau.majCouleurs();
            if(victoire) this.fenetreJeu.victoire();
        }
    }
}
