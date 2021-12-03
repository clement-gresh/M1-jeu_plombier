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
import projetIG.model.CouleurTuyau;
import projetIG.model.TypeCase;
import projetIG.model.niveau.Tuyau;

public class PlateauJeu extends JComponent {    
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
        
        construireReserve(graphics2D, largeurCase, hauteurCase);
        
        construirePlateau(graphics2D, largeurCase, hauteurCase);
        
    }
    
    // CONSTRUCTION DE LA RESERVE
    private void construireReserve(Graphics2D graphics2D, int largeurCase, int hauteurCase){
        // Abscisse de la ligne verticale separant le plateau de la reserve
        int abscisseReserve = this.panelParent.getWidth() - 2 * largeurCase;
        
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
            int COULEUR = (nombreDisponible == 0) ? CouleurTuyau.NOIR.ordinal() : CouleurTuyau.BLANC.ordinal();
            
            
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
    }
    
    
    // CONSTRUCTION DU PLATEAU DE JEU
    private void construirePlateau(Graphics2D graphics2D, int largeurCase, int hauteurCase){
        ArrayList<ArrayList<String>> plateauCourant = this.panelParent.getNiveauCourant().getPlateauCourant();
        
        int colonnePlateau = 0;
        int lignePlateau = 0;
        
        for(ArrayList<String> ligneP : plateauCourant){
            for(String casePlateau : ligneP) {
                
                // On affiche les coins, bordures et background des cases
                BufferedImage imgTemp = this.pipes.getSubimage(
                        typeCase(lignePlateau, colonnePlateau) * (120 + 20),
                        6 * (120 + 20),
                        120, 120);
                
                int nombreRotation = nombreRotations(lignePlateau, colonnePlateau);
                if(nombreRotation != 0) 
                    imgTemp = pivoter(imgTemp, nombreRotation);
                
                graphics2D.drawImage(imgTemp,
                        largeurCase * colonnePlateau, hauteurCase * lignePlateau,
                        largeurCase, hauteurCase, this);

                
                // On affiche les sources
                if(estUneSource(casePlateau) != 0){
                    // On recupere l'image correspondant au tuyau
                    imgTemp = this.pipes.getSubimage(
                                    0 * (120 + 20),
                                    estUneSource(casePlateau) * (120 + 20),
                                    120, 120);

                    // On pivote l'image si necessaire
                    nombreRotation = Integer.parseInt(casePlateau.substring(1));
                    if(nombreRotation != 0) 
                        imgTemp = pivoter(imgTemp, nombreRotation);

                    graphics2D.drawImage(imgTemp,
                        largeurCase * colonnePlateau, hauteurCase * lignePlateau,
                        largeurCase, hauteurCase, this);
                }
                
                // On affiche les cases inamovibles
                else if(casePlateau.startsWith("*")){
                    
                }
                
                else if(!casePlateau.equals("X") && !casePlateau.equals(".")){
                    
                }
                
                colonnePlateau = colonnePlateau + 1;
            }
            
            // On passe a la ligne suivante et on revient à la colonne 0
            lignePlateau = lignePlateau + 1;
            colonnePlateau = 0;
        }
    }
    
    
    // Determine si une case du plateau est un COIN, une BORDURE ou une CASE quelconque
    public int typeCase(int ligne, int colonne){
        int nbrCasesLargeur = this.panelParent.getNiveauCourant().getLargeurPlateau();
        int nbrCasesHauteur = this.panelParent.getNiveauCourant().getHauteurPlateau();
        
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
    
    // Renvoie 0 si la case n'est pas une source et la ligne de l'image correspondante sinon
    public int estUneSource(String nomCase) {
        if(nomCase.startsWith("R")){ return CouleurTuyau.ROUGE.ordinal(); }
        else if(nomCase.startsWith("G")){ return CouleurTuyau.VERT.ordinal(); }
        else if(nomCase.startsWith("B")){ return CouleurTuyau.BLEU.ordinal(); }
        else if(nomCase.startsWith("Y")){ return CouleurTuyau.JAUNE.ordinal(); }
        return 0;
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
