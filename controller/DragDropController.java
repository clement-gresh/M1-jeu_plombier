package projetIG.controller;

import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import projetIG.controller.annulerRetablir.PvPAnnulable;
import projetIG.controller.annulerRetablir.PvRAnnulable;
import projetIG.controller.annulerRetablir.RvPAnnulable;
import projetIG.model.enumeration.Couleur;
import static projetIG.model.enumeration.Dir.N;
import projetIG.model.enumeration.TypeTuyau;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.Tuyau;
import projetIG.model.niveau.TuyauPlateau;
import projetIG.model.niveau.TuyauReserve;
import projetIG.view.PanelJeu;
import projetIG.view.image.ModificationsImage;

public class DragDropController extends MouseAdapter {
    public static final int AUCUNE = 0;
    public static final int PLATEAU = 1;
    public static final int RESERVE = 2;
    
    public static final int PIXELS = 10; // Represente le pas en pixels entre 2 images lors d'un mouvement
    public static final int TEMPS = 2; // Represente le temps d'affichage en ms entre 2 images lors d'un mouvement
    
    
    protected PanelJeu fenetreJeu;
    protected int hauteurCase;
    protected int largeurCase;
    protected int casesTotalHauteur;
    protected int casesTotalLargeur;
    protected int casesPlateauHauteur;
    protected int casesPlateauLargeur;
    protected boolean deplacement = false;
    protected Tuyau tuyauDeplace;
    protected Niveau niveau;
    protected AnnulerManager annulerManager;
    protected int depart = AUCUNE;
    protected int lDepart;
    protected int cDepart;
    protected int arrivee = AUCUNE;
    protected int lArrivee;
    protected int cArrivee;
    

    public DragDropController(PanelJeu fenetreJeu) {
        this.fenetreJeu = fenetreJeu;
        this.annulerManager = fenetreJeu.getPanelPlombier().getAnnulerManager();
        this.niveau = fenetreJeu.getNiveauCourant();
    }
    
    @Override
    public void mousePressed(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            
            this.hauteurCase = this.fenetreJeu.getHauteurCase();
            this.largeurCase = this.fenetreJeu.getLargeurCase();
            
            this.casesTotalHauteur = this.fenetreJeu.getNbrCasesTotalHauteur();
            this.casesTotalLargeur = this.fenetreJeu.getNbrCasesTotalLargeur();
            
            this.casesPlateauHauteur = this.fenetreJeu.getNiveauCourant().getHauteur();
            this.casesPlateauLargeur = this.fenetreJeu.getNiveauCourant().getLargeur();
                        
            // On determine la colonne et la ligne de la case où le clic a eu lieu ( entre 0 et (nbrCases - 1) )
            this.cDepart = (int) Math.ceil( event.getX() / this.largeurCase ) ;
            this.lDepart = (int) Math.ceil( event.getY() / this.hauteurCase ) ;
            
            BufferedImage buffTuyauDD = new BufferedImage(this.largeurCase, this.largeurCase,
                                                          BufferedImage.TYPE_INT_ARGB);
            
            // Si le clic a eu lieu dans la reserve
            if(cDepart > this.casesTotalLargeur - 3 && lDepart < 6) {
                // On verifie que colonne ne depasse pas le nombre maximal (peut arriver a cause de 
                // l'imprecision sur le nombre de pixels lie au cast en int). On lui assigne le max sinon.
                if(cDepart > this.casesTotalLargeur - 1) cDepart = this.casesTotalLargeur - 1;
                
                cDepart = cDepart - (this.casesTotalLargeur - 2);
                
                TuyauReserve tuyauReserve = this.fenetreJeu.getNiveauCourant().getReserve()
                                            .get(lDepart).get(cDepart);

                if(tuyauReserve.getNombre() > 0) {
                    tuyauReserve.diminuerNombre();

                    this.deplacement = true;
                    this.tuyauDeplace = tuyauReserve;
                    this.depart = RESERVE;
                }
            }
            
            
            // Sinon, si le clic a lieu sur le plateau de jeu
            else if (cDepart < this.casesPlateauLargeur && lDepart < this.casesPlateauHauteur) {
                
                TuyauPlateau tuyauPlateau = this.fenetreJeu.getNiveauCourant().getPlateau()[lDepart][cDepart];
                
                if(tuyauPlateau != null && !tuyauPlateau.isInamovible()){
                    this.deplacement = true;
                    this.tuyauDeplace = tuyauPlateau;
                    this.depart = PLATEAU;

                    // On enleve le tuyau du plateau
                    this.fenetreJeu.getNiveauCourant().getPlateau()[lDepart][cDepart] = null;
                }
            }
            
            
            // On recupere l'image du tuyau deplace
            if(this.deplacement){
                buffTuyauDD = this.fenetreJeu.getPipes().getSubimage(this.tuyauDeplace.getNom().ordinal() * (120 + 20),
                        Couleur.BLANC.ordinal() * (120 + 20),
                        120, 120);
                
                if(this.tuyauDeplace.getRotation() != N){
                    buffTuyauDD = ModificationsImage.pivoter(buffTuyauDD, tuyauDeplace.getRotation().ordinal());
                }
                
                else if(this.tuyauDeplace.getNom().equals(TypeTuyau.OVER)){
                    BufferedImage partieVerticale = this.fenetreJeu.getPipes().getSubimage((this.tuyauDeplace.getNom().ordinal() - 1) * (120 + 20),
                        Couleur.BLANC.ordinal() * (120 + 20), 120, 120);
                
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
            if(deplacement) {
                // On determine la colonne et la ligne de la case où le bouton a ete relache ( entre 0 et (nbrCases - 1) )
                cArrivee = (int) Math.ceil( event.getX() / this.largeurCase ) ;
                lArrivee = (int) Math.ceil( event.getY() / this.hauteurCase ) ;
                
                // On determine l'abscisse et l'ordonnee du coin superieur gauche de l'image
                int departX = event.getX() - (int) (0.5 * this.largeurCase);
                int departY = event.getY() - (int) (0.5 * this.hauteurCase);
                
                // On verifie qu'on se trouve a l'interieur du plateau de jeu (sans les bordures)
                if( (cArrivee > 0)
                        && (cArrivee < this.casesPlateauLargeur - 1)
                        && (lArrivee > 0)
                        && (lArrivee < this.casesPlateauHauteur - 1)
                        ) {
                    
                    if(this.fenetreJeu.getNiveauCourant().getPlateau()[lArrivee][cArrivee] != null){
                        renvoyerDansReserve(departX, departY);
                    }
                    
                    else{
                        deplacementRectiligne(departX, departY, cArrivee * this.largeurCase, lArrivee * this.hauteurCase);
                        
                        this.fenetreJeu.getNiveauCourant().getPlateau()[lArrivee][cArrivee] 
                                                            = new TuyauPlateau(this.tuyauDeplace);
                        this.arrivee = PLATEAU;
                    }
                }

                else {
                    renvoyerDansReserve(departX, departY);
                }
            
            
                // On remet l'image Drag&Drop a vide dans la fenetre de jeu 
                this.fenetreJeu.setImageDD(new ImageIcon());
                this.deplacement = false;
                this.fenetreJeu.repaint();
                
                this.ajouterActionAnnulable();
                this.depart = AUCUNE;
                this.arrivee = AUCUNE;
            }
        }
    }
    
    // Renvoie le tuyauDeplace dans la reserve
    private void renvoyerDansReserve(int x, int y){
        boolean tuyauTrouve = false;
        int colonneReserve = 0;
        int ligneReserve = 0;
        
        // On trouve le tuyau correspondant dans la reserve au tuyau deplace
        for(ArrayList<TuyauReserve> ligne : this.fenetreJeu.getNiveauCourant().getReserve()){
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
        
        this.cArrivee = colonneReserve;
        this.lArrivee = ligneReserve;
        
        // On renvoie le tuyau dans la reserve en un mouvement rectiligne uniforme
        TuyauReserve tuyau = this.fenetreJeu.getNiveauCourant().getReserve().get(ligneReserve).get(colonneReserve);
        
        int tuyauX = (colonneReserve + this.casesPlateauLargeur) * this.largeurCase;
        int tuyauY = ligneReserve * this.hauteurCase;
        
        //System.out.println("X : " + x + ", Y : " + y 
        //        + ", tuyauX : " + tuyauX + ", tuyauY : " + tuyauY); // debug
        
        deplacementRectiligne(x, y, tuyauX, tuyauY);
        
        // On augmente le nombre de tuyaux disponibles dans la reserve
        tuyau.augmenterNombre();
        this.arrivee = RESERVE;
    }
    
    
    // Deplacement rectiligne du tuyau d'un point de depart a un point d'arrivee
    private void deplacementRectiligne(int departX, int departY, int arriveeX, int arriveeY){
        
        int distanceX = arriveeX - departX;
        int distanceY = arriveeY - departY;
        
        int distance = (int) Math.sqrt(Math.pow(distanceX,2) + Math.pow(distanceY,2));
        int iterations = (int) (distance / this.PIXELS);
        
        //System.out.println("distance X : " + distanceX + ", distance Y : " + distanceY 
        //        + ", distance totale : " + distance + ", nombre d'iterations : " + iterations); // debug
        
        for(int i = 0; i < iterations; i++ ) {
            // HACK Linux (sleep) : force la communication entre la MV et le serveur X a chaque rafraichissement
            MouseInfo.getPointerInfo ();
            
            try { Thread.sleep(TEMPS); }
            catch (InterruptedException exception) {
                System.err.println("Exception fonction sleep (DragDropController) : " + exception.getMessage());}
            
            departX = departX + (int)(distanceX / iterations);
            departY = departY + (int)(distanceY / iterations);
            
            //System.out.println("iteration n " + i + ", x : " + departX + ", y : " + departY); // debug
            //System.out.println("deplacement x : " + (int)(distanceX / iterations) + ", deplacement y : " + (int)(distanceY / iterations)); // debug
            
            this.fenetreJeu.setXImageDD(departX);
            this.fenetreJeu.setYImageDD(departY);
            this.fenetreJeu.paintImmediately(0, 0, this.fenetreJeu.getTaillePixelLargeur(),
                                                  this.fenetreJeu.getTaillePixelHauteur());
        }
    }
    
    
    private void ajouterActionAnnulable(){
        if(this.depart == PLATEAU && this.arrivee == RESERVE){
            // debug
            /*
            System.out.println("");
            System.out.println("Depart : " + this.zoneDepart + " ligne " + this.ligneDepart + ", colonne " + this.colonneDepart);
            System.out.println("Arrivee  : " + this.zoneArrive + "  ligne " + this.ligneArrive + ", colonne " + this.colonneArrive);
            */
            this.annulerManager.addEdit(new PvRAnnulable(fenetreJeu, niveau,
                    tuyauDeplace, lDepart, cDepart, lArrivee, cArrivee));
        }
        else if(this.depart == RESERVE && this.arrivee == PLATEAU){
            this.annulerManager.addEdit(new RvPAnnulable(fenetreJeu, niveau,
                    tuyauDeplace, lDepart, cDepart, lArrivee, cArrivee));
        }
        else if(this.depart == PLATEAU && this.arrivee == PLATEAU){
            
            this.annulerManager.addEdit(new PvPAnnulable(fenetreJeu, niveau,
                    tuyauDeplace, lDepart, cDepart, lArrivee, cArrivee));
        }
    }
}