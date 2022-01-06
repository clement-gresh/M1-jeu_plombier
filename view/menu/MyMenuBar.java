package projetIG.view.menu;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import projetIG.Plumber;
import projetIG.controller.AnnulerManager;
import projetIG.controller.action.ActionAnnuler;
import projetIG.controller.action.ActionQuitter;
import projetIG.controller.action.ActionRecommencerNiveau;
import projetIG.controller.action.ActionRetablir;
import projetIG.controller.action.ActionRetourAccueil;

public class MyMenuBar extends JMenuBar {

    public MyMenuBar(JFrame frameParent, Plumber panelPlumber, 
                     ActionAnnuler actionAnnuler, ActionRetablir actionRetablir) {
        
        //Creation du menu Jeu
        JMenu menuJeu = new JMenu("Jeu");
        menuJeu.setMnemonic('J');
        this.add(menuJeu);
        
        // Ajout des elements du menu
        menuJeu.add(new ActionRetourAccueil(panelPlumber));
        menuJeu.addSeparator();
        menuJeu.add(new ActionRecommencerNiveau(panelPlumber));
        menuJeu.add(actionAnnuler);
        menuJeu.add(actionRetablir);
        //menuJeu.add(annuler);
        //menuJeu.add(retablir);
        menuJeu.addSeparator();
        menuJeu.add(new ActionQuitter(frameParent));
    }
}
