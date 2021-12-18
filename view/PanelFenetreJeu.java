package projetIG.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import projetIG.controller.CouleurController;
import projetIG.controller.DragDropController;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.ParserNiveau;

public class PanelFenetreJeu extends JPanel {
    protected Niveau niveauCourant;
    
    public PanelFenetreJeu() {
        this.setLayout(new BorderLayout(10, 10));
        
        //Creation du niveau
        this.niveauCourant = ParserNiveau.parserNiveau("src/main/java/projetIG/model/niveau/banque1/level3.p");
        
        //Ajout de la fenetre de jeu
        FenetreJeu fenetreJeu = new FenetreJeu(this, this.niveauCourant);
        this.add(fenetreJeu, BorderLayout.CENTER);
        
              
        //Ajout du controller Drag&Drop sur la fenetre de jeu
        DragDropController dragDrop = new DragDropController(fenetreJeu);
        this.addMouseListener(dragDrop);
        this.addMouseMotionListener(dragDrop);
        
        //Ajout du controller des couleurs sur la fenetre de jeu
        CouleurController couleurController = new CouleurController(this.niveauCourant);
        this.addMouseListener(couleurController);
    }
}
