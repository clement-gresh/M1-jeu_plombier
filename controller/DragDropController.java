package projetIG.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import projetIG.model.CouleurTuyau;
import projetIG.model.niveau.Tuyau;
import projetIG.view.FenetreJeu;

public class DragDropController extends MouseAdapter {
    protected FenetreJeu fenetreJeu;
    protected int hauteurCase;
    protected int largeurCase;
    protected int nbrCasesTotalHauteur;
    protected int nbrCasesTotalLargeur;

    public DragDropController(FenetreJeu fenetreJeu) {
        this.fenetreJeu = fenetreJeu;
    }
    
    @Override
    public void mousePressed(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            this.hauteurCase = this.fenetreJeu.getHauteurCase();
            this.largeurCase = this.fenetreJeu.getLargeurCase();
            
            this.nbrCasesTotalHauteur = this.fenetreJeu.getNbrCasesTotalHauteur();
            this.nbrCasesTotalLargeur = this.fenetreJeu.getNbrCasesTotalLargeur();
                        
            // On determine la colonne et la ligne de la case où le clic a eu lieu ( entre 0 et (nbrCases - 1) )
            int colonne = (int) Math.ceil( event.getX() / this.largeurCase ) ;
            int ligne = (int) Math.ceil( event.getY() / this.hauteurCase ) ;
            
            System.out.println("Colonne " + colonne + " ligne " + ligne); // debug
            
            BufferedImage buffTuyauDD = new BufferedImage(this.largeurCase, this.largeurCase,
                                                          BufferedImage.TYPE_INT_ARGB);
            
            // Si le clic a eu lieu dans la reserve
            if(colonne > this.nbrCasesTotalLargeur - 3) {
                // On verifie si colonne depasse le nombre maximal (a cause de l'imprecision sur le nombre de pixels)
                // On lui assigne le max si c'est le cas)
                if(colonne > this.nbrCasesTotalLargeur - 1)
                    colonne = this.nbrCasesTotalLargeur - 1;
                
                // On verifie que ligne est inferieure à 6 (nombre de lignes dans la reserve)
                if(ligne < 6) {
                    Tuyau tuyauReserve = this.fenetreJeu.getPanelParent().getNiveauCourant()
                                            .getTuyauxDisponibles()
                                            .get(ligne * 2 + (this.nbrCasesTotalLargeur - colonne) % 2);
                    System.out.println("Tuyau : " + tuyauReserve.getNom()); // debug
                    
                    if(tuyauReserve.getNombre() > 0) {
                        // On recupere l'image du tuyau avec la rotation correspondant
                        buffTuyauDD = this.fenetreJeu.getPipes().getSubimage(
                                tuyauReserve.getColonne() * (120 + 20),
                                CouleurTuyau.BLANC.ordinal() * (120 + 20),
                                120, 120);

                        if(tuyauReserve.getRotation() != 0)
                            buffTuyauDD = FenetreJeu.pivoter(buffTuyauDD, tuyauReserve.getRotation());

                        tuyauReserve.setNombre(tuyauReserve.getNombre() - 1);
                    }
                }
                
            }
            
            else {
                String tuyauPlateau = this.fenetreJeu.getPanelParent().getNiveauCourant()
                                                .getPlateauCourant().get(ligne).get(colonne); 
                
                // On recupere l'image du tuyau avec la rotation correspondant
                buffTuyauDD = this.fenetreJeu.getPipes().getSubimage(2 * (120 + 20),
                        CouleurTuyau.BLANC.ordinal() * (120 + 20), 120, 120);
                //if(rotation != 0) imgTemp = pivoter(imgTemp, rotation);
            }
            
            
            
            
            // On l'image Drag&Drop au plateau de jeu (apres lui avoir donne la bonne taille)
            buffTuyauDD = FenetreJeu.modifierTaille(buffTuyauDD, this.largeurCase, this.hauteurCase);
            this.fenetreJeu.setImageDD(new ImageIcon(buffTuyauDD));
            this.fenetreJeu.setXImageDD(event.getX() - (int) (0.5 * this.largeurCase));
            this.fenetreJeu.setYImageDD(event.getY() - (int) (0.5 * this.hauteurCase));
            this.fenetreJeu.repaint();
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            // On met a jour les coordonnes de l'image de Drag&Drop dans la fenetre de jeu
            this.fenetreJeu.setXImageDD(event.getX() - (int) (0.5 * this.largeurCase));
            this.fenetreJeu.setYImageDD(event.getY() - (int) (0.5 * this.hauteurCase));
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