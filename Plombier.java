package projetIG;

import java.awt.AWTException;
import projetIG.view.menu.BarreMenu;
import java.awt.BorderLayout;
import java.awt.Robot;
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
import projetIG.controller.action.ActionRecommencer;
import projetIG.controller.action.ActionRetablir;
import projetIG.controller.action.ActionAccueil;
import projetIG.controller.action.ActionQuitter;
import projetIG.view.PanelBanques;
import projetIG.view.PanelNiveaux;
import projetIG.view.PanelJeu;
import projetIG.view.menu.MenuPopup;

public class Plombier extends JPanel {
    public static final boolean ACCUEIL_ACT = true;
    public static final boolean ACCUEIL_DES = false;
    public static final boolean RECOMMENCER_ACT = true;
    public static final boolean RECOMMENCER_DES = false;
    public static final int AUCUN_NIVEAU = -1;
    
    private final JFrame frame;
    private final String chemin;
    private final String cheminImg;
    private AnnulerManager annulerManager;
    private MenuPopup popupMenu;
    private final PanelBanques panelBanques = new PanelBanques(this);
    private PanelNiveaux panelNiveaux;
    private PanelJeu plateau;
    private int numBanque;
    private int numNiveau;
    private ActionQuitter actionQuitter;
    private ActionAccueil actionAccueil;
    private ActionRecommencer actionRecommencer;
    
    public Plombier(JFrame frame, String chemin, String cheminImg) {
        this.frame = frame;
        this.chemin = chemin;
        this.cheminImg = cheminImg;
        this.setLayout(new BorderLayout());
        menus();
        afficherPnlBanques();
        
        //Demande de confirmation lors de la fermeture de la fenêtre
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Plombier.this.confirmClose();
            }
        });
    }
    
    
    // METHODES
    // Cree les menus et le undo/redo manager
    private void menus(){
        // Creation du manager et des actions
        this.annulerManager = new AnnulerManager(this, cheminImg);
        this.actionAccueil = new ActionAccueil(this);
        this.actionRecommencer = new ActionRecommencer(this, cheminImg);
        this.actionQuitter = new ActionQuitter(this, cheminImg);
        ActionAnnuler actionAnnuler = annulerManager.getAnnuler();
        ActionRetablir actionRetablir = annulerManager.getRetablir();
        
        //Ajout de la barre de menu
        this.frame.setJMenuBar(new BarreMenu(actionAccueil, actionRecommencer,
                                actionQuitter, actionAnnuler, actionRetablir));
        
        //Ajout du menu contextuel (clic-droit)
        this.popupMenu = new MenuPopup(actionAccueil, actionRecommencer,
                                actionQuitter, actionAnnuler, actionRetablir);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if(SwingUtilities.isRightMouseButton(event)) {
                    Plombier.this.popupMenu.show(event.getComponent(),
                                                 event.getX(), event.getY());
                }
            }
        });
    }
    
    // Renvoie le chemin vers la banque de niveau ou vers le niveau
    public String chemin(int numeroBanque, int numeroNiveau){
        if(numeroNiveau <= 0) { return this.chemin + numeroBanque; }
        else{ return this.chemin + numeroBanque + "/level" 
                                 + numeroNiveau + ".p"; }
    }
    
    // Affiche l'accueil permettant de choisir la banque de niveaux
    public void afficherPnlBanques(){
        this.afficher(this.panelBanques, ACCUEIL_DES, RECOMMENCER_DES);
    }
    
    // Cree et affiche l'accueil permettant de choisir le niveau
    public void afficherPnlNiveaux(int numBanque){
        this.numBanque = numBanque;
        PanelNiveaux listeNiveaux = new PanelNiveaux(this, numBanque);
        this.panelNiveaux = listeNiveaux;
        this.afficher(this.panelNiveaux, ACCUEIL_DES, RECOMMENCER_DES);
    }
    
    // Cree le niveau et affiche le plateau correspondant
    public void afficherNiveau(int numBanque, int numNiveau){
        this.numBanque = numBanque;
        this.numNiveau = numNiveau;
        PanelJeu panelJeu = new PanelJeu(this, this.chemin(numBanque,numNiveau),
                                         this.cheminImg);
        this.plateau = panelJeu;
        
        //Ajout du menu contextuel (clic-droit) au plateau
        panelJeu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if(SwingUtilities.isRightMouseButton(event)) {
                    Plombier.this.popupMenu.show(event.getComponent(),
                                                 event.getX(), event.getY());
                }
            }
        });
        this.afficher(this.plateau, ACCUEIL_ACT, RECOMMENCER_ACT);
    }
    
    // Affiche le panel selectionne et met a jour les boutons du menu
    private void afficher(JPanel panel, boolean retour, boolean recommencer){
        this.removeAll();
        this.add(panel, BorderLayout.CENTER);
        this.annulerManager.discardAllEdits();
        this.annulerManager.updateItems();
        this.actionAccueil.setEnabled(retour);
        this.actionRecommencer.setEnabled(recommencer);
        this.frame.pack();
        this.revalidate();
        this.repaint();
    }
    
    // Determine s'il y a un niveau suivant dans la banque de niveau
    public boolean isThereNextLevel(){
        File banque = new File(this.chemin(this.numBanque, AUCUN_NIVEAU));
        return (this.numNiveau < banque.list().length);
    }
    
    // Demande confirmation avant la cloture de la frame
    public void confirmClose() {
        int clicBouton = Plombier.confirmation(this.frame, "Quitter",
                         "Etes vous sur de vouloir quitter le jeu ?");
        if(clicBouton == JOptionPane.YES_OPTION) { this.frame.dispose(); }
    }
    
    // Ouvre une fenetre de confirmation de type YES/NO
    static public int confirmation(JFrame frame, String titre, String texte){
        Plombier.pressAlt();
        frame.setAlwaysOnTop(true);
        int clicBouton = JOptionPane.showConfirmDialog(frame, 
                texte, titre, YES_NO_OPTION, WARNING_MESSAGE);
        frame.setAlwaysOnTop(false);
        Plombier.releaseAlt();
        return clicBouton;
    }
    
    // "Presse" le bouton Alt
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
    public JFrame getFrame() {
        return frame;
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

    public PanelJeu getPlateau() {
        return plateau;
    }

    public int getNumBanque() {
        return numBanque;
    }

    public int getNumNiveau() {
        return numNiveau;
    }
}
