package projetIG;

import projetIG.view.menu.MyMenuBar;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import projetIG.controller.AnnulerManager;
import projetIG.controller.action.ActionAnnuler;
import projetIG.controller.action.ActionRetablir;
import projetIG.view.Accueil1;
import projetIG.view.Accueil2;
import projetIG.view.PanelFenetreJeu;
import projetIG.view.menu.MyPopupMenu;

public class Plumber extends JPanel {
    private final JFrame frameParent;
    private final AnnulerManager annulerManager;
    private final MyPopupMenu popupMenu;
    private final Accueil1 accueil1 = new Accueil1(this);
    private Accueil2 accueil2;
    private PanelFenetreJeu plateau;
    private int numeroBanque;
    private int numeroNiveau;
    public static int PAS_DE_NIVEAU = -1;
    
    public Plumber(JFrame frameParent) {
        this.frameParent = frameParent;
        
        this.setLayout(new BorderLayout());
        
        
        //Ajout du manager pour Annuler/Retablir
        //JMenuItem annuler = new JMenuItem("Annuler");
        //JMenuItem retablir = new JMenuItem("Retablir");
        this.annulerManager = new AnnulerManager(this);
        
        ActionAnnuler actionAnnuler = annulerManager.getAnnuler();
        ActionRetablir actionRetablir = annulerManager.getRetablir();
        
        //Ajout de la barre de menu
        this.frameParent.setJMenuBar(new MyMenuBar(this.frameParent, this, actionAnnuler, actionRetablir));
        
        
        //Ajout de l'accueil
        this.add(this.accueil1, BorderLayout.CENTER);
        
        
        //Ajout du menu contextuel (clic-droit) a la fenetre
        this.popupMenu = new MyPopupMenu(this.frameParent, this, actionAnnuler, actionRetablir);
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if(SwingUtilities.isRightMouseButton(event)) {
                    Plumber.this.popupMenu.show(event.getComponent(), event.getX(), event.getY());
                }
            }
        });
    }
    
    
    // METHODES
    // Renvoie le chemin vers la banque de niveau (si numeroNiveau = PAS_DE_NIVEAU) ou vers le niveau
    public String cheminFichier(int numeroBanque, int numeroNiveau){
        if(numeroNiveau <= 0) {
            return "src/main/java/projetIG/model/niveau/banque" + numeroBanque;
        }
        else{
            return "src/main/java/projetIG/model/niveau/banque" + numeroBanque 
                        + "/level" + numeroNiveau + ".p";
        }
    }
    
    // Affiche l'accueil permettant de choisir la banque de niveaux
    public void afficherAccueil1(){
        this.removeAll();
        this.add(getAccueil1(), BorderLayout.CENTER);
        this.frameParent.pack();
        this.revalidate();
        this.repaint();
    }
    
    //Cree et affiche l'accueil permettant de choisir le niveau
    public void afficherAccueil2(int numeroBanque){
        this.setNumeroBanque(numeroBanque);
        
        Accueil2 panelAccueil2 = new Accueil2(this, numeroBanque);
        this.setAccueil2(panelAccueil2);
            
        this.removeAll();
        this.add(getAccueil2(), BorderLayout.CENTER);
        this.frameParent.pack();
        this.revalidate();
        this.repaint();
    }
    
    //Cree le niveau et affiche le plateau correspondant
    public void afficherNiveau(int numeroBanque, int numeroNiveau){
        this.setNumeroBanque(numeroBanque);
        this.setNumeroNiveau(numeroNiveau);
        
        PanelFenetreJeu panelFenetreJeu = new PanelFenetreJeu(this, this.cheminFichier(numeroBanque, numeroNiveau));
        this.setPlateau(panelFenetreJeu);
        
        
        //Ajout du menu contextuel (clic-droit) au plateau
        panelFenetreJeu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if(SwingUtilities.isRightMouseButton(event)) {
                    Plumber.this.popupMenu.show(event.getComponent(), event.getX(), event.getY());
                }
            }
        });

        this.removeAll();
        this.add(getPlateau(), BorderLayout.CENTER);
        this.frameParent.pack();
        this.revalidate();
        this.repaint();
    }
    
    public boolean isThereNextLevel(){
        File banque = new File(this.cheminFichier(this.numeroBanque, PAS_DE_NIVEAU));
        
        return (this.numeroNiveau < banque.list().length);
    }
    
    
    // GETTERS
    public AnnulerManager getAnnulerManager() {
        return annulerManager;
    }

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
