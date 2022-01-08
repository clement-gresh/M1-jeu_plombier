package projetIG.view.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import projetIG.Plombier;
import projetIG.controller.action.ActionAnnuler;
import projetIG.controller.action.ActionQuitter;
import projetIG.controller.action.ActionRecommencer;
import projetIG.controller.action.ActionRetablir;
import projetIG.controller.action.ActionAccueil;

public class MyMenuBar extends JMenuBar {

    public MyMenuBar(Plombier panelPlombier, ActionAccueil actionRetourAccueil, ActionRecommencer actionRecommencer,
                     ActionAnnuler actionAnnuler, ActionRetablir actionRetablir) {
        
        //Creation du menu Jeu
        JMenu menuJeu = new JMenu("Jeu");
        menuJeu.setMnemonic('J');
        this.add(menuJeu);
        
        // Ajout des elements du menu
        menuJeu.add(actionRetourAccueil);
        menuJeu.addSeparator();
        menuJeu.add(actionRecommencer);
        menuJeu.add(actionAnnuler);
        menuJeu.add(actionRetablir);
        menuJeu.addSeparator();
        menuJeu.add(new ActionQuitter(panelPlombier));
    }
}
