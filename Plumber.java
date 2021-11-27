package projetIG;

import projetIG.view.PlateauJeu;
import projetIG.view.menu.MyMenuBar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import projetIG.view.menu.MyPopupMenu;

public class Plumber extends JPanel {
    private JFrame frameParent;
    
    public Plumber(JFrame frameParent) {
        this.frameParent = frameParent;
        
        this.setPreferredSize(new Dimension(900, 600));
        this.setLayout(new BorderLayout());
        
        
        //Ajout de la barre de menu
        this.frameParent.setJMenuBar(new MyMenuBar(this.frameParent));
        
        
        //Ajout du plateau de jeu
        PlateauJeu plateau = new PlateauJeu();
        this.add(plateau, BorderLayout.CENTER);
        
        
        //Ajout du menu contextuel (clic-droit) au plateau
        MyPopupMenu popupMenu = new MyPopupMenu(this.frameParent);
        
        plateau.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if(SwingUtilities.isRightMouseButton(event)) {
                    popupMenu.show(event.getComponent(), event.getX(), event.getY());
                }
            }
        });
    }
}
