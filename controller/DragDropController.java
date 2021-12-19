package projetIG.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import projetIG.model.enumeration.CouleurTuyau;
import projetIG.model.enumeration.Rotation;
import projetIG.model.enumeration.TypeTuyau;
import projetIG.model.niveau.Tuyau;
import projetIG.model.niveau.TuyauPlateau;
import projetIG.model.niveau.TuyauReserve;
import projetIG.view.FenetreJeu;
import projetIG.view.ModificationsImage;
//import static projetIG.view.FenetreJeu.combiner;

public class DragDropController extends MouseAdapter {
    protected FenetreJeu fenetreJeu;
    protected int hauteurCase;
    protected int largeurCase;
    protected int nbrCasesTotalHauteur;
    protected int nbrCasesTotalLargeur;
    protected int nbrCasesPlateauHauteur;
    protected int nbrCasesPlateauLargeur;
    protected boolean deplacementTuyau = false;
    protected Tuyau tuyauDeplace;
    protected int pasDeDeplacement = 10; // Represente le pas en pixel entre 2 images

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
            
            this.nbrCasesPlateauHauteur = this.fenetreJeu.getNiveauCourant().getNbrCasesPlateauHauteur();
            this.nbrCasesPlateauLargeur = this.fenetreJeu.getNiveauCourant().getNbrCasesPlateauLargeur();
                        
            // On determine la colonne et la ligne de la case où le clic a eu lieu ( entre 0 et (nbrCases - 1) )
            int colonne = (int) Math.ceil( event.getX() / this.largeurCase ) ;
            int ligne = (int) Math.ceil( event.getY() / this.hauteurCase ) ;
            
            BufferedImage buffTuyauDD = new BufferedImage(this.largeurCase, this.largeurCase,
                                                          BufferedImage.TYPE_INT_ARGB);
            
            // Si le clic a eu lieu dans la reserve
            if(colonne > this.nbrCasesTotalLargeur - 3 && ligne < 6) {
                // On verifie que colonne ne depasse pas le nombre maximal (peut arriver a cause de 
                // l'imprecision sur le nombre de pixels lie au cast en int). On lui assigne le max sinon.
                if(colonne > this.nbrCasesTotalLargeur - 1) colonne = this.nbrCasesTotalLargeur - 1;
                
                int colonneReserve = colonne - (this.nbrCasesTotalLargeur - 2);
                
                TuyauReserve tuyauReserve = this.fenetreJeu.getNiveauCourant().getTuyauxReserve()
                                            .get(ligne).get(colonneReserve);

                if(tuyauReserve.getNombre() > 0) {
                    tuyauReserve.diminuerNombre();

                    this.deplacementTuyau = true;
                    this.tuyauDeplace = tuyauReserve;
                }
            }
            
            
            // Sinon, si le clic a lieu sur le plateau de jeu
            else if (colonne < this.nbrCasesPlateauLargeur && ligne < this.nbrCasesPlateauHauteur) {
                
                TuyauPlateau tuyauPlateau = this.fenetreJeu.getNiveauCourant().getPlateauCourant().get(ligne).get(colonne);
                
                if(tuyauPlateau != null && !tuyauPlateau.isInamovible()){
                    this.deplacementTuyau = true;
                    this.tuyauDeplace = tuyauPlateau;

                    // On enleve le tuyau du plateau
                    this.fenetreJeu.getNiveauCourant().getPlateauCourant().get(ligne).set(colonne, null);
                }
            }
            
            
            // On recupere l'image du tuyau deplace
            if(this.deplacementTuyau){
                buffTuyauDD = this.fenetreJeu.getPipes().getSubimage(
                        this.tuyauDeplace.getNom().ordinal() * (120 + 20),
                        CouleurTuyau.BLANC.ordinal() * (120 + 20),
                        120, 120);
                
                if(this.tuyauDeplace.getRotation() != Rotation.PAS_DE_ROTATION){
                    buffTuyauDD = ModificationsImage.pivoter(buffTuyauDD, tuyauDeplace.getRotation().ordinal());
                }
                
                else if(this.tuyauDeplace.getNom().equals(TypeTuyau.OVER)){
                    BufferedImage partieVerticale = this.fenetreJeu.getPipes().getSubimage(
                        (this.tuyauDeplace.getNom().ordinal() - 1) * (120 + 20),
                        CouleurTuyau.BLANC.ordinal() * (120 + 20), 120, 120);
                
                    buffTuyauDD = ModificationsImage.combiner(partieVerticale, buffTuyauDD);
                }
            }
            
            
            // On ajoute l'image Drag&Drop au plateau de jeu (apres lui avoir donne la bonne taille)
            buffTuyauDD = ModificationsImage.modifierTaille(buffTuyauDD, this.largeurCase, this.hauteurCase);
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
                
                int departX = event.getX() - (int) (0.5 * this.largeurCase);
                int departY = event.getY() - (int) (0.5 * this.hauteurCase);
                
                // On verifie qu'on se trouve a l'interieur du plateau de jeu (sans les bordures)
                if( (colonne > 0)
                        && (colonne < this.nbrCasesPlateauLargeur - 1)
                        && (ligne > 0)
                        && (ligne < this.nbrCasesPlateauHauteur - 1)
                        ) {
                    
                    if(this.fenetreJeu.getNiveauCourant().getPlateauCourant().get(ligne).get(colonne) != null){
                        renvoyerDansReserve(departX, departY);
                    }
                    
                    else{
                        deplacerTuyau(departX, departY, colonne * this.largeurCase, ligne * this.hauteurCase);
                        
                        this.fenetreJeu.getNiveauCourant().getPlateauCourant().get(ligne).set(colonne,
                                new TuyauPlateau(this.tuyauDeplace));
                    }
                }

                else {
                    renvoyerDansReserve(departX, departY);
                }
            
            
                // On remet l'image Drag&Drop a vide dans la fenetre de jeu 
                this.fenetreJeu.setImageDD(new ImageIcon());
                this.deplacementTuyau = false;
                this.fenetreJeu.repaint();
            }
        }
    }
    
    // Renvoie le tuyauDeplace dans la reserve
    private void renvoyerDansReserve(int x, int y){
        boolean tuyauTrouve = false;
        int colonneReserve = 0;
        int ligneReserve = 0;
        
        // On trouve le tuyau correspondant dans la reserve au tuyau deplace
        for(ArrayList<TuyauReserve> ligne : this.fenetreJeu.getNiveauCourant().getTuyauxReserve()){
            for(TuyauReserve tuyauReserve : ligne) {
            
                if(tuyauReserve.getNom() == this.tuyauDeplace.getNom() 
                        && tuyauReserve.getRotation() == this.tuyauDeplace.getRotation()){ 
                    tuyauTrouve = true;
                    break;
                }
                
                colonneReserve = colonneReserve + 1;
            }
            if(tuyauTrouve) break;
            
            ligneReserve = ligneReserve + 1;
            colonneReserve = 0;
        }
        
        // On renvoie le tuyau dans la reserve en un mouvement rectiligne uniforme
        TuyauReserve tuyau = this.fenetreJeu.getNiveauCourant().getTuyauxReserve().get(ligneReserve).get(colonneReserve);
        
        int tuyauX = (colonneReserve + this.nbrCasesPlateauLargeur) * this.largeurCase;
        int tuyauY = ligneReserve * this.hauteurCase;
        
        System.out.println("X : " + x + ", Y : " + y 
                + ", tuyauX : " + tuyauX + ", tuyauY : " + tuyauY); // debug
        
        deplacerTuyau(x, y, tuyauX, tuyauY);
        
        // On augmente le nombre de tuyaux disponibles dans la reserve
        tuyau.augmenterNombre();
    }
    
    private void deplacerTuyau(int departX, int departY, int arriveeX, int arriveeY){
        
        int distanceX = arriveeX - departX;
        int distanceY = arriveeY - departY;
        
        int distance = (int) Math.sqrt(Math.pow(distanceX,2) + Math.pow(distanceY,2));
        int iterations = (int) (distance / this.pasDeDeplacement);
        
        System.out.println("distance X : " + distanceX + ", distance Y : " + distanceY 
                + ", distance totale : " + distance + ", nombre d'iterations : " + iterations); // debug
        
        for(int i = 0; i < iterations; i++ ) {
            
            /*try { Thread.sleep(1); }
            catch (InterruptedException exception) {
                System.err.println("Exception sleep (DDController) " + exception.getMessage());}
            */
            departX = departX + (int)(distanceX / iterations);
            departY = departY + (int)(distanceY / iterations);
            
            //System.out.println("iteration n " + i + ", x : " + x + ", y : " + y); // debug
            //System.out.println("deplacement x : " + (int)(distanceX / iterations) + ", deplacement y : " + (int)(distanceY / iterations)); // debug
            
            this.fenetreJeu.setXImageDD(departX);
            this.fenetreJeu.setYImageDD(departY);
            this.fenetreJeu.paintImmediately(0, 0, 750, 700);
        }
    }
}