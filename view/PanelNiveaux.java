package projetIG.view;

import java.io.File;
import javax.swing.JButton;
import projetIG.Plombier;

public class PanelNiveaux extends AbstractAccueil {
    protected int numeroBanque;
    
    public PanelNiveaux(Plombier panelParent, int numeroBanque) {
        super(panelParent);
        this.numeroBanque = numeroBanque;
        
        File banque = new File( panelParent.cheminFichier(numeroBanque, Plombier.AUCUN_NIVEAU) );
        
        for(int i = 1; i <= banque.list().length; i++){
            JButton bouton = this.creerBouton("Niveau n° " + i);
            int numeroNiveau = i;
            
            bouton.addActionListener(
                    (event) -> PanelNiveaux.this.panelParent.afficherNiveau(
                                    PanelNiveaux.this.numeroBanque, numeroNiveau));
        }
    }
}
