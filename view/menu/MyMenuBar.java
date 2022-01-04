package projetIG.view.menu;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import projetIG.Plumber;
import projetIG.controller.action.ActionQuitter;
import projetIG.controller.action.ActionRetourAccueil;

public class MyMenuBar extends JMenuBar {
    private JFrame frameParent;
    private Plumber panelPlumber;

    public MyMenuBar(JFrame frameParent, Plumber panelPlumber) {
        this.frameParent = frameParent;
        this.panelPlumber = panelPlumber;
        
        //Creation du menu Jeu
        JMenu menuJeu = new JMenu("Jeu");
        menuJeu.setMnemonic('J');
        this.add(menuJeu);
        
        menuJeu.add(new ActionRetourAccueil(this.panelPlumber));
        menuJeu.addSeparator();
        menuJeu.add(new ActionQuitter(this.frameParent));
    }
}
