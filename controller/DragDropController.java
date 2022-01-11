package projetIG.controller;

import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import projetIG.controller.annulerRetablir.PvPAnnulable;
import projetIG.controller.annulerRetablir.PvRAnnulable;
import projetIG.controller.annulerRetablir.RvPAnnulable;
import projetIG.model.enumeration.Couleur;
import static projetIG.model.enumeration.Couleur.BLANC;
import projetIG.model.enumeration.Dir;
import static projetIG.model.enumeration.Dir.N;
import projetIG.model.enumeration.Type;
import static projetIG.model.enumeration.Type.SOURCE;
import static projetIG.model.enumeration.Type.TURN;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.Tuyau;
import projetIG.model.niveau.TuyauP;
import projetIG.model.niveau.TuyauR;
import projetIG.view.PanelJeu;
import projetIG.view.image.ImageUtils;
import static projetIG.model.niveau.ParserNiveau.L_RESERVE;
import static projetIG.model.niveau.ParserNiveau.H_RESERVE;

public class DragDropController extends MouseAdapter {
    public static final int AUCUNE = 0;
    public static final int PLATEAU = 1;
    public static final int RESERVE = 2;
    // Le pas en pixels entre 2 images lors d'un mouvement
    public static final int PIXELS = 10; 
    // Le temps d'affichage en ms entre 2 images lors d'un mouvement
    public static final int TEMPS = 2; 
    
    private final PanelJeu fenetreJeu;
    private final Niveau niveau;
    private final TuyauP[][] plateau;
    private final TuyauR[][] reserve;
    private int caseH;
    private int caseL;
    private int casesTotalL;
    private int casesPlateauH;
    private int casesPlateauL;
    private boolean deplacement = false;
    private Tuyau tuyauDeplace;
    private final AnnulerManager annulerManager;
    private int depart = AUCUNE;
    private int lDepart;
    private int cDepart;
    private int arrivee = AUCUNE;
    private int lArrivee;
    private int cArrivee;

    public DragDropController(PanelJeu fenetreJeu) {
        this.fenetreJeu = fenetreJeu;
        this.annulerManager = fenetreJeu.getPlombier().getAnnulerManager();
        this.niveau = fenetreJeu.getNiveau();
        this.plateau = fenetreJeu.getNiveau().getPlateau();
        this.reserve = fenetreJeu.getNiveau().getReserve();
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)){
            this.caseH = this.fenetreJeu.getCaseH();
            this.caseL = this.fenetreJeu.getCaseL();
            this.casesTotalL = this.fenetreJeu.getCasesTotalL();
            this.casesPlateauH = this.niveau.getHauteur();
            this.casesPlateauL = this.niveau.getLargeur();
            
            // On determine la colonne et la ligne où le clic a eu lieu
            this.cDepart = (int) Math.ceil(e.getX() / this.caseL ) ;
            this.lDepart = (int) Math.ceil(e.getY() / this.caseH ) ;
            Type type = SOURCE;
            Dir rotation = N;
            
            // Si le clic a eu lieu dans la reserve
            if(cDepart > this.casesTotalL - L_RESERVE - 1 &&
                    lDepart < H_RESERVE) {
                // On verifie que colonne ne depasse pas le nombre maximal
                // (peut arriver a cause de l'imprecision sur le nombre de 
                // pixels lie au cast en int). On lui assigne le max sinon.
                if(this.cDepart > this.casesTotalL - 1)
                    this.cDepart = this.casesTotalL - 1;
                this.cDepart = this.cDepart - (this.casesTotalL - L_RESERVE);
                TuyauR tuyauR = this.reserve[lDepart][cDepart];

                if(tuyauR.getNombre() > 0) {
                    type = tuyauR.getType();
                    rotation = tuyauR.getRotation();
                    tuyauR.diminuer();
                    this.deplacement = true;
                    this.tuyauDeplace = tuyauR;
                    this.depart = RESERVE;
                }
            }
            // Sinon, si le clic a lieu sur le plateau de jeu
            else if (cDepart < this.casesPlateauL 
                    && lDepart < this.casesPlateauH) {
                TuyauP tuyauP = this.plateau[lDepart][cDepart];
                
                if(tuyauP != null && !tuyauP.isFixe()){
                    type = tuyauP.getType();
                    rotation = tuyauP.getRotation();
                    this.deplacement = true;
                    this.tuyauDeplace = tuyauP;
                    this.depart = PLATEAU;
                    // On enleve le tuyau du plateau
                    this.plateau[lDepart][cDepart] = null;
                }
            }
            // On recupere l'image du tuyau deplace
            if(this.deplacement){
                BufferedImage imgDD = this.fenetreJeu.getPipes().getSubimage(
                                type.ordinal() * (120 + 20),
                                Couleur.BLANC.ordinal() * (120 + 20), 120, 120
                );
                // Pivote l'image si necessaire
                if(this.tuyauDeplace.getRotation() != N){
                    imgDD = ImageUtils.pivoter(imgDD, rotation.ordinal());
                }
                // Ajoute la 2eme composante pour un OVER
                else if(type.equals(Type.OVER)){
                    BufferedImage img2 = this.fenetreJeu.getPipes().getSubimage(
                                (type.ordinal() - 1) * (120 + 20),
                                BLANC.ordinal() * (120 + 20), 120, 120
                    );
                    imgDD = ImageUtils.combiner(img2, imgDD);
                }
                // On ajoute l'image Drag&Drop au plateau de jeu
                imgDD = ImageUtils.changerTaille(imgDD, this.caseL, this.caseH);
                this.fenetreJeu.setImageDD(new ImageIcon(imgDD));
                this.fenetreJeu.setXDD(e.getX() - (int) (0.5 * this.caseL));
                this.fenetreJeu.setYDD(e.getY() - (int) (0.5 * this.caseH));
                this.fenetreJeu.repaint();
            }
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)){
            // MAJ des coordonnes de l'image de DD dans la fenetre de jeu
            this.fenetreJeu.setXDD(e.getX() - (int) (0.5 * this.caseL));
            this.fenetreJeu.setYDD(e.getY() - (int) (0.5 * this.caseH));
            this.fenetreJeu.repaint();
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)){
            if(deplacement) {
                // On determine la colonne et la ligne où le bouton a ete lache
                cArrivee = (int) Math.ceil(e.getX() / this.caseL ) ;
                lArrivee = (int) Math.ceil(e.getY() / this.caseH ) ;
                // On determine l'abscisse et l'ordonnee du coin superieur
                // gauche de l'image
                int departX = e.getX() - (int) (0.5 * this.caseL);
                int departY = e.getY() - (int) (0.5 * this.caseH);
                
                // On verifie qu'on se trouve sur une case du plateau de jeu
                if( (cArrivee > 0)
                        && (cArrivee < this.casesPlateauL - 1)
                        && (lArrivee > 0)
                        && (lArrivee < this.casesPlateauH - 1)
                        ) {
                    if(this.plateau[lArrivee][cArrivee] != null){
                        renvoyerR(departX, departY);
                    }
                    else{
                        rectiligne(departX, departY, cArrivee * this.caseL,
                                   lArrivee * this.caseH);
                        this.plateau[lArrivee][cArrivee] 
                                    = new TuyauP(this.tuyauDeplace);
                        this.arrivee = PLATEAU;
                    }
                }
                else { renvoyerR(departX, departY); }
            
                // On remet l'image Drag&Drop a vide dans la fenetre de jeu 
                this.fenetreJeu.setImageDD(new ImageIcon());
                this.ajouterAnnulable();
                this.deplacement = false;
                this.tuyauDeplace = null;
                this.depart = AUCUNE;
                this.arrivee = AUCUNE;
                this.fenetreJeu.repaint();
            }
        }
    }
    
    // Renvoie le tuyauDeplace dans la reserve
    private void renvoyerR(int x, int y){
        boolean tuyauTrouve = false;
        int l;
        int c = 0;
        TuyauR tuyauR = new TuyauR(TURN, N);
        
        // On trouve dans la reserve le tuyau correspondant au tuyau deplace
        for(l = 0; l < H_RESERVE; l++){
            for(c = 0; c < L_RESERVE; c++){
                tuyauR = this.reserve[l][c];
                
                if(tuyauR.getType() == this.tuyauDeplace.getType() 
                    && tuyauR.getRotation() == this.tuyauDeplace.getRotation())
                { 
                    tuyauTrouve = true;
                    break;
                }
            }
            if(tuyauTrouve){ break; }
        }
        this.cArrivee = c;
        this.lArrivee = l;
        // On renvoie le tuyau dans la reserve en un mouvement rectiligne
        int tuyauX = (c + this.casesPlateauL) * this.caseL;
        int tuyauY = l * this.caseH;
        rectiligne(x, y, tuyauX, tuyauY);
        tuyauR.augmenter();
        this.arrivee = RESERVE;
    } 
    
    // Deplacement rectiligne du tuyau entre 2 points
    private void rectiligne(int departX, int departY,
                            int arriveeX, int arriveeY){
        int dX = arriveeX - departX;
        int dY = arriveeY - departY;
        int d = (int) Math.sqrt(Math.pow(dX,2) + Math.pow(dY,2));
        int iterations = (int) (d / PIXELS);
        
        for(int i = 0; i < iterations; i++ ) {
            // HACK Linux (sleep) : force la communication entre la MV et le
            // serveur X a chaque rafraichissement
            MouseInfo.getPointerInfo ();
            
            try { Thread.sleep(TEMPS); }
            catch (InterruptedException exception) {
                System.err.println("Exception fonction sleep (DDController) : "
                        + exception.getMessage());
            }
            departX = departX + (int)(dX / iterations);
            departY = departY + (int)(dY / iterations);
            this.fenetreJeu.setXDD(departX);
            this.fenetreJeu.setYDD(departY);
            this.fenetreJeu.paintImmediately(0, 0, this.fenetreJeu.getPixelsL(),
                                             this.fenetreJeu.getPixelsH());
        }
    }
    
    private void ajouterAnnulable(){
        if(this.depart == PLATEAU && this.arrivee == RESERVE){
            this.annulerManager.addEdit(new PvRAnnulable(fenetreJeu, niveau,
                        tuyauDeplace, lDepart, cDepart, lArrivee, cArrivee));
        }
        else if(this.depart == RESERVE && this.arrivee == PLATEAU){
            this.annulerManager.addEdit(new RvPAnnulable(fenetreJeu, niveau,
                        tuyauDeplace, lDepart, cDepart, lArrivee, cArrivee));
        }
        // Deplacement d'un tuyau du plateau a une case differente de celui-ci
        else if(this.depart == PLATEAU && this.arrivee == PLATEAU
                && (cDepart != cArrivee || lDepart != lArrivee)){
            this.annulerManager.addEdit(new PvPAnnulable(fenetreJeu, niveau,
                        tuyauDeplace, lDepart, cDepart, lArrivee, cArrivee));
        }
    }
}