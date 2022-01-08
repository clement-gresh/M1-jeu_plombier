package projetIG.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import projetIG.Plombier;

public class ActionAccueil extends AbstractAction {
    private final Plombier panelPlumber;

    public ActionAccueil(Plombier panelPlumber) {
        this.panelPlumber = panelPlumber;
        
        this.putValue(Action.NAME, "Accueil");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        this.putValue(Action.SHORT_DESCRIPTION, "Retourner � l'accueil (Ctrl + A)");
        this.putValue(Action.ACCELERATOR_KEY, 
                KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        int clicBouton = Plombier.fenetreConfirmation(ActionAccueil.this.panelPlumber.getFrameParent(),
                "Retour � l'accueil",
                "Etes-vous s�r de vouloir retourner � l'accueil ? La partie en cours sera perdue.");
        
        if(clicBouton == JOptionPane.YES_OPTION) {
            this.panelPlumber.afficherPnlBanques();
        }
    }
}