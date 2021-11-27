package projetIG.view.menu;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import projetIG.controller.action.ActionQuitter;

public class MyMenuBar extends JMenuBar {
    private JFrame frameParent;

    public MyMenuBar(JFrame frameParent) {
        this.frameParent = frameParent;
        
        //Creation du menu Jeu
        JMenu menuJeu = new JMenu("Jeu");
        menuJeu.setMnemonic('J');
        this.add(menuJeu);
        
        menuJeu.add(new ActionQuitter(this.frameParent));
    }
}
