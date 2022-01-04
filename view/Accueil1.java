package projetIG.view;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import projetIG.Plumber;

public class Accueil1 extends JPanel {
    protected Plumber panelParent;
    protected final int NOMBRE_BANQUES = 2;
    
    public Accueil1(Plumber panelParent) {
        this.panelParent = panelParent;
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
        for(int i = 1; i <= NOMBRE_BANQUES; i++){
            this.creerBouton("Banque n° " + i, i);
        }
    }
    
    private void creerBouton(String nom, int numero) {
        JButton button = new JButton(nom);
        button.setPreferredSize(new Dimension(200, 70));
        
        button.addActionListener((event) -> {
            Accueil2 accueil2 = new Accueil2(Accueil1.this.panelParent, numero);
            Accueil1.this.panelParent.setAccueil2(accueil2);
            
            Accueil1.this.panelParent.removeAll();
            Accueil1.this.panelParent.add(Accueil1.this.panelParent.getAccueil2());
            Accueil1.this.panelParent.revalidate();
            Accueil1.this.panelParent.repaint();
        });
        
        this.add(button);
    }
}
