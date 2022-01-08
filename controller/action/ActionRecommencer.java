package projetIG.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import projetIG.Plombier;

public class ActionRecommencer extends AbstractAction {
    private final Plombier panelPlumber;

    public ActionRecommencer(Plombier panelPlumber) {
        this.panelPlumber = panelPlumber;
        
        this.putValue(Action.NAME, "Recommencer");
        this.putValue(Action.SMALL_ICON, new ImageIcon(
                "src/main/java/projetIG/view/image/icone/new.png"));
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
        this.putValue(Action.SHORT_DESCRIPTION, "Recommencer le niveau (Ctrl + N)");
        this.putValue(Action.ACCELERATOR_KEY, 
                KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        int clicBouton = Plombier.fenetreConfirmation(ActionRecommencer.this.panelPlumber.getFrameParent(),
                "Recommencer le niveau",
                "Etes-vous sûr de vouloir recommencer le niveau ? La configuration en cours sera perdue.");
        
        if(clicBouton == JOptionPane.YES_OPTION) {
            this.panelPlumber.afficherNiveau(this.panelPlumber.getNumeroBanque(), this.panelPlumber.getNumeroNiveau());
        }
    }
    
}
