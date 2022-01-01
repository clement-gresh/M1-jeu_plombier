package projetIG.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import projetIG.controller.CouleurVictoireController;
import projetIG.controller.DragDropController;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.ParserNiveau;

public class PanelFenetreJeu extends JPanel {
    protected Niveau niveauCourant;
    
    public PanelFenetreJeu(String cheminNiveau) {
        this.setLayout(new BorderLayout(10, 10));
        
        //Creation du niveau
        this.niveauCourant = ParserNiveau.parserNiveau(cheminNiveau);
        
        //Ajout de la fenetre de jeu
        FenetreJeu fenetreJeu = new FenetreJeu(this, this.niveauCourant);
        this.add(fenetreJeu, BorderLayout.CENTER);
        
              
        //Ajout du controller Drag&Drop sur la fenetre de jeu
        DragDropController dragDrop = new DragDropController(fenetreJeu);
        this.addMouseListener(dragDrop);
        this.addMouseMotionListener(dragDrop);
        
        //Ajout du controller des couleurs sur la fenetre de jeu
        CouleurVictoireController couleurController = new CouleurVictoireController(this.niveauCourant);
        this.addMouseListener(couleurController);
    }
}
