package projetIG.view.menu;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import projetIG.Plumber;
import projetIG.controller.action.ActionQuitter;
import projetIG.controller.action.ActionRecommencerNiveau;
import projetIG.controller.action.ActionRetourAccueil;

public class MyPopupMenu extends JPopupMenu {
    private final JFrame frameParent;
    private final Plumber panelPlumber;

    public MyPopupMenu(JFrame frameParent, Plumber panelPlumber) {
        this.frameParent = frameParent;
        this.panelPlumber = panelPlumber;
        
        // Ajout des elements du menu
        this.add(new ActionRetourAccueil(this.panelPlumber));
        this.addSeparator();
        this.add(new ActionRecommencerNiveau(this.panelPlumber));
        this.addSeparator();
        this.add(new ActionQuitter(frameParent));
    }
}
