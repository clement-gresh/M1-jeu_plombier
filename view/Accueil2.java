package projetIG.view;

import java.awt.Dimension;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import projetIG.Plombier;

public class Accueil2 extends JPanel {
    protected Plombier panelParent;
    protected int numeroBanque;
    
    public Accueil2(Plombier panelParent, int numeroBanque) {
        this.panelParent = panelParent;
        this.numeroBanque = numeroBanque;
        
        
        this.setPreferredSize(new Dimension(850, 700)); // largeur, hauteur
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setAlignmentX(CENTER_ALIGNMENT);
        
        File banque = new File( panelParent.cheminFichier(numeroBanque, Plombier.PAS_DE_NIVEAU) );
        
        for(int i = 1; i <= banque.list().length; i++){
            this.creerBouton("Niveau n� " + i, i);
        }
    }
    
    private void creerBouton(String nom, int numeroNiveau) {
        JButton button = new JButton(nom);
        button.setPreferredSize(new Dimension(200, 70));
        button.setAlignmentX(CENTER_ALIGNMENT);
        
        button.addActionListener((event) -> Accueil2.this.panelParent.afficherNiveau(
                Accueil2.this.numeroBanque, numeroNiveau
        ));
        
        this.add(button);
    }
    
}
