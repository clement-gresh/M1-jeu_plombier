package projetIG.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import projetIG.model.CouleurTuyau;
import projetIG.model.Rotation;
import projetIG.model.TypeTuyau;
import projetIG.model.niveau.Tuyau;
import projetIG.model.niveau.TuyauPlateau;
import projetIG.model.niveau.TuyauReserve;
import projetIG.view.FenetreJeu;
import static projetIG.view.FenetreJeu.combiner;

public class DragDropController extends MouseAdapter {
    protected FenetreJeu fenetreJeu;
    protected int hauteurCase;
    protected int largeurCase;
    protected int nbrCasesTotalHauteur;
    protected int nbrCasesTotalLargeur;
    protected boolean deplacementTuyau = false;
    protected Tuyau tuyauDeplace;

    public DragDropController(FenetreJeu fenetreJeu) {
        this.fenetreJeu = fenetreJeu;
    }
    
    @Override
    public void mousePressed(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            
            this.hauteurCase = this.fenetreJeu.getHauteurCase();
            this.largeurCase = this.fenetreJeu.getLargeurCase();
            
            this.nbrCasesTotalHauteur = this.fenetreJeu.getNbrCasesTotalHauteur();
            this.nbrCasesTotalLargeur = this.fenetreJeu.getNbrCasesTotalLargeur();
                        
            // On determine la colonne et la ligne de la case où le clic a eu lieu ( entre 0 et (nbrCases - 1) )
            int colonne = (int) Math.ceil( event.getX() / this.largeurCase ) ;
            int ligne = (int) Math.ceil( event.getY() / this.hauteurCase ) ;
            
            System.out.println("Colonne " + colonne + " ligne " + ligne); // debug
            
            BufferedImage buffTuyauDD = new BufferedImage(this.largeurCase, this.largeurCase,
                                                          BufferedImage.TYPE_INT_ARGB);
            
            // Si le clic a eu lieu dans la reserve
            if(colonne > this.nbrCasesTotalLargeur - 3 && ligne < 6) {
                // On verifie que colonne ne depasse pas le nombre maximal (peut arriver a cause de 
                // l'imprecision sur le nombre de pixels lie au cast en int). On lui assigne le max sinon.
                if(colonne > this.nbrCasesTotalLargeur - 1) colonne = this.nbrCasesTotalLargeur - 1;
                
                TuyauReserve tuyauReserve = this.fenetreJeu.getNiveauCourant().getTuyauxReserve()
                                        .get(ligne * 2 + (this.nbrCasesTotalLargeur - colonne) % 2);

                if(tuyauReserve.getNombre() > 0) {
                    tuyauReserve.diminuerNombre();

                    this.deplacementTuyau = true;
                    this.tuyauDeplace = tuyauReserve;
                }
            }
            
            // Sinon, si le clic a lieu sur le plateau de jeu
            else if (ligne < this.nbrCasesTotalHauteur) {
                
                // On verifie qu'il s'agit d'un tuyau et qu'il est deplacable
                int size = this.fenetreJeu.getNiveauCourant().getPlateauCourant().size();
                
                for(int i = 0; i < size; i++){
                    TuyauPlateau tuyauPlateau = this.fenetreJeu.getNiveauCourant().getPlateauCourant().get(i);
                                        
                    if(ligne == tuyauPlateau.getLigne()
                            && colonne == tuyauPlateau.getColonne()
                            && !tuyauPlateau.isInamovible()
                            ){
                        
                        this.deplacementTuyau = true;
                        this.tuyauDeplace = tuyauPlateau;
                                                
                        // On enleve le tuyau du plateau
                        this.fenetreJeu.getNiveauCourant().getPlateauCourant().remove(i);
                        i--;
                        size--;
                        break;
                    }
                }
            }
            
            

            if(this.deplacementTuyau){
                // On recupere l'image du tuyau avec la rotation correspondant
                buffTuyauDD = this.fenetreJeu.getPipes().getSubimage(
                        this.tuyauDeplace.getNom().ordinal() * (120 + 20),
                        CouleurTuyau.BLANC.ordinal() * (120 + 20),
                        120, 120);
                
                if(this.tuyauDeplace.getRotation() != Rotation.PAS_DE_ROTATION){
                    buffTuyauDD = FenetreJeu.pivoter(buffTuyauDD, tuyauDeplace.getRotation().ordinal());
                }
                
                else if(this.tuyauDeplace.getNom().equals(TypeTuyau.OVER)){
                    BufferedImage imgTemp2 = this.fenetreJeu.getPipes().getSubimage(
                        (this.tuyauDeplace.getNom().ordinal() - 1) * (120 + 20),
                        CouleurTuyau.BLANC.ordinal() * (120 + 20), 120, 120);
                
                    buffTuyauDD = combiner(imgTemp2, buffTuyauDD);
                }
            }
            
            
            // On ajoute l'image Drag&Drop au plateau de jeu (apres lui avoir donne la bonne taille)
            buffTuyauDD = FenetreJeu.modifierTaille(buffTuyauDD, this.largeurCase, this.hauteurCase);
            this.fenetreJeu.setImageDD(new ImageIcon(buffTuyauDD));
            this.fenetreJeu.setXImageDD(event.getX() - (int) (0.5 * this.largeurCase));
            this.fenetreJeu.setYImageDD(event.getY() - (int) (0.5 * this.hauteurCase));
            this.fenetreJeu.repaint();
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            // On met a jour les coordonnes de l'image de Drag&Drop dans la fenetre de jeu
            this.fenetreJeu.setXImageDD(event.getX() - (int) (0.5 * this.largeurCase));
            this.fenetreJeu.setYImageDD(event.getY() - (int) (0.5 * this.hauteurCase));
            this.fenetreJeu.repaint();
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            if(deplacementTuyau) {
                // On determine la colonne et la ligne de la case où le bouton a ete relache ( entre 0 et (nbrCases - 1) )
                int colonne = (int) Math.ceil( event.getX() / this.largeurCase ) ;
                int ligne = (int) Math.ceil( event.getY() / this.hauteurCase ) ;

                System.out.println("Colonne relaché " + colonne + " ligne relaché " + ligne); // debug


                // On verifie qu'on se trouve a l'interieur du plateau de jeu (sans les bordures)
                if( (colonne > 0)
                        && (colonne < this.nbrCasesTotalLargeur - 3)
                        && (ligne > 0)
                        && (ligne < this.nbrCasesTotalHauteur - 1)
                        ) {
                    
                    boolean caseOccupee = false;
                    
                    for(TuyauPlateau tuyauPlateau : this.fenetreJeu.getNiveauCourant().getPlateauCourant()){
                        if(tuyauPlateau.getColonne() == colonne && tuyauPlateau.getLigne() == ligne){
                            renvoyerDansReserve();
                            caseOccupee = true;
                            break;
                        }
                    }
                    
                    if(!caseOccupee)
                        this.fenetreJeu.getNiveauCourant().getPlateauCourant().add(
                                new TuyauPlateau(this.tuyauDeplace, ligne, colonne));
                    
                    else{
                        renvoyerDansReserve();
                    }
                }

                else {
                    renvoyerDansReserve();
                }
            
            
                // On remet l'image Drag&Drop dans la fenetre de jeu à vide
                this.fenetreJeu.setImageDD(new ImageIcon());
                this.fenetreJeu.repaint();

                this.deplacementTuyau = false;
            }
        }
    }
    
    // Renvoie le tuyauDeplace dans la reserve
    private void renvoyerDansReserve(){
        for(TuyauReserve tuyauReserve : this.fenetreJeu.getNiveauCourant().getTuyauxReserve()){
            
            if(tuyauReserve.getNom() == this.tuyauDeplace.getNom() 
                    && tuyauReserve.getRotation() == this.tuyauDeplace.getRotation()){
                
                tuyauReserve.augmenterNombre();                
                break;
            }
        }
    }

}