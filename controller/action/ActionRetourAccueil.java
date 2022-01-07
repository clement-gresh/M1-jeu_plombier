package projetIG.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import projetIG.Plombier;

public class ActionRetourAccueil extends AbstractAction {
    private final Plombier panelPlumber;

    public ActionRetourAccueil(Plombier panelPlumber) {
        this.panelPlumber = panelPlumber;
        
        this.putValue(Action.NAME, "Accueil");
        //this.putValue(Action.SMALL_ICON, new ImageIcon(
        //        "src/main/java/projetIG/view/image/icone/exit.png"));
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        this.putValue(Action.SHORT_DESCRIPTION, "Retourner à l'accueil (Ctrl + A)");
        this.putValue(Action.ACCELERATOR_KEY, 
                KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        Plombier.pressAlt();
        ActionRetourAccueil.this.panelPlumber.getFrameParent().setAlwaysOnTop(true);
        
        int clickButton = JOptionPane.showConfirmDialog(this.panelPlumber, 
                "Etes-vous sûr de vouloir retourner à l'accueil ? La partie en cours sera perdue.", 
                "Retour à l'accueil", JOptionPane.YES_NO_OPTION);
        
        if(clickButton == JOptionPane.YES_OPTION) {
            this.panelPlumber.afficherAccueil1();
        }
        
        
        ActionRetourAccueil.this.panelPlumber.getFrameParent().setAlwaysOnTop(false);
        Plombier.releaseAlt();
    }
    
}
