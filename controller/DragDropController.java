package projetIG.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import projetIG.model.CouleurTuyau;
import projetIG.view.FenetreJeu;

public class DragDropController extends MouseAdapter {
    protected FenetreJeu fenetreJeu;

    public DragDropController(FenetreJeu fenetreJeu) {
        this.fenetreJeu = fenetreJeu;
    }
    
    @Override
    public void mousePressed(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){            
            // On determine la colonne et la ligne de la case où le clic a eu lieu
            int colonne = (int) Math.ceil( event.getX() / this.fenetreJeu.getLargeurCase() ) ;
            int ligne = (int) Math.ceil( event.getY() / this.fenetreJeu.getHauteurCase() ) ;
            
            System.out.println("Colonne " + colonne + " ligne " + ligne); // debug
            
            // On recupere l'image du tuyau avec la rotation correspondant
            BufferedImage buffImgDD = this.fenetreJeu.getPipes().getSubimage(2 * (120 + 20),
                    CouleurTuyau.BLANC.ordinal() * (120 + 20), 120, 120);
            //if(rotation != 0) imgTemp = pivoter(imgTemp, rotation);
            
            
            // On met à jour l'image de jeu avec l'image Drag&Drop et ses coordonnees
            this.fenetreJeu.setImageDD(new ImageIcon(buffImgDD));
            this.fenetreJeu.setXImageDD(event.getX() - (int) (0.5 * this.fenetreJeu.getLargeurCase()));
            this.fenetreJeu.setYImageDD(event.getY() - (int) (0.5 * this.fenetreJeu.getHauteurCase()));
            this.fenetreJeu.repaint();
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            // On met a jour les coordonnes de l'image de Drag&Drop dans la fenetre de jeu
            this.fenetreJeu.setXImageDD(event.getX() - (int) (0.5 * this.fenetreJeu.getLargeurCase()));
            this.fenetreJeu.setYImageDD(event.getY() - (int) (0.5 * this.fenetreJeu.getHauteurCase()));
            this.fenetreJeu.repaint();
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            // On remet l'image Drag&Drop dans la fenetre de jeu à vide
            this.fenetreJeu.setImageDD(new ImageIcon());
            this.fenetreJeu.repaint();
        }
    }
}