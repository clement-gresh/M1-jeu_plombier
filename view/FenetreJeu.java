package projetIG.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import projetIG.model.enumeration.CouleurTuyau;
import projetIG.model.enumeration.Rotation;
import projetIG.model.enumeration.TypeCase;
import projetIG.model.enumeration.TypeTuyau;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.TuyauPlateau;
import projetIG.model.niveau.TuyauReserve;
import static projetIG.view.PanelFenetreJeu.NBR_CASES_RESERVE_HAUTEUR;
import static projetIG.view.PanelFenetreJeu.NBR_CASES_RESERVE_LARGEUR;

public class FenetreJeu extends JComponent {   
    
    // Attributs
    protected PanelFenetreJeu panelParent;
    protected Niveau niveauCourant;
    protected BufferedImage pipes = new BufferedImage(820, 960, BufferedImage.TYPE_INT_ARGB);
    protected BufferedImage imageArrierePlan;
    protected int nbrCasesTotalLargeur;
    protected int nbrCasesTotalHauteur;
    protected int largeurCase;
    protected int hauteurCase;
    protected int xImageDD = 0;
    protected int yImageDD = 0;
    protected ImageIcon imageDD = new ImageIcon();

    
    // Constructeur
    public FenetreJeu(PanelFenetreJeu panelParent, Niveau niveau) {
        this.panelParent = panelParent;
        this.niveauCourant = niveau;
        this.imageArrierePlan = new BufferedImage(panelParent.getTaillePixelLargeur(),
                                                  panelParent.getTaillePixelHauteur(),
                                                  BufferedImage.TYPE_INT_ARGB);
        
        try { this.pipes = ImageIO.read(new File("src/main/java/projetIG/view/image/pipes.gif")); }
        catch (IOException exception) { System.err.println("Erreur importation pipes.gif : " + exception.getMessage());}
        
        tailleCase();
        
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
        
        tailleCase();
        
        Graphics2D graphics2DIAP = (Graphics2D) this.imageArrierePlan.getGraphics();
        graphics2DIAP.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
        construireArrierePlan(graphics2DIAP);
        graphics2D.drawImage(this.imageArrierePlan, 0, 0, this.panelParent.getWidth(),
                             this.panelParent.getHeight(), this);
        
        construireReserve(graphics2D);
        
        afficherTuyauxPlateau(graphics2D);
        
        // On ajoute l'image Drag&Drop
        this.imageDD.paintIcon(this, graphics2D, this.xImageDD, this.yImageDD);
    }
    
    
    // On determine le nombre de cases en hauteur et en largeur
    private void tailleCase(){
        int nbrCasesPlateauLargeur = this.niveauCourant.getNbrCasesPlateauLargeur();
        int nbrCasesPlateauHauteur = this.niveauCourant.getNbrCasesPlateauHauteur();
        
        
        this.nbrCasesTotalLargeur = nbrCasesPlateauLargeur + NBR_CASES_RESERVE_LARGEUR;
        this.nbrCasesTotalHauteur = Integer.max(nbrCasesPlateauHauteur, NBR_CASES_RESERVE_HAUTEUR);
        
        // On determine la taille d'une case en pixel
        this.largeurCase = (int)  (this.panelParent.getWidth() / this.nbrCasesTotalLargeur);
        this.hauteurCase = (int)  (this.panelParent.getHeight() / this.nbrCasesTotalHauteur);
    }
    
    
    // CONSTRUCTION DE L'IMAGE DE FOND
    private void construireArrierePlan(Graphics2D graphics2D){
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        
        // PLATEAU
        // On affiche les coins, bordures et background des cases
        int nbrCasesPlateauLargeur = this.niveauCourant.getNbrCasesPlateauLargeur();
        int nbrCasesPlateauHauteur = this.niveauCourant.getNbrCasesPlateauHauteur();
        
        for(int lignePlateau = 0; lignePlateau < this.niveauCourant.getNbrCasesPlateauHauteur(); lignePlateau ++){
            for(int colonnePlateau = 0; colonnePlateau < this.niveauCourant.getNbrCasesPlateauLargeur(); colonnePlateau ++){
                
                BufferedImage imgTemp = this.pipes.getSubimage(
                        TypeCase.typeCase(lignePlateau, colonnePlateau, nbrCasesPlateauLargeur, nbrCasesPlateauHauteur) 
                            * (120 + 20),
                        6 * (120 + 20),
                        120, 120);
                
                Rotation nombreRotation = Rotation.nombreRotationsCase( lignePlateau, colonnePlateau, 
                                                                        nbrCasesPlateauLargeur, nbrCasesPlateauHauteur);
                if(nombreRotation != Rotation.PAS_DE_ROTATION) 
                    imgTemp = ModificationsImage.pivoter(imgTemp, nombreRotation.ordinal());
                
                graphics2D.drawImage(imgTemp,
                        this.largeurCase * colonnePlateau, this.hauteurCase * lignePlateau,
                        this.largeurCase, this.hauteurCase, this);
            }
        }
        
        
        // RESERVE
        // On affiche les cases et les tuyaux en noir
        // Abscisse de la ligne verticale separant le plateau de la reserve
        int abscisseReserve = this.panelParent.getWidth() - NBR_CASES_RESERVE_LARGEUR * this.largeurCase;
        
        int colonneReserve = 0;
        int ligneReserve = 0;
        
        
        for(ArrayList<TuyauReserve> ligne : this.niveauCourant.getTuyauxReserve()) {
            for(TuyauReserve tuyauReserve : ligne) {
                
                // On ajoute le background de la case dans la reserve (i.e. un carre marron fonce)
                BufferedImage image = this.pipes.getSubimage(
                         TypeCase.MARRON_FONCE.ordinal() * (120 + 20),
                         TypeTuyau.NOT_A_PIPE.ordinal() * (120 + 20), 120, 120);

                graphics2D.drawImage(image,
                            abscisseReserve + this.largeurCase * colonneReserve,
                            this.hauteurCase * ligneReserve,
                            this.largeurCase, this.hauteurCase, this);
                
                
                // On ajoute les tuyaux en noir
                dessinerTuyauReserve(graphics2D, tuyauReserve, CouleurTuyau.NOIR,
                                     abscisseReserve, colonneReserve, ligneReserve);
                
                colonneReserve = colonneReserve + 1;
            }
            
            colonneReserve = 0;
            ligneReserve = ligneReserve + 1;
        }
    }
    
    
    // CONSTRUCTION DE LA RESERVE
    private void construireReserve(Graphics2D graphics2D){
        // Abscisse de la ligne verticale separant le plateau de la reserve
        int abscisseReserve = this.panelParent.getWidth() - NBR_CASES_RESERVE_LARGEUR * this.largeurCase;
        
        int colonneReserve = 0;
        int ligneReserve = 0;
        
        for(ArrayList<TuyauReserve> ligne : this.niveauCourant.getTuyauxReserve()) {
            for(TuyauReserve tuyauReserve : ligne) {
                
                // On affiche en bas a gauche de chaque case le nombre de tuyaux correspondant disponibles
                Font font = new Font("Arial", Font.BOLD, 15);
                graphics2D.setFont(font);
                graphics2D.setColor(Color.WHITE);

                graphics2D.drawString(String.valueOf(tuyauReserve.getNombre()),
                            abscisseReserve + this.largeurCase * colonneReserve + 5,
                            this.hauteurCase * (ligneReserve + 1) - 5);

                if(tuyauReserve.getNombre() > 0){
                    dessinerTuyauReserve(graphics2D, tuyauReserve, CouleurTuyau.BLANC,
                                         abscisseReserve, colonneReserve, ligneReserve);
                }

                colonneReserve = colonneReserve + 1;
            }
            
            colonneReserve = 0;
            ligneReserve = ligneReserve + 1;
        }
    }
    
    
    // AJOUT DES TUYAUX AU PLATEAU
    private void afficherTuyauxPlateau(Graphics2D graphics2D){
        int colonnePlateau = 0;
        int lignePlateau = 0;
        
        for(ArrayList<TuyauPlateau> ligne : this.niveauCourant.getPlateauCourant()) {
            for(TuyauPlateau tuyauPlateau : ligne) {
                if(tuyauPlateau != null) {
                    BufferedImage imgTemp;

                    // On ajoute l'indicateur pour les tuyaux inamovibles
                    if(tuyauPlateau.isInamovible() && tuyauPlateau.getNom() != TypeTuyau.SOURCE){
                        imgTemp = this.pipes.getSubimage(
                                            TypeCase.FIXE.ordinal() * (120 + 20),
                                            CouleurTuyau.PAS_UNE_COULEUR.ordinal() * (120 + 20),
                                            120, 120);

                        // On affiche l'image sur le graphique a l'endroit et la taille voulue
                        graphics2D.drawImage(imgTemp,
                            this.largeurCase * colonnePlateau,
                            this.hauteurCase * lignePlateau,
                            this.largeurCase, this.hauteurCase, this);
                    }

                    // On recupere l'image correspondant au tuyau
                    imgTemp = this.pipes.getSubimage(
                                    tuyauPlateau.getNom().ordinal() * (120 + 20),
                                    tuyauPlateau.getCouleur().get(0).ordinal() * (120 + 20),
                                    120, 120);


                    if(tuyauPlateau.getNom() == TypeTuyau.OVER){
                        BufferedImage imgTemp2 = this.pipes.getSubimage(
                                TypeTuyau.LINE.ordinal() * (120 + 20),
                                tuyauPlateau.getCouleur().get(1).ordinal() * (120 + 20),
                                120, 120);

                        imgTemp = ModificationsImage.combiner(imgTemp2, imgTemp);
                    }


                    // On pivote l'image si necessaire
                    if(tuyauPlateau.getRotation() != Rotation.PAS_DE_ROTATION) 
                        imgTemp = ModificationsImage.pivoter(imgTemp, tuyauPlateau.getRotation().ordinal());

                    // On affiche l'image sur le graphique a l'endroit et la taille voulue
                    graphics2D.drawImage(imgTemp,
                        this.largeurCase * colonnePlateau, this.hauteurCase * lignePlateau,
                        this.largeurCase, this.hauteurCase, this);
                }
                
                colonnePlateau = colonnePlateau + 1;
            }
            
            colonnePlateau = 0;
            lignePlateau = lignePlateau + 1;
        }
    }
    
    
    
    private void dessinerTuyauReserve(Graphics2D graphics2D, TuyauReserve tuyauReserve, CouleurTuyau couleur,
                                        int abscisseReserve, int colonneReserve, int ligneReserve){
        
        BufferedImage image = this.pipes.getSubimage(
                tuyauReserve.getNom().ordinal() * (120 + 20),
                couleur.ordinal() * (120 + 20), 120, 120);

        if(tuyauReserve.getNom() == TypeTuyau.OVER){
            BufferedImage partieVerticale = this.pipes.getSubimage(
                    (tuyauReserve.getNom().ordinal() - 1) * (120 + 20),
                    couleur.ordinal() * (120 + 20), 120, 120);

            image = ModificationsImage.combiner(partieVerticale, image);
        }

        if(tuyauReserve.getRotation() != Rotation.PAS_DE_ROTATION)
                image = ModificationsImage.pivoter(image, tuyauReserve.getRotation().ordinal());


        graphics2D.drawImage(image,
                    abscisseReserve + this.largeurCase * colonneReserve,
                    this.hauteurCase * ligneReserve,
                    this.largeurCase, this.hauteurCase, this);
    }
    
    
    
    
    // GETTERS
    public PanelFenetreJeu getPanelParent() {
        return panelParent;
    }

    public Niveau getNiveauCourant() {
        return niveauCourant;
    }

    public BufferedImage getPipes() {
        return pipes;
    }

    public int getNbrCasesTotalLargeur() {
        return nbrCasesTotalLargeur;
    }

    public int getNbrCasesTotalHauteur() {
        return nbrCasesTotalHauteur;
    }

    public int getLargeurCase() {
        return largeurCase;
    }

    public int getHauteurCase() {
        return hauteurCase;
    }

    
    // SETTERS
    public void setXImageDD(int xImageDD) {
        this.xImageDD = xImageDD;
    }

    public void setYImageDD(int yImageDD) {
        this.yImageDD = yImageDD;
    }

    public void setImageDD(ImageIcon imageDD) {
        this.imageDD = imageDD;
    }
}
