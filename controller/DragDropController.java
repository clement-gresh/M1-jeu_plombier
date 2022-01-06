package projetIG.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import projetIG.controller.annulerRetablir.DeplacementPlaPlaAnnulable;
import projetIG.controller.annulerRetablir.DeplacementPlaReAnnulable;
import projetIG.controller.annulerRetablir.DeplacementRePlaAnnulable;
import projetIG.model.enumeration.CouleurTuyau;
import projetIG.model.enumeration.Rotation;
import projetIG.model.enumeration.TypeTuyau;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.Tuyau;
import projetIG.model.niveau.TuyauPlateau;
import projetIG.model.niveau.TuyauReserve;
import projetIG.view.FenetreJeu;
import projetIG.view.ModificationsImage;

public class DragDropController extends MouseAdapter {
    public static final int AUCUNE = 0;
    public static final int PLATEAU = 1;
    public static final int RESERVE = 2;
    
    protected FenetreJeu fenetreJeu;
    protected int hauteurCase;
    protected int largeurCase;
    protected int nbrCasesTotalHauteur;
    protected int nbrCasesTotalLargeur;
    protected int nbrCasesPlateauHauteur;
    protected int nbrCasesPlateauLargeur;
    protected boolean deplacementTuyau = false;
    protected Tuyau tuyauDeplace;
    protected int pasDeDeplacement = 10; // Represente le pas en pixel entre 2 images lors d'un mouvement
    
    protected Niveau niveau;
    protected AnnulerManager annulerManager;
    protected int zoneDepart = AUCUNE;
    protected int ligneDepart;
    protected int colonneDepart;
    protected int zoneArrive = AUCUNE;
    protected int ligneArrive;
    protected int colonneArrive;
    

    public DragDropController(FenetreJeu fenetreJeu) {
        this.fenetreJeu = fenetreJeu;
        this.annulerManager = fenetreJeu.getPanelParent().getPanelPlumber().getAnnulerManager();
        this.niveau = fenetreJeu.getNiveauCourant();
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
            this.colonneDepart = (int) Math.ceil( event.getX() / this.largeurCase ) ;
            this.ligneDepart = (int) Math.ceil( event.getY() / this.hauteurCase ) ;
            
            BufferedImage buffTuyauDD = new BufferedImage(this.largeurCase, this.largeurCase,
                                                          BufferedImage.TYPE_INT_ARGB);
            
            // Si le clic a eu lieu dans la reserve
            if(colonneDepart > this.nbrCasesTotalLargeur - 3 && ligneDepart < 6) {
                // On verifie que colonne ne depasse pas le nombre maximal (peut arriver a cause de 
                // l'imprecision sur le nombre de pixels lie au cast en int). On lui assigne le max sinon.
                if(colonneDepart > this.nbrCasesTotalLargeur - 1) colonneDepart = this.nbrCasesTotalLargeur - 1;
                
                colonneDepart = colonneDepart - (this.nbrCasesTotalLargeur - 2);
                
                TuyauReserve tuyauReserve = this.fenetreJeu.getNiveauCourant().getTuyauxReserve()
                                            .get(ligneDepart).get(colonneDepart);

                if(tuyauReserve.getNombre() > 0) {
                    tuyauReserve.diminuerNombre();

                    this.deplacementTuyau = true;
                    this.tuyauDeplace = tuyauReserve;
                    this.zoneDepart = RESERVE;
                }
            }
            
            
            // Sinon, si le clic a lieu sur le plateau de jeu
            else if (colonneDepart < this.nbrCasesPlateauLargeur && ligneDepart < this.nbrCasesPlateauHauteur) {
                
                TuyauPlateau tuyauPlateau = this.fenetreJeu.getNiveauCourant().getPlateauCourant().get(ligneDepart).get(colonneDepart);
                
                if(tuyauPlateau != null && !tuyauPlateau.isInamovible()){
                    this.deplacementTuyau = true;
                    this.tuyauDeplace = tuyauPlateau;
                    this.zoneDepart = PLATEAU;

                    // On enleve le tuyau du plateau
                    this.fenetreJeu.getNiveauCourant().getPlateauCourant().get(ligneDepart).set(colonneDepart, null);
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
                colonneArrive = (int) Math.ceil( event.getX() / this.largeurCase ) ;
                ligneArrive = (int) Math.ceil( event.getY() / this.hauteurCase ) ;
                
                // On determine l'abscisse et l'ordonnee du coin superieur gauche de l'image
                int departX = event.getX() - (int) (0.5 * this.largeurCase);
                int departY = event.getY() - (int) (0.5 * this.hauteurCase);
                
                // On verifie qu'on se trouve a l'interieur du plateau de jeu (sans les bordures)
                if( (colonneArrive > 0)
                        && (colonneArrive < this.nbrCasesPlateauLargeur - 1)
                        && (ligneArrive > 0)
                        && (ligneArrive < this.nbrCasesPlateauHauteur - 1)
                        ) {
                    
                    if(this.fenetreJeu.getNiveauCourant().getPlateauCourant().get(ligneArrive).get(colonneArrive) != null){
                        renvoyerDansReserve(departX, departY);
                    }
                    
                    else{
                        deplacementRectiligne(departX, departY, colonneArrive * this.largeurCase, ligneArrive * this.hauteurCase);
                        
                        this.fenetreJeu.getNiveauCourant().getPlateauCourant().get(ligneArrive).set(colonneArrive,
                                new TuyauPlateau(this.tuyauDeplace));
                        this.zoneArrive = PLATEAU;
                    }
                }

                else {
                    renvoyerDansReserve(departX, departY);
                }
            
            
                // On remet l'image Drag&Drop a vide dans la fenetre de jeu 
                this.fenetreJeu.setImageDD(new ImageIcon());
                this.deplacementTuyau = false;
                this.fenetreJeu.repaint();
                
                this.ajouterActionAnnulable();
                this.zoneDepart = AUCUNE;
                this.zoneArrive = AUCUNE;
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
        
        this.colonneArrive = colonneReserve;
        this.ligneArrive = ligneReserve;
        
        // On renvoie le tuyau dans la reserve en un mouvement rectiligne uniforme
        TuyauReserve tuyau = this.fenetreJeu.getNiveauCourant().getTuyauxReserve().get(ligneReserve).get(colonneReserve);
        
        int tuyauX = (colonneReserve + this.nbrCasesPlateauLargeur) * this.largeurCase;
        int tuyauY = ligneReserve * this.hauteurCase;
        
        //System.out.println("X : " + x + ", Y : " + y 
        //        + ", tuyauX : " + tuyauX + ", tuyauY : " + tuyauY); // debug
        
        deplacementRectiligne(x, y, tuyauX, tuyauY);
        
        // On augmente le nombre de tuyaux disponibles dans la reserve
        tuyau.augmenterNombre();
        this.zoneArrive = RESERVE;
    }
    
    
    // Deplacement rectiligne du tuyau d'un point de depart a un point d'arrivee
    private void deplacementRectiligne(int departX, int departY, int arriveeX, int arriveeY){
        
        int distanceX = arriveeX - departX;
        int distanceY = arriveeY - departY;
        
        int distance = (int) Math.sqrt(Math.pow(distanceX,2) + Math.pow(distanceY,2));
        int iterations = (int) (distance / this.pasDeDeplacement);
        
        //System.out.println("distance X : " + distanceX + ", distance Y : " + distanceY 
        //        + ", distance totale : " + distance + ", nombre d'iterations : " + iterations); // debug
        
        for(int i = 0; i < iterations; i++ ) {
            
            try { Thread.sleep(2); }
            catch (InterruptedException exception) {
                System.err.println("Exception fonction sleep (DragDropController) : " + exception.getMessage());}
            
            departX = departX + (int)(distanceX / iterations);
            departY = departY + (int)(distanceY / iterations);
            
            //System.out.println("iteration n " + i + ", x : " + departX + ", y : " + departY); // debug
            //System.out.println("deplacement x : " + (int)(distanceX / iterations) + ", deplacement y : " + (int)(distanceY / iterations)); // debug
            
            this.fenetreJeu.setXImageDD(departX);
            this.fenetreJeu.setYImageDD(departY);
            this.fenetreJeu.paintImmediately(0, 0, this.fenetreJeu.getPanelParent().getTaillePixelLargeur(),
                                                  this.fenetreJeu.getPanelParent().getTaillePixelHauteur());
        }
    }
    
    
    private void ajouterActionAnnulable(){
        if(this.zoneDepart == PLATEAU && this.zoneArrive == RESERVE){
            // debug
            /*
            System.out.println("");
            System.out.println("Depart : " + this.zoneDepart + " ligne " + this.ligneDepart + ", colonne " + this.colonneDepart);
            System.out.println("Arrivee  : " + this.zoneArrive + "  ligne " + this.ligneArrive + ", colonne " + this.colonneArrive);
            */
            this.annulerManager.addEdit(new DeplacementPlaReAnnulable(fenetreJeu, niveau,
                    tuyauDeplace, ligneDepart, colonneDepart, ligneArrive, colonneArrive));
        }
        else if(this.zoneDepart == RESERVE && this.zoneArrive == PLATEAU){
            this.annulerManager.addEdit(new DeplacementRePlaAnnulable(fenetreJeu, niveau,
                    tuyauDeplace, ligneDepart, colonneDepart, ligneArrive, colonneArrive));
        }
        else if(this.zoneDepart == PLATEAU && this.zoneArrive == PLATEAU){
            
            this.annulerManager.addEdit(new DeplacementPlaPlaAnnulable(fenetreJeu, niveau,
                    tuyauDeplace, ligneDepart, colonneDepart, ligneArrive, colonneArrive));
        }
    }
}