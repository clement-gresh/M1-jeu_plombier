package projetIG.view.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class ImageUtils {
    public static BufferedImage pivoter(BufferedImage img, int rotation) {
        int largeur = img.getWidth();
        int hauteur = img.getHeight();
        BufferedImage imgPivotee = new BufferedImage(largeur, hauteur,
                                                 BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imgPivotee.createGraphics();
        AffineTransform transform = new AffineTransform();
        transform.rotate((Math.PI / 2) * rotation, largeur / 2 , hauteur / 2);
        g2d.drawRenderedImage(img, transform);
        g2d.dispose();
        return imgPivotee;
    }
    
    public static BufferedImage combiner(BufferedImage img1,BufferedImage img2){
        int largeur = img1.getWidth();
        int hauteur = img1.getHeight();
        BufferedImage imgCombinee = new BufferedImage(largeur, hauteur,
                                                  BufferedImage.TYPE_INT_ARGB);
        // Ajoute les 2 images a imgCombinee
        Graphics g = imgCombinee.getGraphics();
        g.drawImage(img1, 0, 0, null);
        g.drawImage(img2, 0, 0, null);
        g.dispose();
        return imgCombinee;
    }
    
    public static BufferedImage changerTaille(BufferedImage img,
                                              int largeur, int hauteur) { 
        Image tmp = img.getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(largeur, hauteur,
                                               BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }
}
