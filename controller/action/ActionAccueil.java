package projetIG.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import projetIG.Plombier;

public class ActionAccueil extends AbstractAction {
    private final Plombier plombier;

    public ActionAccueil(Plombier plombier) {
        this.plombier = plombier;
        this.putValue(Action.NAME, "Accueil");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        this.putValue(Action.SHORT_DESCRIPTION,
                "Retourner à l'accueil (Ctrl + A)");
        this.putValue(Action.ACCELERATOR_KEY, 
                KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int clicBouton = Plombier.confirmation(
                ActionAccueil.this.plombier.getFrame(),
                "Retour à l'accueil",
                "Etes-vous sûr de vouloir retourner à l'accueil ? "
                + "La partie en cours sera perdue.");
        
        if(clicBouton == JOptionPane.YES_OPTION) { 
            this.plombier.afficherPnlBanques();
        }
    }
}
