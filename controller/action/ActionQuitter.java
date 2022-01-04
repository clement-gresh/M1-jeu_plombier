package projetIG.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class ActionQuitter extends AbstractAction {
    private final JFrame frameParent;

    public ActionQuitter(JFrame frameParent) {
        this.frameParent = frameParent;
        
        this.putValue(Action.NAME, "Quitter");
        this.putValue(Action.SMALL_ICON, new ImageIcon(
                "src/main/java/projetIG/view/image/icone/exit.png"));
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);
        this.putValue(Action.SHORT_DESCRIPTION, "Quitter le jeu (Ctrl + W)");
        this.putValue(Action.ACCELERATOR_KEY, 
                KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int clickButton = JOptionPane.showConfirmDialog(this.frameParent, 
                "Etes vous sur de vouloir quitter le jeu ?", 
                "Quitter", JOptionPane.YES_NO_OPTION);
        
        if(clickButton == JOptionPane.YES_OPTION) {
            this.frameParent.dispose();
        }
    }
    
}
