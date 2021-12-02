package projetIG.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Accueil1 extends JPanel {

    public Accueil1() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        this.createButton("Banque n°1");
        this.createButton("Banque n°2");
        
    }
    
    private void createButton(String nom) {
        JButton button = new JButton(nom);
        button.setPreferredSize(new Dimension(200, 70));
        
        button.addActionListener((event) -> {
            
        });
        
        this.add(button);
    }
}
