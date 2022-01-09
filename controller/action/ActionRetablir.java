package projetIG.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import projetIG.controller.AnnulerManager;

public class ActionRetablir extends AbstractAction {
    private final AnnulerManager manager;

    public ActionRetablir(AnnulerManager manager, String cheminImg) {
        this.manager = manager;
        this.putValue(Action.NAME, "Rétablir");
        this.putValue(Action.SMALL_ICON, new ImageIcon(
                cheminImg + "/icone/redo.png"));
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Y);
        this.putValue(Action.SHORT_DESCRIPTION,
                "Rétablir la dernière action annulée (Ctrl + Y)");
        this.putValue(Action.ACCELERATOR_KEY, 
                KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ActionRetablir.this.manager.redo();
    }
    
}
