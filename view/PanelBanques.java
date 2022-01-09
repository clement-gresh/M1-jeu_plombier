package projetIG.view;

import javax.swing.JButton;
import projetIG.Plombier;

public class PanelBanques extends AbstractAccueil {
    protected final int NBR_BANQUES = 2;
    
    public PanelBanques(Plombier plombier) {
        super(plombier);
        // Ajout d'un bouton par banque de niveaux
        for(int i = 1; i <= NBR_BANQUES; i++){
            JButton bouton = this.creerBouton("Banque n° " + i);
            int numBanque = i;
            bouton.addActionListener((event) -> PanelBanques.this.plombier
                                                .afficherPnlNiveaux(numBanque)
            );
        }
    }
}
