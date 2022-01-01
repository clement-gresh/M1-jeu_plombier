package projetIG.view;

import java.awt.Dimension;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import projetIG.Plumber;

public class Accueil2 extends JPanel {
    protected Plumber panelParent;
    protected String numero;
    
    public Accueil2(Plumber panelParent, String numero) {
        this.panelParent = panelParent;
        this.numero = numero;
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        File banque = new File("src/main/java/projetIG/model/niveau/banque" + numero);
        System.out.println("src/main/java/projetIG/model/niveau/banque" + numero);
        System.out.println("Dossier existe : " + banque.exists());
        
        System.out.println("Nombre de fichiers : " + banque.list().length);
        
        for(int i = 1; i <= banque.list().length; i++){
            this.createButton("Niveau n° " + i, i);
        }
        
    }
    
    private void createButton(String nom, int i) {
        JButton button = new JButton(nom);
        button.setPreferredSize(new Dimension(200, 70));
        
        button.addActionListener((event) -> {
            PanelFenetreJeu panelFenetreJeu = new PanelFenetreJeu(
                    "src/main/java/projetIG/model/niveau/banque" + Accueil2.this.numero + "/level" + i + ".p");
            Accueil2.this.panelParent.setPlateau(panelFenetreJeu);
            
            Accueil2.this.panelParent.removeAll();
            Accueil2.this.panelParent.add(Accueil2.this.panelParent.getPlateau());
            Accueil2.this.panelParent.revalidate();
            Accueil2.this.panelParent.repaint();
        });
        
        this.add(button);
    }
}
