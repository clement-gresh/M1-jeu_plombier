package projetIG.menus;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import projetIG.actions.ActionQuitter;

public class MyPopupMenu extends JPopupMenu {
    private JFrame frameParent;

    public MyPopupMenu(JFrame frameParent) {
        this.frameParent = frameParent;
        
        this.add(new ActionQuitter(frameParent));
    }
}
