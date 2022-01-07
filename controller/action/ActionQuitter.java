package projetIG.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import projetIG.Plombier;

public class ActionQuitter extends AbstractAction {
    private final Plombier panelPlombier;

    public ActionQuitter(Plombier panelPlombier) {
        this.panelPlombier = panelPlombier;
        
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
        ActionQuitter.this.panelPlombier.confirmClose();
    }
}
