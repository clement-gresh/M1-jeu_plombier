package projetIG.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import projetIG.model.CouleurTuyau;
import projetIG.model.TypeTuyau;
import projetIG.model.niveau.TuyauReserve;
import projetIG.view.FenetreJeu;

public class DragDropController extends MouseAdapter {
    protected FenetreJeu fenetreJeu;
    protected int hauteurCase;
    protected int largeurCase;
    protected int nbrCasesTotalHauteur;
    protected int nbrCasesTotalLargeur;
    protected boolean deplacementTuyau = false;
    protected String tuyauDeplace;

    public DragDropController(FenetreJeu fenetreJeu) {
        this.fenetreJeu = fenetreJeu;
    }
    /*
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
                // On verifie que colonne ne depasse pas le nombre maximal (peut arriver a cause de 
                // l'imprecision sur le nombre de pixels lie au cast en int). On lui assigne le max sinon.
                if(colonne > this.nbrCasesTotalLargeur - 1)
                    colonne = this.nbrCasesTotalLargeur - 1;
                
                // On verifie que ligne est inferieure à 6 (nombre de lignes dans la reserve)
                if(ligne < 6) {
                    TuyauReserve tuyauReserve = this.fenetreJeu.getPanelParent().getNiveauCourant()
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
                        
                        this.deplacementTuyau = true;
                        this.tuyauDeplace = tuyauReserve.getNom();
                    }
                }
                
            }
            
            // Sinon, si le clic a lieu sur le plateau de jeu
            else if (ligne < this.nbrCasesTotalHauteur) {
                String tuyauPlateau = this.fenetreJeu.getPanelParent().getNiveauCourant()
                                                .getPlateauCourant().get(ligne).get(colonne);
                
                int typeTuyau = TypeTuyau.appartient(tuyauPlateau.substring(0, 1));
                
                
                System.out.println("DD plateau de Jeu : tuyau " + tuyauPlateau); //debug
                
                // S'il s'agit d'un tuyau deplacable, on recupere l'image et on enleve le tuyau du plateau
                if(typeTuyau > -1) {
                    System.out.println("DD plateau de Jeu, bon type de tuyau : " + typeTuyau); //debug
                    
                    buffTuyauDD = this.fenetreJeu.getPipes().getSubimage(
                            typeTuyau * (120 + 20),
                            CouleurTuyau.BLANC.ordinal() * (120 + 20),
                            120, 120);
                    
                    int rotation = Integer.parseInt(tuyauPlateau.substring(1, 2));
                    if(rotation != 0) buffTuyauDD = FenetreJeu.pivoter(buffTuyauDD, rotation);
                    
                    this.fenetreJeu.getPanelParent().getNiveauCourant().getPlateauCourant().get(ligne).set(colonne, ".");
                    
                    this.deplacementTuyau = true;
                    this.tuyauDeplace = tuyauPlateau;
                }
            }
            
            
            
            
            // On ajoute l'image Drag&Drop au plateau de jeu (apres lui avoir donne la bonne taille)
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
            if(deplacementTuyau) {
                // On determine la colonne et la ligne de la case où le bouton a ete relache ( entre 0 et (nbrCases - 1) )
                int colonne = (int) Math.ceil( event.getX() / this.largeurCase ) ;
                int ligne = (int) Math.ceil( event.getY() / this.hauteurCase ) ;

                System.out.println("Colonne relaché " + colonne + " ligne relaché " + ligne); // debug


                // On verifie qu'on se trouve a l'interieur du plateau de jeu (sans les bordures)
                if( (colonne > 0)
                        && (colonne < this.nbrCasesTotalLargeur - 3)
                        && (ligne > 0)
                        && (ligne < this.nbrCasesTotalHauteur - 1)
                        ) {
                    
                    if(this.fenetreJeu.getPanelParent().getNiveauCourant().getPlateauCourant().get(ligne).get(colonne).equals("."))
                    {
                        this.fenetreJeu.getPanelParent().getNiveauCourant().getPlateauCourant().get(ligne).set(colonne, tuyauDeplace);
                    }
                    
                    else{
                        renvoyerDansReserve();
                    }
                    
                }

                else {
                    renvoyerDansReserve();
                }
            
            
                // On remet l'image Drag&Drop dans la fenetre de jeu à vide
                this.fenetreJeu.setImageDD(new ImageIcon());
                this.fenetreJeu.repaint();

                this.deplacementTuyau = false;
            }
        }
    }
    
    // Renvoie le tuyauDeplace dans la reserve
    private void renvoyerDansReserve(){
        for(TuyauReserve tuyauReserve : this.fenetreJeu.getPanelParent().getNiveauCourant().getTuyauxDisponibles()){
            if(tuyauReserve.getNom().equals(this.tuyauDeplace)){
                tuyauReserve.setNombre(tuyauReserve.getNombre() + 1);
                break;
            }
        }
    }
*/
}