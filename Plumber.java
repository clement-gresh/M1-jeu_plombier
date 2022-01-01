package projetIG;

import projetIG.view.menu.MyMenuBar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import projetIG.view.Accueil1;
import projetIG.view.Accueil2;
import projetIG.view.PanelFenetreJeu;
import projetIG.view.menu.MyPopupMenu;

public class Plumber extends JPanel {
    private JFrame frameParent;
    private JPanel accueil1 = new Accueil1(this);
    private JPanel accueil2;
    private JPanel plateau;
    
    public Plumber(JFrame frameParent) {
        this.frameParent = frameParent;
        
        this.setPreferredSize(new Dimension(750, 700)); // largeur, hauteur
        this.setLayout(new BorderLayout());
        
        
        //Ajout de la barre de menu
        this.frameParent.setJMenuBar(new MyMenuBar(this.frameParent));
        
        
        //Ajout de l'accueil
        this.add(this.accueil1, BorderLayout.CENTER);
        
        
        //Ajout du menu contextuel (clic-droit) au plateau
        MyPopupMenu popupMenu = new MyPopupMenu(this.frameParent);
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if(SwingUtilities.isRightMouseButton(event)) {
                    popupMenu.show(event.getComponent(), event.getX(), event.getY());
                }
            }
        });
    }
    
    
    // GETTERS
    public JPanel getAccueil1() {
        return accueil1;
    }

    public JPanel getAccueil2() {
        return accueil2;
    }

    public JPanel getPlateau() {
        return plateau;
    }
    

    // SETTERS
    public void setAccueil2(JPanel accueil2) {
        this.accueil2 = accueil2;
    }

    public void setPlateau(JPanel plateau) {
        this.plateau = plateau;
    }
    
}
