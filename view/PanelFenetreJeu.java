package projetIG.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import projetIG.Plombier;
import projetIG.controller.CouleurVictoireController;
import projetIG.controller.DragDropController;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.ParserNiveau;

public class PanelFenetreJeu extends JPanel {
    public static final int NBR_CASES_RESERVE_LARGEUR = 2;
    public static final int NBR_CASES_RESERVE_HAUTEUR = 6; 
    
    protected Plombier panelPlumber;
    protected Niveau niveauCourant;
    protected int nbrCasesTotalLargeur;
    protected int nbrCasesTotalHauteur;
    protected int taillePixelLargeur;
    protected int taillePixelHauteur = 700;
    protected DragDropController dragDrop;
    protected CouleurVictoireController couleurController;
    
    // Constructeur
    public PanelFenetreJeu(Plombier panelPlumber, String cheminNiveau) {
        this.panelPlumber = panelPlumber;
        this.setLayout(new BorderLayout(10, 10));
        
        
        //Creation du niveau
        this.niveauCourant = ParserNiveau.parserNiveau(cheminNiveau);
        
        
        // Calcul de la taille de la fenetre de jeu (en cases et en pixels)
        this.nbrCasesTotalLargeur = this.niveauCourant.getNbrCasesPlateauLargeur() + NBR_CASES_RESERVE_LARGEUR;
        this.nbrCasesTotalHauteur = Integer.max(this.niveauCourant.getNbrCasesPlateauHauteur(), NBR_CASES_RESERVE_HAUTEUR);
        this.taillePixelLargeur = (int) (this.taillePixelHauteur * this.nbrCasesTotalLargeur / this.nbrCasesTotalHauteur);
        
        this.setPreferredSize(new Dimension(this.taillePixelLargeur, this.taillePixelHauteur)); // largeur, hauteur
        
        
        //Ajout de la fenetre de jeu
        FenetreJeu fenetreJeu = new FenetreJeu(this, this.niveauCourant, this.taillePixelLargeur, this.taillePixelHauteur);
        this.add(fenetreJeu, BorderLayout.CENTER);
        
              
        //Ajout du controller Drag&Drop sur la fenetre de jeu
        dragDrop = new DragDropController(fenetreJeu);
        this.addMouseListener(dragDrop);
        this.addMouseMotionListener(dragDrop);
        
        
        //Ajout du controller Couleurs & Victoire sur la fenetre de jeu
        couleurController = new CouleurVictoireController(this, fenetreJeu, this.niveauCourant);
        
        this.addMouseListener(couleurController);
    }
    
    
    // Getters
    public Plombier getPanelPlumber() {
        return panelPlumber;
    }

    public Niveau getNiveauCourant() {
        return niveauCourant;
    }

    public int getNbrCasesTotalLargeur() {
        return nbrCasesTotalLargeur;
    }

    public int getNbrCasesTotalHauteur() {
        return nbrCasesTotalHauteur;
    }

    public int getTaillePixelLargeur() {
        return taillePixelLargeur;
    }

    public int getTaillePixelHauteur() {
        return taillePixelHauteur;
    }
    
    

    public DragDropController getDragDropController() {
        return dragDrop;
    }

    public CouleurVictoireController getCouleurController() {
        return couleurController;
    }
}
