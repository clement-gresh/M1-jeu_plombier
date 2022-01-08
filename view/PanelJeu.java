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
import static projetIG.model.niveau.ParserNiveau.HAUTEUR_RESERVE;
import static projetIG.model.niveau.ParserNiveau.LARGEUR_RESERVE;

public class PanelJeu extends JPanel {
    // Attributs statiques
    public static final int CASE = 6;
    public static final int MARRON = 0;
    public static final int COIN = 3;
    public static final int BORDURE = 4;
    public static final int FIXE = 5;
    
    
    
    // Attributs non-statiques
    protected Plombier plombier;
    protected Niveau niveau;
    protected BufferedImage pipes = new BufferedImage(820, 960, BufferedImage.TYPE_INT_ARGB);
    protected BufferedImage arrierePlan;
    
    protected int casesPlateauLargeur;
    protected int casesPlateauHauteur;
    protected int casesTotalLargeur;
    protected int casesTotalHauteur;
    protected int pixelsLargeur;
    protected int pixelsHauteur = 700;
    protected int largeurCase;
    protected int hauteurCase;
    
    protected DragDropController dragDrop;
    protected VictoireController victoireController;
    protected int xImageDD = 0;
    protected int yImageDD = 0;
    protected ImageIcon imageDD = new ImageIcon();

    
    // Constructeur
    public PanelJeu(Plombier panelPlombier, String cheminNiveau) {
        this.plombier = panelPlombier;
        
        //Creation du niveau
        this.niveau = ParserNiveau.parserNiveau(cheminNiveau);
        
        // Calcul de la taille de la fenetre de jeu (en cases et en pixels)
        this.casesPlateauLargeur = this.niveau.getLargeur();
        this.casesPlateauHauteur = this.niveau.getHauteur();
        this.casesTotalLargeur = this.casesPlateauLargeur + LARGEUR_RESERVE;
        this.casesTotalHauteur = Integer.max(this.casesPlateauHauteur, HAUTEUR_RESERVE);
        this.pixelsLargeur = (int) (this.pixelsHauteur * this.casesTotalLargeur / this.casesTotalHauteur);
        
        this.setPreferredSize(new Dimension(this.pixelsLargeur, this.pixelsHauteur)); // largeur, hauteur
        this.setLayout(new BorderLayout(10, 10));
        
              
        //Ajout du controller Drag&Drop sur la fenetre de jeu
        dragDrop = new DragDropController(this);
        this.addMouseListener(dragDrop);
        this.addMouseMotionListener(dragDrop);
        
        
        //Ajout du controller Couleurs & Victoire sur la fenetre de jeu
        victoireController = new VictoireController(this, this.niveau);
        
        this.addMouseListener(victoireController);
        
        // On recupere le gif contenant les images des tuyaux
        try { this.pipes = ImageIO.read(new File("src/main/java/projetIG/view/image/pipes.gif")); }
        catch (IOException exception) { System.err.println("Erreur importation pipes.gif : " + exception.getMessage());}
        
        
        // On determine la taille d'une case en pixel
        this.largeurCase = (int)  (pixelsLargeur / this.casesTotalLargeur);
        this.hauteurCase = (int)  (pixelsHauteur / this.casesTotalHauteur);
        
        
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
        
        graphics2D.drawImage(this.arrierePlan, 0, 0, this.plombier.getWidth(),
                             this.plombier.getHeight(), this);
        
        reserve(graphics2D);
        
        tuyauxPlateau(graphics2D);
        
        // On ajoute l'image Drag&Drop
        this.imageDD.paintIcon(this, graphics2D, this.xImageDD, this.yImageDD);
    }
    
    
    
    
    // CONSTRUCTION DE L'IMAGE DE FOND
    private void arrierePlan(){
        // Initialisation de l'image (taille, antialiasing et fond noir)
        this.arrierePlan = new BufferedImage(pixelsLargeur, pixelsHauteur,
                                                  BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D graphics2D = (Graphics2D) this.arrierePlan.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
        
        graphics2D.setColor(BLACK);
        graphics2D.fillRect(0, 0, this.pixelsLargeur, this.pixelsHauteur);
        
        
        
        // PLATEAU
        // On affiche les coins, bordures et background des cases
        for(int l = 0; l < casesPlateauHauteur; l ++){
            for(int c = 0; c < casesPlateauLargeur; c ++){
                
                BufferedImage img = this.pipes.getSubimage(
                            this.typeCase(l, c, casesPlateauLargeur, casesPlateauHauteur) * (120 + 20),
                            HAUTEUR_RESERVE * (120 + 20), 120, 120);
                
                Dir nombreRotation = this.nbrRotations(l, c, casesPlateauLargeur, casesPlateauHauteur);
                
                if(nombreRotation != N) img = ModificationsImage.pivoter(img, nombreRotation.ordinal());
                
                graphics2D.drawImage(img,
                            this.largeurCase * c, this.hauteurCase * l,
                            this.largeurCase, this.hauteurCase, this);
            }
        }
        
        
        // RESERVE
        // On affiche les cases et les tuyaux en noir
        
        // Abscisse de la ligne verticale separant le plateau de la reserve
        int abscisseReserve = this.pixelsLargeur - LARGEUR_RESERVE * this.largeurCase;
                
        
        for(int l = 0; l < HAUTEUR_RESERVE; l++){
            for(int c = 0; c < LARGEUR_RESERVE; c++){
                TuyauReserve tuyau = this.niveau.getReserve()[l][c];
                
                // On ajoute le background de la case dans la reserve (i.e. un carre marron fonce)
                BufferedImage image = this.pipes.getSubimage(MARRON * (120 + 20),
                                                             CASE * (120 + 20), 120, 120);

                graphics2D.drawImage(image, abscisseReserve + this.largeurCase * c, 
                                     this.hauteurCase * l, this.largeurCase, this.hauteurCase, this);
                
                // On ajoute les tuyaux en noir
                tuyauReserve(graphics2D, tuyau, NOIR, abscisseReserve, c, l);
            }
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
    private Dir nbrRotations(int ligne, int colonne, int casesLargeur, int casesHauteur){
        
        // Pour les coins du plateau
        if(ligne == 0 && colonne == 0) return N;
        else if(ligne == 0 && colonne == casesLargeur - 1) return E;
        else if(ligne == casesHauteur - 1 && colonne == 0) return O;
        else if(ligne == casesHauteur - 1 && colonne == casesLargeur - 1) return S;
        
        // Pour les bords du plateau
        else if(ligne == 0) return N;
        else if(ligne == casesHauteur - 1) return S;
        else if(colonne == 0) return O;
        else if(colonne == casesLargeur - 1) return E;
        
        //Pour les autres cases du plateau
        else return N;
    }
    
    // CONSTRUCTION DE LA RESERVE
    private void reserve(Graphics2D graphics2D){
        // Abscisse de la ligne verticale separant le plateau de la reserve
        int abscisseReserve = this.plombier.getWidth() - LARGEUR_RESERVE * this.largeurCase;
        
        for(int l = 0; l < HAUTEUR_RESERVE; l++){
            for(int c = 0; c < LARGEUR_RESERVE; c++){
                TuyauReserve tuyau = this.niveau.getReserve()[l][c];
                
                // On affiche en bas a gauche de chaque case le nombre de tuyaux correspondant disponibles
                Font font = new Font("Arial", BOLD, 15);
                graphics2D.setFont(font);
                graphics2D.setColor(WHITE);

                graphics2D.drawString(String.valueOf(tuyau.getNombre()),
                                     abscisseReserve + this.largeurCase * c + 5,
                                      this.hauteurCase * (l + 1) - 5);

                if(tuyau.getNombre() > 0){
                    tuyauReserve(graphics2D, tuyau, BLANC, abscisseReserve, c, l);
                }
            }
        }
    }
    
    
    // AJOUT DES TUYAUX AU PLATEAU
    private void tuyauxPlateau(Graphics2D graphics2D){
        
        for(int l = 0; l < casesPlateauHauteur; l++) {
            for(int c = 0; c < casesPlateauLargeur; c++) {
            
                TuyauPlateau tuyau = this.niveau.getPlateau()[l][c];
                
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
        this.paintImmediately(0, 0, pixelsLargeur, pixelsHauteur);


        // Passer au niveau suivant s'il existe
        if(this.plombier.isThereNextLevel()){
            
            int numeroBanque = this.plombier.getNumeroBanque();
            int numeroNiveau = this.plombier.getNumeroNiveau();

            int clicBouton = Plombier.fenetreConfirmation(this.plombier.getFrameParent(),
                                         "Victoire", "VICTOIRE ! Passer au niveau suivant ?");

            if(clicBouton == YES_OPTION) this.plombier.afficherNiveau(numeroBanque, numeroNiveau + 1);
        }


        // Revenir à l'accueil sinon
        else {
            int clicBouton = Plombier.fenetreConfirmation(this.plombier.getFrameParent(),
                                         "Victoire", "VICTOIRE ! Revenir à l'accueil ?");

            if(clicBouton == YES_OPTION) this.plombier.afficherPnlBanques();
        }
    }
    
    
    
    // GETTERS
    public Plombier getPlombier() {
        return plombier;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public BufferedImage getPipes() {
        return pipes;
    }

    public int getCasesTotalLargeur() {
        return casesTotalLargeur;
    }

    public int getCasesTotalHauteur() {
        return casesTotalHauteur;
    }

    public int getPixelsLargeur() {
        return pixelsLargeur;
    }

    public int getPixelsHauteur() {
        return pixelsHauteur;
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

    public VictoireController getVictoireController() {
        return victoireController;
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
