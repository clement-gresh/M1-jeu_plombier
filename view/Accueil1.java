package projetIG.view;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import projetIG.Plombier;

public class Accueil1 extends JPanel {
    protected Plombier panelParent;
    protected final int NOMBRE_BANQUES = 2;
    
    public Accueil1(Plombier panelParent) {
        this.panelParent = panelParent;
        
        
        this.setPreferredSize(new Dimension(850, 700)); // largeur, hauteur
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setAlignmentX(CENTER_ALIGNMENT);
        
        for(int i = 1; i <= NOMBRE_BANQUES; i++){
            this.creerBouton("Banque n° " + i, i);
        }
    }
    
    private void creerBouton(String nom, int numeroBanque) {
        JButton button = new JButton(nom);
        button.setPreferredSize(new Dimension(200, 70));
        button.setAlignmentX(CENTER_ALIGNMENT);
        
        button.addActionListener((event) -> Accueil1.this.panelParent.afficherAccueil2(numeroBanque));
        
        this.add(button);
    }
}
