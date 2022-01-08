package projetIG.view;

import projetIG.view.image.ModificationsImage;
import java.awt.BorderLayout;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import java.awt.Dimension;
import java.awt.Font;
import static java.awt.Font.BOLD;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import static javax.swing.JOptionPane.YES_OPTION;
import javax.swing.JPanel;
import projetIG.Plombier;
import projetIG.controller.VictoireController;
import projetIG.controller.DragDropController;
import projetIG.model.enumeration.Couleur;
import static projetIG.model.enumeration.Couleur.BLANC;
import static projetIG.model.enumeration.Couleur.NOIR;
import projetIG.model.enumeration.Dir;
import static projetIG.model.enumeration.Dir.E;
import projetIG.model.enumeration.TypeTuyau;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.TuyauPlateau;
import projetIG.model.niveau.TuyauReserve;
import static projetIG.model.enumeration.Dir.N;
import static projetIG.model.enumeration.Dir.O;
import static projetIG.model.enumeration.Dir.S;
import projetIG.model.niveau.ParserNiveau;

public class PanelJeu extends JPanel {
    // Attributs statiques
    public static final int NBR_CASES_RESERVE_LARGEUR = 2;
    public static final int NBR_CASES_RESERVE_HAUTEUR = 6; 
    
    public static final int CASE = 6;
    public static final int MARRON = 0;
    public static final int COIN = 3;
    public static final int BORDURE = 4;
    public static final int FIXE = 5;
    
    
    
    // Attributs non-statiques
    protected Plombier panelPlombier;
    protected Niveau niveauCourant;
    protected BufferedImage pipes = new BufferedImage(820, 960, BufferedImage.TYPE_INT_ARGB);
    protected BufferedImage imageArrierePlan;
    
    protected int nbrCasesTotalLargeur;
    protected int nbrCasesTotalHauteur;
    protected int taillePixelLargeur;
    protected int taillePixelHauteur = 700;
    protected int largeurCase;
    protected int hauteurCase;
    
    protected DragDropController dragDrop;
    protected VictoireController couleurController;
    protected int xImageDD = 0;
    protected int yImageDD = 0;
    protected ImageIcon imageDD = new ImageIcon();

    
    // Constructeur
    public PanelJeu(Plombier panelPlombier, String cheminNiveau) {
        this.panelPlombier = panelPlombier;
        
        //Creation du niveau
        this.niveauCourant = ParserNiveau.parserNiveau(cheminNiveau);
        
        // Calcul de la taille de la fenetre de jeu (en cases et en pixels)
        this.nbrCasesTotalLargeur = this.niveauCourant.getLargeur() + NBR_CASES_RESERVE_LARGEUR;
        this.nbrCasesTotalHauteur = Integer.max(this.niveauCourant.getHauteur(), NBR_CASES_RESERVE_HAUTEUR);
        this.taillePixelLargeur = (int) (this.taillePixelHauteur * this.nbrCasesTotalLargeur / this.nbrCasesTotalHauteur);
        
        this.setPreferredSize(new Dimension(this.taillePixelLargeur, this.taillePixelHauteur)); // largeur, hauteur
        this.setLayout(new BorderLayout(10, 10));
        
              
        //Ajout du controller Drag&Drop sur la fenetre de jeu
        dragDrop = new DragDropController(this);
        this.addMouseListener(dragDrop);
        this.addMouseMotionListener(dragDrop);
        
        
        //Ajout du controller Couleurs & Victoire sur la fenetre de jeu
        couleurController = new VictoireController(this, this.niveauCourant);
        
        this.addMouseListener(couleurController);
        
        
        
        this.imageArrierePlan = new BufferedImage(taillePixelLargeur,
                                                  taillePixelHauteur,
                                                  BufferedImage.TYPE_INT_ARGB);
        
        // On recupere le gif contenant les images des tuyaux
        try { this.pipes = ImageIO.read(new File("src/main/java/projetIG/view/image/pipes.gif")); }
        catch (IOException exception) { System.err.println("Erreur importation pipes.gif : " + exception.getMessage());}
        
        
        // On determine la taille d'une case en pixel
        this.largeurCase = (int)  (taillePixelLargeur / this.nbrCasesTotalLargeur);
        this.hauteurCase = (int)  (taillePixelHauteur / this.nbrCasesTotalHauteur);
        
        
        // On construit l'image d'arriere plan
        arrierePlan();
        
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
        
        graphics2D.drawImage(this.imageArrierePlan, 0, 0, this.panelPlombier.getWidth(),
                             this.panelPlombier.getHeight(), this);
        
        reserve(graphics2D);
        
        tuyauxPlateau(graphics2D);
        
        // On ajoute l'image Drag&Drop
        this.imageDD.paintIcon(this, graphics2D, this.xImageDD, this.yImageDD);
    }
    
    
    
    
    // CONSTRUCTION DE L'IMAGE DE FOND
    private void arrierePlan(){
        
        Graphics2D graphics2D = (Graphics2D) this.imageArrierePlan.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
        
        graphics2D.setColor(BLACK);
        graphics2D.fillRect(0, 0, this.taillePixelLargeur, this.taillePixelHauteur);
        
        
        
        // PLATEAU
        // On affiche les coins, bordures et background des cases
        int nbrCasesPlateauLargeur = this.niveauCourant.getLargeur();
        int nbrCasesPlateauHauteur = this.niveauCourant.getHauteur();
        
        for(int lignePlateau = 0; lignePlateau < this.niveauCourant.getHauteur(); lignePlateau ++){
            for(int colonnePlateau = 0; colonnePlateau < this.niveauCourant.getLargeur(); colonnePlateau ++){
                
                BufferedImage imgTemp = this.pipes.getSubimage(
                        this.typeCase(lignePlateau, colonnePlateau, nbrCasesPlateauLargeur, nbrCasesPlateauHauteur) 
                            * (120 + 20),
                        6 * (120 + 20),
                        120, 120);
                
                Dir nombreRotation = this.nbrRotations( lignePlateau, colonnePlateau, 
                                                                        nbrCasesPlateauLargeur, nbrCasesPlateauHauteur);
                if(nombreRotation != N) 
                    imgTemp = ModificationsImage.pivoter(imgTemp, nombreRotation.ordinal());
                
                graphics2D.drawImage(imgTemp,
                        this.largeurCase * colonnePlateau, this.hauteurCase * lignePlateau,
                        this.largeurCase, this.hauteurCase, this);
            }
        }
        
        
        // RESERVE
        // On affiche les cases et les tuyaux en noir
        
        // Abscisse de la ligne verticale separant le plateau de la reserve
        int abscisseReserve = this.taillePixelLargeur - NBR_CASES_RESERVE_LARGEUR * this.largeurCase;
        
        int colonneReserve = 0;
        int ligneReserve = 0;
        
        
        for(ArrayList<TuyauReserve> ligne : this.niveauCourant.getReserve()) {
            for(TuyauReserve tuyauReserve : ligne) {
                
                // On ajoute le background de la case dans la reserve (i.e. un carre marron fonce)
                BufferedImage image = this.pipes.getSubimage(MARRON * (120 + 20),
                         CASE * (120 + 20), 120, 120);

                graphics2D.drawImage(image,
                            abscisseReserve + this.largeurCase * colonneReserve,
                            this.hauteurCase * ligneReserve,
                            this.largeurCase, this.hauteurCase, this);
                
                
                // On ajoute les tuyaux en noir
                tuyauReserve(graphics2D, tuyauReserve, NOIR,
                                     abscisseReserve, colonneReserve, ligneReserve);
                
                colonneReserve = colonneReserve + 1;
            }
            
            colonneReserve = 0;
            ligneReserve = ligneReserve + 1;
        }
    }
    
    
    
    // Determine si une case du plateau est un COIN, une BORDURE ou une CASE quelconque
    private int typeCase(int ligne, int colonne, int nbrCasesLargeur, int nbrCasesHauteur){
        
        if((ligne == 0 || ligne == nbrCasesHauteur - 1) 
            && (colonne==0 || colonne == nbrCasesLargeur - 1)){
            return COIN;
        }

        else if((ligne == 0 || ligne == nbrCasesHauteur - 1) 
            || (colonne==0 || colonne == nbrCasesLargeur - 1)){
            return BORDURE;
        }

        else return MARRON;
    }
    
    
    // Determine le nombre de rotations necessaires de l'image pour un COIN, une BORDURE ou une CASE quelconque
    private Dir nbrRotations(int ligne, int colonne, int nbrCasesLargeur, int nbrCasesHauteur){
        
        // Pour les coins du plateau
        if(ligne == 0 && colonne == 0) return N;
        else if(ligne == 0 && colonne == nbrCasesLargeur - 1) return E;
        else if(ligne == nbrCasesHauteur - 1 && colonne == 0) return O;
        else if(ligne == nbrCasesHauteur - 1 && colonne == nbrCasesLargeur - 1) return S;
        
        // Pour les bords du plateau
        else if(ligne == 0) return N;
        else if(ligne == nbrCasesHauteur - 1) return S;
        else if(colonne == 0) return O;
        else if(colonne == nbrCasesLargeur - 1) return E;
        
        //Pour les autres cases du plateau
        else return N;
    }
    
    // CONSTRUCTION DE LA RESERVE
    private void reserve(Graphics2D graphics2D){
        // Abscisse de la ligne verticale separant le plateau de la reserve
        int abscisseReserve = this.panelPlombier.getWidth() - NBR_CASES_RESERVE_LARGEUR * this.largeurCase;
        
        int colonneReserve = 0;
        int ligneReserve = 0;
        
        for(ArrayList<TuyauReserve> ligne : this.niveauCourant.getReserve()) {
            for(TuyauReserve tuyauReserve : ligne) {
                
                // On affiche en bas a gauche de chaque case le nombre de tuyaux correspondant disponibles
                Font font = new Font("Arial", BOLD, 15);
                graphics2D.setFont(font);
                graphics2D.setColor(WHITE);

                graphics2D.drawString(String.valueOf(tuyauReserve.getNombre()),
                            abscisseReserve + this.largeurCase * colonneReserve + 5,
                            this.hauteurCase * (ligneReserve + 1) - 5);

                if(tuyauReserve.getNombre() > 0){
                    tuyauReserve(graphics2D, tuyauReserve, BLANC,
                                         abscisseReserve, colonneReserve, ligneReserve);
                }

                colonneReserve = colonneReserve + 1;
            }
            
            colonneReserve = 0;
            ligneReserve = ligneReserve + 1;
        }
    }
    
    
    // AJOUT DES TUYAUX AU PLATEAU
    private void tuyauxPlateau(Graphics2D graphics2D){
        
        for(int l = 0; l < this.niveauCourant.getHauteur(); l++) {
            for(int c = 0; c < this.niveauCourant.getLargeur(); c++) {
            
                TuyauPlateau tuyau = this.niveauCourant.getPlateau()[l][c];
                
                if(tuyau != null) {
                    BufferedImage imgTemp;

                    // On ajoute l'indicateur pour les tuyaux inamovibles
                    if(tuyau.isInamovible() && tuyau.getNom() != TypeTuyau.SOURCE){
                        imgTemp = this.pipes.getSubimage(FIXE * (120 + 20),
                                            Couleur.PAS_UNE_COULEUR.ordinal() * (120 + 20),
                                            120, 120);

                        // On affiche l'image sur le graphique a l'endroit et la taille voulue
                        graphics2D.drawImage(imgTemp,
                            this.largeurCase * c,
                            this.hauteurCase * l,
                            this.largeurCase, this.hauteurCase, this);
                    }

                    // On recupere l'image correspondant au tuyau
                    imgTemp = this.pipes.getSubimage(tuyau.getNom().ordinal() * (120 + 20),
                                    tuyau.getCouleur().get(0).ordinal() * (120 + 20),
                                    120, 120);


                    if(tuyau.getNom() == TypeTuyau.OVER){
                        BufferedImage imgTemp2 = this.pipes.getSubimage(TypeTuyau.LINE.ordinal() * (120 + 20),
                                tuyau.getCouleur().get(1).ordinal() * (120 + 20),
                                120, 120);

                        imgTemp = ModificationsImage.combiner(imgTemp2, imgTemp);
                    }


                    // On pivote l'image si necessaire
                    if(tuyau.getRotation() != N) 
                        imgTemp = ModificationsImage.pivoter(imgTemp, tuyau.getRotation().ordinal());

                    // On affiche l'image sur le graphique a l'endroit et la taille voulue
                    graphics2D.drawImage(imgTemp,
                        this.largeurCase * c, this.hauteurCase * l,
                        this.largeurCase, this.hauteurCase, this);
                }
            }
        }
    }
    
    
    private void tuyauReserve(Graphics2D graphics2D, TuyauReserve tuyauReserve, Couleur couleur,
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

        if(tuyauReserve.getRotation() != N)
                image = ModificationsImage.pivoter(image, tuyauReserve.getRotation().ordinal());


        graphics2D.drawImage(image,
                    abscisseReserve + this.largeurCase * colonneReserve,
                    this.hauteurCase * ligneReserve,
                    this.largeurCase, this.hauteurCase, this);
    }
    
    
    public void victoire(){
        // On met a jour la vue avant d'afficher une fenetre de dialogue
        this.paintImmediately(0, 0, taillePixelLargeur, taillePixelHauteur);


        // Passer au niveau suivant s'il existe
        if(this.panelPlombier.isThereNextLevel()){
            
            int numeroBanque = this.panelPlombier.getNumeroBanque();
            int numeroNiveau = this.panelPlombier.getNumeroNiveau();

            int clicBouton = Plombier.fenetreConfirmation(this.panelPlombier.getFrameParent(),
                                         "Victoire", "VICTOIRE ! Passer au niveau suivant ?");

            if(clicBouton == YES_OPTION) this.panelPlombier.afficherNiveau(numeroBanque, numeroNiveau + 1);
        }


        // Revenir � l'accueil sinon
        else {
            int clicBouton = Plombier.fenetreConfirmation(this.panelPlombier.getFrameParent(),
                                         "Victoire", "VICTOIRE ! Revenir � l'accueil ?");

            if(clicBouton == YES_OPTION) this.panelPlombier.afficherPnlBanques();
        }
    }
    
    
    
    // GETTERS
    public Plombier getPanelPlombier() {
        return panelPlombier;
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

    public int getTaillePixelLargeur() {
        return taillePixelLargeur;
    }

    public void setTaillePixelLargeur(int taillePixelLargeur) {
        this.taillePixelLargeur = taillePixelLargeur;
    }

    public int getTaillePixelHauteur() {
        return taillePixelHauteur;
    }

    public void setTaillePixelHauteur(int taillePixelHauteur) {
        this.taillePixelHauteur = taillePixelHauteur;
    }

    public int getLargeurCase() {
        return largeurCase;
    }

    public int getHauteurCase() {
        return hauteurCase;
    }

    public DragDropController getDragDrop() {
        return dragDrop;
    }

    public VictoireController getCouleurController() {
        return couleurController;
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
