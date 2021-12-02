package projetIG.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import projetIG.model.level.Niveau;

public class PanelPlateauJeu extends JPanel {
    protected Niveau niveauCourant;

    public PanelPlateauJeu() {
        this.setLayout(new BorderLayout(10, 10));
        
        //Creation du niveau
        this.niveauCourant = new Niveau();
        
        //ajout du plateau de jeu
        PlateauJeu plateau = new PlateauJeu(this);
        this.add(plateau);
    }

    public Niveau getNiveauCourant() {
        return niveauCourant;
    }
}
