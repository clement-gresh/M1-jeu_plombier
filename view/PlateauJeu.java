package projetIG.view;

import java.awt.Color;
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
import projetIG.model.level.Tuyau;

public class PlateauJeu extends JComponent {
    // Ligne du tuyau dans pipes.gif en fonction de sa couleur
    public static final int TUYAU_BLANC = 0;
    public static final int TUYAU_NOIR = 5;
    
    protected PanelPlateauJeu panelParent;
    protected BufferedImage pipes = new BufferedImage(820, 960, BufferedImage.TYPE_INT_ARGB);
    protected BufferedImage imagePlateau = new BufferedImage(820, 960, BufferedImage.TYPE_INT_ARGB);

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
        
        graphics2D.setColor(Color.BLUE);
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
        ArrayList<Tuyau> tuyauxDisplonibles = this.panelParent.getNiveauCourant().getTuyauxDisponibles();
        
        int colonneReserve = 0;
        int ligneReserve = 0;
        
        for(Tuyau tuyau : tuyauxDisplonibles) {
            // On met la couleur de fond de la case à marron fonce
            BufferedImage imgTemp = this.pipes.getSubimage(0, 6 * (120 + 20), 120, 120);
            
            graphics2D.drawImage(imgTemp,
                        abscisseReserve + largeurCase * colonneReserve,
                        hauteurCase * ligneReserve,
                        largeurCase, hauteurCase, this);
            
            
            // On determine le nom du tuyau et le nombre disponible
            String nom = tuyau.getNom();
            int nombreDisponible = tuyau.getNombre();
            
            int COULEUR = (nombreDisponible == 0) ? TUYAU_NOIR : TUYAU_BLANC;
            
            // On ajoute les tuyaux à la reserve
            if(nom.equals("C")){
                imgTemp = this.pipes.getSubimage(5 * (120 + 20), COULEUR * (120 + 20), 120, 120);
            }
            
            else if(tuyau.getNom().equals("O")){
                BufferedImage imgTemp1 = this.pipes.getSubimage(1 * (120 + 20), COULEUR * (120 + 20), 120, 120);
                BufferedImage imgTemp2 = this.pipes.getSubimage(2 * (120 + 20), COULEUR * (120 + 20), 120, 120);
                
                imgTemp = combiner(imgTemp1, imgTemp2);
            }
            
            else if(nom.startsWith("L")){
                int rotation = Integer.parseInt(nom.substring( Math.max(0, nom.length() - 1) ));
                imgTemp = this.pipes.getSubimage(1 * (120 + 20), COULEUR * (120 + 20), 120, 120);
                imgTemp = pivoter(imgTemp, rotation);
            }
            
            else if(nom.startsWith("T")){
                int rotation = Integer.parseInt(nom.substring( Math.max(0, nom.length() - 1) ));
                imgTemp = this.pipes.getSubimage(3 * (120 + 20), COULEUR * (120 + 20), 120, 120);
                imgTemp = pivoter(imgTemp, rotation);
            }
            
            else if(nom.startsWith("F")){
                int rotation = Integer.parseInt(nom.substring( Math.max(0, nom.length() - 1) ));
                imgTemp = this.pipes.getSubimage(4 * (120 + 20), COULEUR * (120 + 20), 120, 120);
                imgTemp = pivoter(imgTemp, rotation);
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
        ArrayList<ArrayList<String>> plateauGagnant = this.panelParent.getNiveauCourant().getPlateauGagnant();
        
        int colonne = 0;
        int ligne = 0;
        
        for(ArrayList<String> lignePlateau : plateauGagnant){
            for(String casePlateau : lignePlateau) {
                BufferedImage imgTemp = this.pipes.getSubimage(280, 140, 120, 120);
                graphics2D.drawImage(imgTemp,
                        largeurCase * colonne, hauteurCase * ligne,
                        largeurCase, hauteurCase, this);
                
                colonne = colonne + 1;
            }
            
            ligne = ligne + 1;
            colonne = 0;
        }
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
