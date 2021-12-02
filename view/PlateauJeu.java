package projetIG.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import projetIG.model.niveau.Tuyau;

public class PlateauJeu extends JComponent {
    // Ligne du tuyau dans pipes.gif en fonction de sa couleur
    public static final int TUYAU_BLANC = 0;
    public static final int TUYAU_NOIR = 5;
    
    // Colonne de l'image dans pipes.gif
    public static final int CASE = 0;
    public static final int COIN = 3;
    public static final int BORDURE = 4;
    
    // Attributs
    protected PanelPlateauJeu panelParent;
    protected BufferedImage pipes = new BufferedImage(820, 960, BufferedImage.TYPE_INT_ARGB);
    protected BufferedImage imagePlateau = new BufferedImage(820, 960, BufferedImage.TYPE_INT_ARGB);

    // Constructeur
    public PlateauJeu(PanelPlateauJeu panelParent) {
        this.panelParent = panelParent;
        
        try {
            this.pipes = ImageIO.read(new File("src/main/java/projetIG/view/image/pipes.gif"));
        }
        catch (IOException e) {}
    }
    
    
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
        
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        
        // On determine le nombre de cases en hauteur et en largeur
        int casesPlateauLargeur = this.panelParent.getNiveauCourant().getLargeurPlateau();
        int casesPlateauHauteur = this.panelParent.getNiveauCourant().getHauteurPlateau();
        
        int casesReserveLargeur = 2;
        int casesReserveHauteur = 6; 
        
        int casesTotaleLargeur = casesPlateauLargeur + casesReserveLargeur;
        int casesTotaleHauteur = Integer.max(casesPlateauHauteur, casesReserveHauteur);
        
        // On determine la taille d'une case en pixel
        int largeurCase = (int)  (this.panelParent.getWidth() / casesTotaleLargeur);
        int hauteurCase = (int)  (this.panelParent.getHeight() / casesTotaleHauteur);
        
        
        // Abscisse de la ligne verticale separant le plateau de la reserve
        int abscisseReserve = this.panelParent.getWidth() - 2 * largeurCase;

        
        // Construction de la reserve
        ArrayList<Tuyau> tuyauxDisponibles = this.panelParent.getNiveauCourant().getTuyauxDisponibles();
        
        int colonneReserve = 0;
        int ligneReserve = 0;
                
        for(Tuyau tuyau : tuyauxDisponibles) {
            // Nombre de rotation du tuyau (en quarts de tour)
            int rotation = tuyau.getRotation();
            // Nombre de tuyaux correspondant disponibles dans la reserve
            int nombreDisponible = tuyau.getNombre();
            // Type du tuyau (i.e. la colonne correspondant dans pipes.gif)
            int typeTuyau = tuyau.getColonne();
            // Couleur du tuyau (noir ou blanc) en fonction de sa disponibilite
            int COULEUR = (nombreDisponible == 0) ? TUYAU_NOIR : TUYAU_BLANC;
            
            
            // On ajoute le background de la case dans la reserve (i.e. un carre marron fonce)
            BufferedImage imgTemp = this.pipes.getSubimage(0, 6 * (120 + 20), 120, 120);
            
            graphics2D.drawImage(imgTemp,
                        abscisseReserve + largeurCase * colonneReserve,
                        hauteurCase * ligneReserve,
                        largeurCase, hauteurCase, this);
            
            
            // On affiche en bas a gauche de chaque case le nombre de tuyaux correspondant disponibles
            Font font = new Font("Arial", Font.BOLD, 15);
            graphics2D.setFont(font);
            graphics2D.setColor(Color.WHITE);
            
            graphics2D.drawString(String.valueOf(nombreDisponible),
                        abscisseReserve + largeurCase * colonneReserve + 5,
                        hauteurCase * (ligneReserve + 1) - 5);
            
            
            // On ajoute les images des tuyaux à la reserve
            if(tuyau.getNom().startsWith("O")){
                BufferedImage imgTemp1 = this.pipes.getSubimage(1 * (120 + 20), COULEUR * (120 + 20), 120, 120);
                BufferedImage imgTemp2 = this.pipes.getSubimage(2 * (120 + 20), COULEUR * (120 + 20), 120, 120);
                
                imgTemp = combiner(imgTemp1, imgTemp2);
            }
            
            else{
                imgTemp = this.pipes.getSubimage(typeTuyau * (120 + 20), COULEUR * (120 + 20), 120, 120);
                if(rotation != 0) imgTemp = pivoter(imgTemp, rotation);
            }
            
            graphics2D.drawImage(imgTemp,
                        abscisseReserve + largeurCase * colonneReserve,
                        hauteurCase * ligneReserve,
                        largeurCase, hauteurCase, this);
            
            // On alterne entre les colonnes 0 et 1 de la reserve
            colonneReserve = (colonneReserve + 1) % 2;
            
            // On passe à la ligne suivante quand on revient à la colonne 0
            if(colonneReserve == 0) ligneReserve = ligneReserve + 1;
        }
        
        
        
        
        // Construction du plateau de jeu
        ArrayList<ArrayList<String>> plateauCourant = this.panelParent.getNiveauCourant().getPlateauCourant();
        
        int colonnePlateau = 0;
        int lignePlateau = 0;
        
        for(ArrayList<String> ligneP : plateauCourant){
            for(String casePlateau : ligneP) {
                
                BufferedImage imgTemp = this.pipes.getSubimage(
                        typeCase(lignePlateau, colonnePlateau) * (120 + 20),
                        6 * (120 + 20),
                        120, 120);
                
                if(nombreRotations(lignePlateau, colonnePlateau) != 0) 
                    imgTemp = pivoter(imgTemp, nombreRotations(lignePlateau, colonnePlateau));
                
                graphics2D.drawImage(imgTemp,
                        largeurCase * colonnePlateau, hauteurCase * lignePlateau,
                        largeurCase, hauteurCase, this);
                
                
                if(!casePlateau.equals("X")){
                    
                }
                
                colonnePlateau = colonnePlateau + 1;
            }
            
            // On passe a la ligne suivante et on revient à la colonne 0
            lignePlateau = lignePlateau + 1;
            colonnePlateau = 0;
        }
        System.out.println(""); //debug
        System.out.println(""); //debug
    }
    
    
    public int typeCase(int ligne, int colonne){
        int nbrCasesLargeur = this.panelParent.getNiveauCourant().getLargeurPlateau();
        int nbrCasesHauteur = this.panelParent.getNiveauCourant().getHauteurPlateau();
        
        if((ligne == 0 || ligne == nbrCasesHauteur - 1) 
            && (colonne==0 || colonne == nbrCasesLargeur - 1)){
            return COIN;
        }

        else if((ligne == 0 || ligne == nbrCasesHauteur - 1) 
            || (colonne==0 || colonne == nbrCasesLargeur - 1)){
            return BORDURE;
        }

        else return CASE;
    }
    
    
    public int nombreRotations(int ligne, int colonne){
        int nbrCasesLargeur = this.panelParent.getNiveauCourant().getLargeurPlateau();
        int nbrCasesHauteur = this.panelParent.getNiveauCourant().getHauteurPlateau();
        
        // Pour les coins du plateau
        if(ligne == 0 && colonne == 0) return 0;
        else if(ligne == 0 && colonne == nbrCasesLargeur - 1) return 1;
        else if(ligne == nbrCasesHauteur - 1 && colonne == 0) return 3;
        else if(ligne == nbrCasesHauteur - 1 && colonne == nbrCasesLargeur - 1) return 2;
        
        // Pour les bords du plateau
        else if(ligne == 0) return 0;
        else if(ligne == nbrCasesHauteur - 1) return 2;
        else if(colonne == 0) return 3;
        else if(colonne == nbrCasesLargeur - 1) return 1;
        
        //Pour les autres cases du plateau
        else return 0;
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
}
