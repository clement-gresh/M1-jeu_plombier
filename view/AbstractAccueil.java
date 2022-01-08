package projetIG.view;

import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Dimension;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import projetIG.Plombier;

abstract public class AbstractAccueil extends JPanel {
    protected Plombier panelParent;
    
    protected AbstractAccueil(Plombier panelParent) {
        this.panelParent = panelParent;
        
        this.setPreferredSize(new Dimension(850, 700)); // largeur, hauteur
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setAlignmentX(CENTER_ALIGNMENT);
    }
    
    protected JButton creerBouton(String nom) {
        JButton bouton = new JButton(nom);
        bouton.setPreferredSize(new Dimension(200, 70));
        bouton.setAlignmentX(CENTER_ALIGNMENT);
        
        this.add(bouton);
        return bouton;
    }
    
}
