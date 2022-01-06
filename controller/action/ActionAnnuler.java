package projetIG.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import projetIG.controller.AnnulerManager;

public class ActionAnnuler extends AbstractAction {
    private final AnnulerManager annulerManager;

    public ActionAnnuler(AnnulerManager annulerManager) {
        this.annulerManager = annulerManager;
        
        this.putValue(Action.NAME, "Annuler");
        this.putValue(Action.SMALL_ICON, new ImageIcon(
                "src/main/java/projetIG/view/image/icone/undo.png"));
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Z);
        this.putValue(Action.SHORT_DESCRIPTION, "Annuler la dernière action (Ctrl + Z)");
        this.putValue(Action.ACCELERATOR_KEY, 
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ActionAnnuler.this.annulerManager.undo();
    }
    
}