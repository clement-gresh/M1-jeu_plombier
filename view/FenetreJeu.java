package projetIG.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import projetIG.model.CouleurTuyau;
import projetIG.model.Rotation;
import projetIG.model.TypeCase;
import projetIG.model.TypeTuyau;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.TuyauPlateau;
import projetIG.model.niveau.TuyauReserve;

public class FenetreJeu extends JComponent {   
    
    // Attributs
    protected PanelFenetreJeu panelParent;
    protected Niveau niveauCourant;
    protected BufferedImage pipes = new BufferedImage(820, 960, BufferedImage.TYPE_INT_ARGB);
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
        
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        tailleCase();
        
        
        construireReserve(graphics2D);
        
        construirePlateauVide(graphics2D);
        
        afficherTuyaux(graphics2D);
        
        // On ajoute l'image en Drag&Drop
        this.imageDD.paintIcon(this, graphics2D, this.xImageDD, this.yImageDD);
    }
    
    
    // On determine le nombre de cases en hauteur et en largeur
    private void tailleCase(){
        int nbrCasesPlateauLargeur = this.niveauCourant.getNbrCasesPlateauLargeur();
        int nbrCasesPlateauHauteur = this.niveauCourant.getNbrCasesPlateauHauteur();
        
        //Nombre de lignes et colonnes de la reserve (constant quel que soit le niveau)
        int nbrCasesReserveLargeur = 2;
        int nbrCasesReserveHauteur = 6; 
        
        this.nbrCasesTotalLargeur = nbrCasesPlateauLargeur + nbrCasesReserveLargeur;
        this.nbrCasesTotalHauteur = Integer.max(nbrCasesPlateauHauteur, nbrCasesReserveHauteur);
        
        // On determine la taille d'une case en pixel
        this.largeurCase = (int)  (this.panelParent.getWidth() / this.nbrCasesTotalLargeur);
        this.hauteurCase = (int)  (this.panelParent.getHeight() / this.nbrCasesTotalHauteur);
    }
    
    
    // CONSTRUCTION DE LA RESERVE
    private void construireReserve(Graphics2D graphics2D){
        
        // Abscisse de la ligne verticale separant le plateau de la reserve
        int abscisseReserve = this.panelParent.getWidth() - 2 * this.largeurCase;
        
        int colonneReserve = 0;
        int ligneReserve = 0;
        
        for(TuyauReserve tuyauReserve : this.niveauCourant.getTuyauxReserve()) {
             // On ajoute le background de la case dans la reserve (i.e. un carre marron fonce)
            BufferedImage imgTemp = this.pipes.getSubimage(
                     TypeCase.MARRON_FONCE.ordinal() * (120 + 20),
                     TypeTuyau.NOT_A_PIPE.ordinal() * (120 + 20), 120, 120);
            
            graphics2D.drawImage(imgTemp,
                        abscisseReserve + this.largeurCase * colonneReserve,
                        this.hauteurCase * ligneReserve,
                        this.largeurCase, this.hauteurCase, this);
            
            
            // On affiche en bas a gauche de chaque case le nombre de tuyaux correspondant disponibles
            Font font = new Font("Arial", Font.BOLD, 15);
            graphics2D.setFont(font);
            graphics2D.setColor(Color.WHITE);
            
            graphics2D.drawString(String.valueOf(tuyauReserve.getNombre()),
                        abscisseReserve + this.largeurCase * colonneReserve + 5,
                        this.hauteurCase * (ligneReserve + 1) - 5);
            
            
            
            // On ajoute les images des tuyaux à la reserve
            if(tuyauReserve.getNom() == TypeTuyau.OVER){
                
                BufferedImage imgTemp1 = this.pipes.getSubimage(
                        tuyauReserve.getNom().ordinal() * (120 + 20),
                        tuyauReserve.getCouleur().ordinal() * (120 + 20), 120, 120);
                
                BufferedImage imgTemp2 = this.pipes.getSubimage(
                        (tuyauReserve.getNom().ordinal() - 1) * (120 + 20),
                        tuyauReserve.getCouleur().ordinal() * (120 + 20), 120, 120);
                
                imgTemp = combiner(imgTemp2, imgTemp1);
            }
            
            else{
                imgTemp = this.pipes.getSubimage(
                        tuyauReserve.getNom().ordinal() * (120 + 20),
                        tuyauReserve.getCouleur().ordinal() * (120 + 20), 120, 120);
                if(tuyauReserve.getRotation() != Rotation.PAS_DE_ROTATION)
                    imgTemp = pivoter(imgTemp, tuyauReserve.getRotation().ordinal());
            }
            
            graphics2D.drawImage(imgTemp,
                        abscisseReserve + this.largeurCase * colonneReserve,
                        this.hauteurCase * ligneReserve,
                        this.largeurCase, this.hauteurCase, this);
            
            
            // On alterne entre les colonnes 0 et 1 de la reserve
            colonneReserve = (colonneReserve + 1) % 2;
            
            // On passe à la ligne suivante quand on revient à la colonne 0
            if(colonneReserve == 0) ligneReserve = ligneReserve + 1;
        }
    }
    
    
    
    // CONSTRUCTION DU PLATEAU DE JEU (sans les tuyaux)
    private void construirePlateauVide(Graphics2D graphics2D){
        for(int lignePlateau = 0; lignePlateau < this.niveauCourant.getNbrCasesPlateauHauteur(); lignePlateau ++){
            for(int colonnePlateau = 0; colonnePlateau < this.niveauCourant.getNbrCasesPlateauLargeur(); colonnePlateau ++){
                // On affiche les coins, bordures et background des cases
                BufferedImage imgTemp = this.pipes.getSubimage(
                        typeCase(lignePlateau, colonnePlateau) * (120 + 20),
                        6 * (120 + 20),
                        120, 120);
                
                Rotation nombreRotation = nombreRotations(lignePlateau, colonnePlateau);
                if(nombreRotation != Rotation.PAS_DE_ROTATION) 
                    imgTemp = pivoter(imgTemp, nombreRotation.ordinal());
                
                graphics2D.drawImage(imgTemp,
                        this.largeurCase * colonnePlateau, this.hauteurCase * lignePlateau,
                        this.largeurCase, this.hauteurCase, this);
            }
        }
    }
    
    // AJOUT DES TUYAUX AU PLATEAU
    private void afficherTuyaux(Graphics2D graphics2D){
        for(TuyauPlateau tuyauPlateau : this.niveauCourant.getPlateauCourant()){
            BufferedImage imgTemp;
            
            // On ajoute l'indicateur pour les tuyaux inamovibles
            if(tuyauPlateau.isInamovible() && tuyauPlateau.getNom() != TypeTuyau.SOURCE){
                imgTemp = this.pipes.getSubimage(
                                    TypeCase.FIXE.ordinal() * (120 + 20),
                                    CouleurTuyau.PAS_UNE_COULEUR.ordinal() * (120 + 20),
                                    120, 120);
                    
                // On affiche l'image sur le graphique a l'endroit et la taille voulue
                graphics2D.drawImage(imgTemp,
                    this.largeurCase * tuyauPlateau.getColonne(),
                    this.hauteurCase * tuyauPlateau.getLigne(),
                    this.largeurCase, this.hauteurCase, this);
            }
            
            // On recupere l'image correspondant au tuyau
            imgTemp = this.pipes.getSubimage(
                            tuyauPlateau.getNom().ordinal() * (120 + 20),
                            tuyauPlateau.getCouleur().ordinal() * (120 + 20),
                            120, 120);

            
            if(tuyauPlateau.getNom() == TypeTuyau.OVER){
                BufferedImage imgTemp2 = this.pipes.getSubimage(
                        TypeTuyau.LINE.ordinal() * (120 + 20),
                        tuyauPlateau.getCouleur().ordinal() * (120 + 20),
                        120, 120);

                imgTemp = combiner(imgTemp2, imgTemp);
            }
            
            
            // On pivote l'image si necessaire
            if(tuyauPlateau.getRotation() != Rotation.PAS_DE_ROTATION) 
                imgTemp = pivoter(imgTemp, tuyauPlateau.getRotation().ordinal());
            
            // On affiche l'image sur le graphique a l'endroit et la taille voulue
            graphics2D.drawImage(imgTemp,
                this.largeurCase * tuyauPlateau.getColonne(), this.hauteurCase * tuyauPlateau.getLigne(),
                this.largeurCase, this.hauteurCase, this);
        }
    }
    
    
    // Determine si une case du plateau est un COIN, une BORDURE ou une CASE quelconque
    public int typeCase(int ligne, int colonne){
        int nbrCasesLargeur = this.niveauCourant.getNbrCasesPlateauLargeur();
        int nbrCasesHauteur = this.niveauCourant.getNbrCasesPlateauHauteur();
        
        if((ligne == 0 || ligne == nbrCasesHauteur - 1) 
            && (colonne==0 || colonne == nbrCasesLargeur - 1)){
            return TypeCase.COIN.ordinal();
        }

        else if((ligne == 0 || ligne == nbrCasesHauteur - 1) 
            || (colonne==0 || colonne == nbrCasesLargeur - 1)){
            return TypeCase.BORDURE.ordinal();
        }

        else return TypeCase.MARRON_FONCE.ordinal();
    }
    
    
    // Determine le nombre de rotations necessaires de l'image pour un COIN, une BORDURE ou une CASE quelconque
    public Rotation nombreRotations(int ligne, int colonne){
        int nbrCasesLargeur = this.niveauCourant.getNbrCasesPlateauLargeur();
        int nbrCasesHauteur = this.niveauCourant.getNbrCasesPlateauHauteur();
        
        // Pour les coins du plateau
        if(ligne == 0 && colonne == 0) return Rotation.PAS_DE_ROTATION;
        else if(ligne == 0 && colonne == nbrCasesLargeur - 1) return Rotation.QUART_TOUR_HORAIRE;
        else if(ligne == nbrCasesHauteur - 1 && colonne == 0) return Rotation.QUART_TOUR_TRIGO;
        else if(ligne == nbrCasesHauteur - 1 && colonne == nbrCasesLargeur - 1) return Rotation.DEMI_TOUR;
        
        // Pour les bords du plateau
        else if(ligne == 0) return Rotation.PAS_DE_ROTATION;
        else if(ligne == nbrCasesHauteur - 1) return Rotation.DEMI_TOUR;
        else if(colonne == 0) return Rotation.QUART_TOUR_TRIGO;
        else if(colonne == nbrCasesLargeur - 1) return Rotation.QUART_TOUR_HORAIRE;
        
        //Pour les autres cases du plateau
        else return Rotation.PAS_DE_ROTATION;
    }
    
    public static BufferedImage pivoter(BufferedImage imgAPivoter, int quartsDeCercle) {
        int largeur = imgAPivoter.getWidth();
        int hauteur = imgAPivoter.getHeight();
        BufferedImage imgPivotee = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imgPivotee.createGraphics();

        AffineTransform transform = new AffineTransform();
        transform.rotate((Math.PI / 2) * quartsDeCercle, largeur / 2 , hauteur / 2);
        g2d.drawRenderedImage(imgAPivoter, transform);

        g2d.dispose();
        return imgPivotee;
    }
    
    
    public static BufferedImage combiner(BufferedImage img1, BufferedImage img2){
        int largeur = img1.getWidth();
        int hauteur = img1.getHeight();
        BufferedImage imgCombinee = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);

        // Ajoute les 2 images a imgCombinee
        Graphics g = imgCombinee.getGraphics();
        g.drawImage(img1, 0, 0, null);
        g.drawImage(img2, 0, 0, null);

        g.dispose();
        
        return imgCombinee;
    }
    
    public static BufferedImage modifierTaille(BufferedImage img, int nvlLargeur, int nvlHauteur) { 
        Image tmp = img.getScaledInstance(nvlLargeur, nvlHauteur, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(nvlLargeur, nvlHauteur, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
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
