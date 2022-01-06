package projetIG.view.menu;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import projetIG.controller.action.ActionAnnuler;
import projetIG.controller.action.ActionQuitter;
import projetIG.controller.action.ActionRecommencerNiveau;
import projetIG.controller.action.ActionRetablir;
import projetIG.controller.action.ActionRetourAccueil;

public class MyPopupMenu extends JPopupMenu {

    public MyPopupMenu(JFrame frameParent, ActionRetourAccueil actionRetourAccueil, ActionRecommencerNiveau actionRecommencer,
                       ActionAnnuler actionAnnuler, ActionRetablir actionRetablir) {
        
        // Ajout des elements du menu
        this.add(actionRetourAccueil);
        this.addSeparator();
        this.add(actionRecommencer);
        this.add(actionAnnuler);
        this.add(actionRetablir);
        this.addSeparator();
        this.add(new ActionQuitter(frameParent));
    }
}
