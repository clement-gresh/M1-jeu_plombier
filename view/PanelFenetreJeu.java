package projetIG.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import projetIG.controller.DragDropController;
import projetIG.model.niveau.Niveau;

public class PanelFenetreJeu extends JPanel {
    protected Niveau niveauCourant;

    public PanelFenetreJeu() {
        this.setLayout(new BorderLayout(10, 10));
        
        //Creation du niveau
        this.niveauCourant = new Niveau();
        
        //Ajout de la fenetre de jeu
        FenetreJeu fenetreJeu = new FenetreJeu(this);
        this.add(fenetreJeu, BorderLayout.CENTER);
        
        //Ajout du controller Drag&Drop sur la fenetre de jeu
        DragDropController dragDrop = new DragDropController(fenetreJeu);
        this.addMouseListener(dragDrop);
        this.addMouseMotionListener(dragDrop);
    }

    public Niveau getNiveauCourant() {
        return niveauCourant;
    }
}
