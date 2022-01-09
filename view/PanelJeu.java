package projetIG.view;

import projetIG.view.image.ImageUtils;
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
import projetIG.model.niveau.TuyauP;
import projetIG.model.niveau.TuyauR;
import static projetIG.model.enumeration.Dir.N;
import static projetIG.model.enumeration.Dir.O;
import static projetIG.model.enumeration.Dir.S;
import projetIG.model.niveau.ParserNiveau;
import static projetIG.model.niveau.ParserNiveau.L_RESERVE;
import static projetIG.model.niveau.ParserNiveau.H_RESERVE;

public class PanelJeu extends JPanel {
    // Attributs statiques
    public static final int CASE = 6;
    public static final int MARRON = 0;
    public static final int COIN = 3;
    public static final int BORDURE = 4;
    public static final int FIXE = 5;
    
    // Attributs non-statiques
    private Plombier plombier;
    private Niveau niveau;
    private BufferedImage pipes = new BufferedImage(820, 960,
                                                BufferedImage.TYPE_INT_ARGB);
    private BufferedImage arrierePlan;
    private int casesPlateauL;
    private int casesPlateauH;
    private int casesTotalL;
    private int casesTotalH;
    private int pixelsL;
    private int pixelsH = 700;
    private final int caseL;
    private final int caseH;
    private int abscisseR;
    private DragDropController dragDrop;
    private VictoireController victoireController;
    private int xDD = 0;
    private int yDD = 0;
    private ImageIcon imageDD = new ImageIcon();
    
    // Constructeur
    public PanelJeu(Plombier plombier, String chemin, String cheminImg) {
        this.plombier = plombier;
        //Creation du niveau
        this.niveau = ParserNiveau.parser(chemin);
        // Calcul de la taille de la fenetre de jeu (en cases et en pixels)
        this.casesPlateauL = this.niveau.getLargeur();
        this.casesPlateauH = this.niveau.getHauteur();
        this.casesTotalL = this.casesPlateauL + L_RESERVE;
        this.casesTotalH = Integer.max(this.casesPlateauH, H_RESERVE);
        this.pixelsL =(int)(this.pixelsH * this.casesTotalL / this.casesTotalH);
        this.setPreferredSize(new Dimension(this.pixelsL, this.pixelsH));
        this.setLayout(new BorderLayout(10, 10));
              
        //Ajout du controller Drag&Drop sur la fenetre de jeu
        dragDrop = new DragDropController(this);
        this.addMouseListener(dragDrop);
        this.addMouseMotionListener(dragDrop);
        //Ajout du controller Couleurs & Victoire sur la fenetre de jeu
        victoireController = new VictoireController(this, this.niveau);
        this.addMouseListener(victoireController);
        
        // On recupere le gif contenant les images des tuyaux
        try { this.pipes = ImageIO.read(new File(cheminImg + "pipes.gif")); }
        catch (IOException exception) { System.err.println("Erreur importation"
                                   + "pipes.gif : " + exception.getMessage());}
        // On determine la taille d'une case en pixel
        this.caseL = (int)  (pixelsL / this.casesTotalL);
        this.caseH = (int)  (pixelsH / this.casesTotalH);
        
        // On construit l'image d'arriere plan
        arrierePlan();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.drawImage(this.arrierePlan, 0, 0, this.plombier.getWidth(),
                      this.plombier.getHeight(), this);
        reserve(g2D);
        afficherTuyauxP(g2D);
        // On ajoute l'image Drag&Drop
        this.imageDD.paintIcon(this, g2D, this.xDD, this.yDD);
    }
    
    // CONSTRUCTION DE L'IMAGE DE FOND
    private void arrierePlan(){
        // Initialisation de l'image (taille, antialiasing et fond noir)
        this.arrierePlan = new BufferedImage(pixelsL, pixelsH,
                                             BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = (Graphics2D) this.arrierePlan.getGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setColor(BLACK);
        g2D.fillRect(0, 0, this.pixelsL, this.pixelsH);
        
        // PLATEAU
        // On affiche les coins, bordures et background des cases
        for(int l = 0; l < casesPlateauH; l ++){
            for(int c = 0; c < casesPlateauL; c ++){
                BufferedImage img = this.pipes.getSubimage(typeCase(l, c)
                               * (120 + 20), H_RESERVE * (120 + 20), 120, 120);
                Dir rotation = rotations(l, c);
                if(rotation != N)
                    img = ImageUtils.pivoter(img, rotation.ordinal());
                g2D.drawImage(img, this.caseL * c, this.caseH * l,
                              this.caseL, this.caseH, this);
            }
        }
        
        // RESERVE
        // On affiche les cases et les tuyaux en noir
        
        // Abscisse de la ligne verticale separant le plateau de la reserve
        this.abscisseR = this.pixelsL - L_RESERVE * this.caseL;
        
        for(int l = 0; l < H_RESERVE; l++){
            for(int c = 0; c < L_RESERVE; c++){
                TuyauR tuyau = this.niveau.getReserve()[l][c];
                // On ajoute le background de la case dans la reserve
                BufferedImage image = this.pipes.getSubimage(
                            MARRON * (120 + 20), CASE * (120 + 20), 120, 120);
                g2D.drawImage(image, this.abscisseR + this.caseL * c, 
                            this.caseH * l, this.caseL, this.caseH, this);
                // On ajoute les tuyaux en noir
                afficherTuyauR(g2D, tuyau, NOIR, c, l);
            }
        }
    }
    
    // Determine si une case du plateau est un COIN, une BORDURE ou une CASE
    private int typeCase(int l, int c){
        if((l == 0 || l == this.casesPlateauH - 1) 
            && (c==0 || c == this.casesPlateauL - 1)){
            return COIN;
        }
        else if((l == 0 || l == this.casesPlateauH - 1) 
            || (c==0 || c == this.casesPlateauL - 1)){
            return BORDURE;
        }
        else return MARRON;
    }
    
    // Determine le nombre de rotations necessaires de l'image pour un COIN,
    // une BORDURE ou une CASE
    private Dir rotations(int l, int c){
        // Pour les coins du plateau
        if(l == 0 && c == 0) return N;
        else if(l == 0 && c == this.casesPlateauL - 1) return E;
        else if(l == this.casesPlateauH - 1 && c == 0) return O;
        else if(l == this.casesPlateauH - 1 && c == this.casesPlateauL - 1)
            return S;
        
        // Pour les bords du plateau
        else if(l == 0) return N;
        else if(l == this.casesPlateauH - 1) return S;
        else if(c == 0) return O;
        else if(c == this.casesPlateauL - 1) return E;
        
        //Pour les autres cases du plateau
        else return N;
    }
    
    // CONSTRUCTION DE LA RESERVE
    private void reserve(Graphics2D g2D){
        for(int l = 0; l < H_RESERVE; l++){
            for(int c = 0; c < L_RESERVE; c++){
                // On affiche le nombre de tuyaux en bas a gauche des cases
                TuyauR tuyau = this.niveau.getReserve()[l][c];
                Font font = new Font("Arial", BOLD, 15);
                g2D.setFont(font);
                g2D.setColor(WHITE);
                g2D.drawString(String.valueOf(tuyau.getNombre()),
                                     this.abscisseR + this.caseL * c + 5,
                                     this.caseH * (l + 1) - 5);

                if(tuyau.getNombre() > 0){ 
                    afficherTuyauR(g2D, tuyau, BLANC, c, l);
                }
            }
        }
    }
    
    // AJOUT DES TUYAUX AU PLATEAU
    private void afficherTuyauxP(Graphics2D g2D){
        for(int l = 0; l < casesPlateauH; l++) {
            for(int c = 0; c < casesPlateauL; c++) {
                TuyauP tuyau = this.niveau.getPlateau()[l][c];
                
                if(tuyau != null) {
                    BufferedImage img;
                    // On ajoute l'indicateur des tuyaux inamovibles
                    if(tuyau.isFixe() && tuyau.getNom() != TypeTuyau.SOURCE){
                        img = this.pipes.getSubimage(FIXE * (120 + 20),
                                                 6 * (120 + 20), 120, 120);
                        // On affiche l'image sur le graphique
                        g2D.drawImage(img, this.caseL * c, this.caseH * l,
                                      this.caseL, this.caseH, this);
                    }
                    // On recupere l'image correspondant au tuyau
                    img = this.pipes.getSubimage(
                               tuyau.getNom().ordinal() * (120 + 20),
                               tuyau.getCouleur().get(0).ordinal() * (120 + 20),
                               120, 120);

                    if(tuyau.getNom() == TypeTuyau.OVER){
                        BufferedImage img2 = this.pipes.getSubimage(
                               TypeTuyau.LINE.ordinal() * (120 + 20),
                               tuyau.getCouleur().get(1).ordinal() * (120 + 20),
                               120, 120
                        );
                        img = ImageUtils.combiner(img2, img);
                    }
                    if(tuyau.getRotation() != N) 
                        img = ImageUtils.pivoter(img, 
                                                 tuyau.getRotation().ordinal());
                    g2D.drawImage(img, this.caseL * c, this.caseH * l,
                                  this.caseL, this.caseH, this);
                }
            }
        }
    }
    
    
    private void afficherTuyauR(Graphics2D g2D, TuyauR tuyau, Couleur couleur,
                                int colonneR, int ligneR){
        BufferedImage img = this.pipes.getSubimage(
                                    tuyau.getNom().ordinal() * (120 + 20),
                                    couleur.ordinal() * (120 + 20), 120, 120);

        if(tuyau.getNom() == TypeTuyau.OVER){
            BufferedImage partieVerticale = this.pipes.getSubimage(
                                    (tuyau.getNom().ordinal() - 1) * (120 + 20),
                                    couleur.ordinal() * (120 + 20), 120, 120
            );
            img = ImageUtils.combiner(partieVerticale, img);
        }
        if(tuyau.getRotation() != N)
                img = ImageUtils.pivoter(img, tuyau.getRotation().ordinal());
        g2D.drawImage(img, this.abscisseR + this.caseL * colonneR,
                      this.caseH * ligneR, this.caseL, this.caseH, this);
    }
    
    
    public void victoire(){
        // On met a jour la vue avant d'afficher une fenetre de dialogue
        this.paintImmediately(0, 0, pixelsL, pixelsH);
        
        // Passer au niveau suivant s'il existe
        if(this.plombier.isThereNextLevel()){
            int numBanque = this.plombier.getNumBanque();
            int numNiveau = this.plombier.getNumNiveau();
            int clicBouton = Plombier.confirmation(this.plombier.getFrame(),
                        "Victoire", "VICTOIRE ! Passer au niveau suivant ?");
            if(clicBouton == YES_OPTION)
                this.plombier.afficherNiveau(numBanque, numNiveau + 1);
        }
        // Revenir à l'accueil sinon
        else {
            int clicBouton = Plombier.confirmation(this.plombier.getFrame(),
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

    public int getCasesTotalL() {
        return casesTotalL;
    }

    public int getCasesTotalH() {
        return casesTotalH;
    }

    public int getPixelsL() {
        return pixelsL;
    }

    public int getPixelsH() {
        return pixelsH;
    }

    public int getCaseL() {
        return caseL;
    }

    public int getCaseH() {
        return caseH;
    }

    public DragDropController getDragDrop() {
        return dragDrop;
    }

    public VictoireController getVictoireController() {
        return victoireController;
    }
    

    
    // SETTERS
    public void setXDD(int xDD) {
        this.xDD = xDD;
    }

    public void setYDD(int yDD) {
        this.yDD = yDD;
    }

    public void setImageDD(ImageIcon imageDD) {
        this.imageDD = imageDD;
    }
}
