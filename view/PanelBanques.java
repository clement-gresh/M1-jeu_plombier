package projetIG.view;

import javax.swing.JButton;
import projetIG.Plombier;

public class PanelBanques extends AbstractAccueil {
    protected final int NOMBRE_BANQUES = 2;
    
    public PanelBanques(Plombier panelParent) {
        super(panelParent);
        
        for(int i = 1; i <= NOMBRE_BANQUES; i++){
            JButton bouton = this.creerBouton("Banque n° " + i);
            int numeroBanque = i;
            
            bouton.addActionListener(
                    (event) -> PanelBanques.this.panelParent.afficherPnlNiveaux(numeroBanque)
            );
        }
    }
}
