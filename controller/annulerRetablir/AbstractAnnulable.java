
package projetIG.controller.annulerRetablir;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import static projetIG.controller.DragDropController.PLATEAU;
import static projetIG.controller.DragDropController.RESERVE;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.Tuyau;
import projetIG.model.niveau.TuyauPlateau;
import projetIG.view.PanelJeu;

public class AbstractAnnulable extends AbstractUndoableEdit {
    
    protected final PanelJeu fenetreJeu;
    protected final Niveau niveau;
    protected final Tuyau tuyau;
    protected final int lDepart;
    protected final int cDepart;
    protected final int lArrivee;
    protected final int cArrivee;

    public AbstractAnnulable(PanelJeu fenetreJeu, Niveau niveau, Tuyau tuyau,
                            int lDepart, int cDepart, int lArrivee, int cArrivee) {
        this.fenetreJeu = fenetreJeu;
        this.niveau = niveau;
        this.tuyau = tuyau;
        this.lDepart = lDepart;
        this.cDepart = cDepart;
        this.lArrivee = lArrivee;
        this.cArrivee = cArrivee;
    }

    @Override
    public boolean canRedo() { return true; }

    @Override
    public boolean canUndo() { return true; }

    protected void enleverTuyau(int l, int c, int zone){
        if(zone == PLATEAU) niveau.getPlateau()[l][c] = null;
        else if(zone == RESERVE) niveau.getReserve()[l][c].diminuerNombre();
    }
    
    protected void ajouterTuyau(int l, int c, int zone){
        if(zone == PLATEAU) niveau.getPlateau()[l][c] = new TuyauPlateau(tuyau);
        else if(zone == RESERVE) niveau.getReserve()[l][c].augmenterNombre();
    }
    
    protected void maj(){
        fenetreJeu.getNiveau().majCouleurs();
        fenetreJeu.paintImmediately(0, 0, this.fenetreJeu.getPixelsLargeur(),
                                          this.fenetreJeu.getPixelsHauteur());
    }
}
