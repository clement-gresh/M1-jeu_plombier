package projetIG.view;

import java.awt.Dimension;
import java.io.File;
import javax.swing.Box;
import javax.swing.JButton;
import projetIG.Plombier;

public class PanelNiveaux extends AbstractAccueil {
    protected int numeroBanque;
    
    public PanelNiveaux(Plombier panelParent, int numeroBanque) {
        super(panelParent);
        this.numeroBanque = numeroBanque;
        
        // Ajout d'un bouton par fichier de niveau se trouvant dans le dossier
        File banque = new File( panelParent.cheminFichier(numeroBanque, Plombier.AUCUN_NIVEAU) );
        
        for(int i = 1; i <= banque.list().length; i++){
            JButton bouton = this.creerBouton("Niveau n° " + i);
            int numeroNiveau = i;
            
            bouton.addActionListener(
                    (event) -> PanelNiveaux.this.panelParent.afficherNiveau(
                                    PanelNiveaux.this.numeroBanque, numeroNiveau));
        }
        
        
        // Ajout d'un bouton retour au panel des banques de niveau
        this.add(Box.createRigidArea(new Dimension(0, 50)));
        
        JButton retour = new JButton("Retour");
        retour.setPreferredSize(new Dimension(200, 70));
        retour.setAlignmentX(CENTER_ALIGNMENT);
        retour.addActionListener((event) -> PanelNiveaux.this.panelParent.afficherPnlBanques());
        this.add(retour);
    }
}
