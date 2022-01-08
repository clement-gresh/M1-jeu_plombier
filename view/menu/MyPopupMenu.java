package projetIG.view.menu;

import javax.swing.JPopupMenu;
import projetIG.Plombier;
import projetIG.controller.action.ActionAnnuler;
import projetIG.controller.action.ActionQuitter;
import projetIG.controller.action.ActionRecommencer;
import projetIG.controller.action.ActionRetablir;
import projetIG.controller.action.ActionAccueil;

public class MyPopupMenu extends JPopupMenu {

    public MyPopupMenu(Plombier panelPlombier, ActionAccueil actionRetourAccueil, ActionRecommencer actionRecommencer,
                       ActionAnnuler actionAnnuler, ActionRetablir actionRetablir) {
        
        // Ajout des elements du menu
        this.add(actionRetourAccueil);
        this.addSeparator();
        this.add(actionRecommencer);
        this.add(actionAnnuler);
        this.add(actionRetablir);
        this.addSeparator();
        this.add(new ActionQuitter(panelPlombier));
    }
}
