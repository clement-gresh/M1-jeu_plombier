package projetIG.view;

import java.awt.Dimension;
import java.io.File;
import javax.swing.Box;
import javax.swing.JButton;
import projetIG.Plombier;
import static projetIG.Plombier.AUCUN_NIVEAU;

public class PanelNiveaux extends AbstractAccueil {
    private int numBanque;
    
    public PanelNiveaux(Plombier plombier, int numBanque) {
        super(plombier);
        this.numBanque = numBanque;
        File banque = new File(plombier.chemin(numBanque, AUCUN_NIVEAU));
        
        // Ajout d'un bouton par fichier de niveau se trouvant dans le dossier
        for(int i = 1; i <= banque.list().length; i++){
            JButton bouton = this.creerBouton("Niveau n° " + i);
            int numNiveau = i;
            
            bouton.addActionListener((event) -> PanelNiveaux.this.plombier
                    .afficherNiveau(PanelNiveaux.this.numBanque, numNiveau));
        }
        // Ajout d'un bouton retour
        this.add(Box.createRigidArea(new Dimension(0, 50)));
        JButton retour = new JButton("Retour");
        retour.setPreferredSize(new Dimension(200, 70));
        retour.setAlignmentX(CENTER_ALIGNMENT);
        retour.addActionListener((event) -> PanelNiveaux.this.plombier
                                                 .afficherPnlBanques());
        this.add(retour);
    }
}
