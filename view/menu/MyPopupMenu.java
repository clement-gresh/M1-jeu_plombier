package projetIG.view.menu;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import projetIG.Plumber;
import projetIG.controller.AnnulerManager;
import projetIG.controller.action.ActionAnnuler;
import projetIG.controller.action.ActionQuitter;
import projetIG.controller.action.ActionRecommencerNiveau;
import projetIG.controller.action.ActionRetablir;
import projetIG.controller.action.ActionRetourAccueil;

public class MyPopupMenu extends JPopupMenu {

    public MyPopupMenu(JFrame frameParent, Plumber panelPlumber,
                       ActionAnnuler actionAnnuler, ActionRetablir actionRetablir) {
        
        // Ajout des elements du menu
        this.add(new ActionRetourAccueil(panelPlumber));
        this.addSeparator();
        this.add(new ActionRecommencerNiveau(panelPlumber));
        this.add(actionAnnuler);
        this.add(actionRetablir);
        //this.add(annuler);
        //this.add(retablir);
        this.addSeparator();
        this.add(new ActionQuitter(frameParent));
    }
}
