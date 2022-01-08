package projetIG;

import java.awt.AWTException;
import projetIG.view.menu.MyMenuBar;
import java.awt.BorderLayout;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import projetIG.controller.AnnulerManager;
import projetIG.controller.action.ActionAnnuler;
import projetIG.controller.action.ActionRecommencerNiveau;
import projetIG.controller.action.ActionRetablir;
import projetIG.controller.action.ActionRetourAccueil;
import projetIG.view.PanelBanques;
import projetIG.view.PanelNiveaux;
import projetIG.view.PanelFenetreJeu;
import projetIG.view.menu.MyPopupMenu;

public class Plombier extends JPanel {
    public static final int PAS_NIVEAU = -1;
    public static final boolean RETOUR_ACC_ACT = true;
    public static final boolean RETOUR_ACC_DES = false;
    public static final boolean RE_NIVEAU_ACT = true;
    public static final boolean RE_NIVEAU_DES = false;
    
    private final JFrame frameParent;
    private final AnnulerManager annulerManager;
    private final MyPopupMenu popupMenu;
    private final PanelBanques panelBanques = new PanelBanques(this);
    private PanelNiveaux panelNiveaux;
    private PanelFenetreJeu plateau;
    private int numeroBanque;
    private int numeroNiveau;
    private final ActionRetourAccueil actionRetourAccueil;
    private final ActionRecommencerNiveau actionRecommencer;
    
    public Plombier(JFrame frameParent) {
        //Demande de confirmation lors de la fermeture de la fenêtre
        this.frameParent = frameParent;
        this.frameParent.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Plombier.this.confirmClose();
            }
        });
        
        this.setLayout(new BorderLayout());
        
        
        //Ajout du manager pour Annuler/Retablir
        this.annulerManager = new AnnulerManager(this);
        
        //Creation des actions des menus
        this.actionRetourAccueil = new ActionRetourAccueil(this);
        this.actionRecommencer = new ActionRecommencerNiveau(this);
        ActionAnnuler actionAnnuler = annulerManager.getAnnuler();
        ActionRetablir actionRetablir = annulerManager.getRetablir();
        
        //Ajout de la barre de menu
        this.frameParent.setJMenuBar(new MyMenuBar(this, actionRetourAccueil,
                                         actionRecommencer, actionAnnuler, actionRetablir));
        
        
        //Ajout de l'accueil
        this.afficherPnlBanques();
        
        
        //Ajout du menu contextuel (clic-droit) a la fenetre
        this.popupMenu = new MyPopupMenu(this, actionRetourAccueil,
                                         actionRecommencer, actionAnnuler, actionRetablir);
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if(SwingUtilities.isRightMouseButton(event)) {
                    Plombier.this.popupMenu.show(event.getComponent(), event.getX(), event.getY());
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
    public void afficherPnlBanques(){
        this.afficher(this.panelBanques, RETOUR_ACC_DES, RE_NIVEAU_DES);
    }
    
    // Cree et affiche l'accueil permettant de choisir le niveau
    public void afficherPnlNiveaux(int numeroBanque){
        this.setNumeroBanque(numeroBanque);
        
        PanelNiveaux panelAccueil2 = new PanelNiveaux(this, numeroBanque);
        this.setPanelNiveaux(panelAccueil2);
            
        this.afficher(this.panelNiveaux, RETOUR_ACC_DES, RE_NIVEAU_DES);
    }
    
    // Cree le niveau et affiche le plateau correspondant
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
                    Plombier.this.popupMenu.show(event.getComponent(), event.getX(), event.getY());
                }
            }
        });

        this.afficher(this.plateau, RETOUR_ACC_ACT, RE_NIVEAU_ACT);
    }
    
    // Affiche le panel selectionne et met a jour les boutons du menu
    private void afficher(JPanel panel, boolean actionRetour, boolean actionRecommencer){
        this.removeAll();
        this.add(panel, BorderLayout.CENTER);
        this.annulerManager.discardAllEdits();
        this.annulerManager.updateItems();
        this.actionRetourAccueil.setEnabled(actionRetour);
        this.actionRecommencer.setEnabled(actionRecommencer);
        this.frameParent.pack();
        this.revalidate();
        this.repaint();
    }
    
    
    
    
    // Determine s'il y a un niveau suivant dans la banque de niveau
    public boolean isThereNextLevel(){
        File banque = new File(this.cheminFichier(this.numeroBanque, PAS_NIVEAU));
        
        return (this.numeroNiveau < banque.list().length);
    }
    
    // Demande confirmation avant la cloture de la frame
    public void confirmClose() {
        int clicBouton = Plombier.fenetreConfirmation(
                this.frameParent, "Quitter", "Etes vous sur de vouloir quitter le jeu ?");
        
        if(clicBouton == JOptionPane.YES_OPTION) { this.frameParent.dispose(); }
    }
    
    static public int fenetreConfirmation(JFrame frame, String titre, String texte){
        Plombier.pressAlt();
        frame.setAlwaysOnTop(true);
        
        int clicBouton = JOptionPane.showConfirmDialog(frame, 
                texte, titre, YES_NO_OPTION, WARNING_MESSAGE);
        
        frame.setAlwaysOnTop(false);
        Plombier.releaseAlt();
        
        return clicBouton;
    }
    
    // "Presse" le bouton Alt jusqu'a l'appel de releaseAlt()
    public static void pressAlt(){
        try {
            Robot r = new Robot();
            r.keyPress(KeyEvent.VK_ALT);
        } catch (AWTException ex) {
            System.out.println("Erreur press Alt : " + ex.getMessage());
        }
    }

    // "Relache" le bouton Alt
    public static void releaseAlt(){
        try {
            Robot r = new Robot();
            r.keyRelease(KeyEvent.VK_ALT);
        } catch (AWTException ex) {
            System.out.println("Erreur press Alt : " + ex.getMessage());
        }
    }
    
    
    // GETTERS
    public JFrame getFrameParent() {
        return frameParent;
    }

    public AnnulerManager getAnnulerManager() {
        return annulerManager;
    }

    public PanelBanques getPanelBanques() {
        return panelBanques;
    }

    public PanelNiveaux getPanelNiveaux() {
        return panelNiveaux;
    }

    public PanelFenetreJeu getPlateau() {
        return plateau;
    }

    public int getNumeroBanque() {
        return numeroBanque;
    }

    public int getNumeroNiveau() {
        return numeroNiveau;
    }
    

    // SETTERS
    public void setPanelNiveaux(PanelNiveaux panelNiveaux) {
        this.panelNiveaux = panelNiveaux;
    }

    public void setPlateau(PanelFenetreJeu plateau) {
        this.plateau = plateau;
    }

    public void setNumeroBanque(int numeroBanque) {
        this.numeroBanque = numeroBanque;
    }

    public void setNumeroNiveau(int numeroNiveau) {
        this.numeroNiveau = numeroNiveau;
    }
}
