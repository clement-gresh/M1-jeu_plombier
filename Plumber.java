package projetIG;

import projetIG.view.menu.MyMenuBar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import projetIG.view.Accueil1;
import projetIG.view.Accueil2;
import projetIG.view.PanelFenetreJeu;
import projetIG.view.menu.MyPopupMenu;

public class Plumber extends JPanel {
    private final JFrame frameParent;
    private final JPanel accueil1 = new Accueil1(this);
    private JPanel accueil2;
    private JPanel plateau;
    private int numeroBanque;
    private int numeroNiveau;
    public static int PAS_DE_NIVEAU = -1;
    
    public Plumber(JFrame frameParent) {
        this.frameParent = frameParent;
        
        this.setPreferredSize(new Dimension(750, 700)); // largeur, hauteur
        this.setLayout(new BorderLayout());
        
        
        //Ajout de la barre de menu
        this.frameParent.setJMenuBar(new MyMenuBar(this.frameParent, this));
        
        
        //Ajout de l'accueil
        this.add(this.accueil1, BorderLayout.CENTER);
        
        
        //Ajout du menu contextuel (clic-droit) au plateau
        MyPopupMenu popupMenu = new MyPopupMenu(this.frameParent, this);
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if(SwingUtilities.isRightMouseButton(event)) {
                    popupMenu.show(event.getComponent(), event.getX(), event.getY());
                }
            }
        });
    }
    
    
    // METHODES
    public String cheminFichier(int numeroBanque, int numeroNiveau){
        if(numeroNiveau <= 0) {
            return "src/main/java/projetIG/model/niveau/banque" + numeroBanque;
        }
        else{
            return "src/main/java/projetIG/model/niveau/banque" + numeroBanque 
                        + "/level" + numeroNiveau + ".p";
        }
    }
    
    public void afficherAccueil1(){
        this.removeAll();
        this.add(getAccueil1());
        this.revalidate();
        this.repaint();
    }
    
    public void afficherAccueil2(int numeroBanque){
        this.setNumeroBanque(numeroBanque);
        
        Accueil2 accueil2 = new Accueil2(this, numeroBanque);
        this.setAccueil2(accueil2);
            
        this.removeAll();
        this.add(getAccueil2());
        this.revalidate();
        this.repaint();
    }
    
    public void afficherNiveau(int numeroBanque, int numeroNiveau){
        this.setNumeroBanque(numeroBanque);
        this.setNumeroNiveau(numeroNiveau);
        
        PanelFenetreJeu panelFenetreJeu = new PanelFenetreJeu(this, this.cheminFichier(numeroBanque, numeroNiveau));
        this.setPlateau(panelFenetreJeu);

        this.removeAll();
        this.add(getPlateau());
        this.revalidate();
        this.repaint();
    }
    
    public boolean isThereNextLevel(){
        File banque = new File(this.cheminFichier(this.numeroBanque, -1));
        
        if(this.numeroNiveau < banque.list().length){ return true; }
        
        return false;
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

    public int getNumeroBanque() {
        return numeroBanque;
    }

    public int getNumeroNiveau() {
        return numeroNiveau;
    }
    

    // SETTERS
    public void setAccueil2(JPanel accueil2) {
        this.accueil2 = accueil2;
    }

    public void setPlateau(JPanel plateau) {
        this.plateau = plateau;
    }

    public void setNumeroBanque(int numeroBanque) {
        this.numeroBanque = numeroBanque;
    }

    public void setNumeroNiveau(int numeroNiveau) {
        this.numeroNiveau = numeroNiveau;
    }
}
