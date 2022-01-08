
package projetIG.controller.annulerRetablir;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.Tuyau;
import projetIG.model.niveau.TuyauPlateau;
import projetIG.view.FenetreJeu;

public class DeplacementPlaPlaAnnulable extends AbstractUndoableEdit {
    private final FenetreJeu fenetreJeu;
    private final Niveau niveau;
    private final Tuyau tuyau;
    private final int ligneDepart;
    private final int colonneDepart;
    private final int ligneArrivee;
    private final int colonneArrivee;

    public DeplacementPlaPlaAnnulable(FenetreJeu fenetreJeu, Niveau niveau, Tuyau tuyau,
            int ligneDepart, int colonneDepart, int ligneArrivee, int colonneArrivee) {
        this.fenetreJeu = fenetreJeu;
        this.niveau = niveau;
        this.tuyau = tuyau;
        this.ligneDepart = ligneDepart;
        this.colonneDepart = colonneDepart;
        this.ligneArrivee = ligneArrivee;
        this.colonneArrivee = colonneArrivee;
    }

    @Override
    public boolean canRedo() { return true; }

    @Override
    public void redo() throws CannotRedoException {
        niveau.getPlateauCourant().get(this.ligneDepart).set(colonneDepart, null);
        niveau.getPlateauCourant().get(this.ligneArrivee).set(colonneArrivee, new TuyauPlateau(tuyau));
        
        fenetreJeu.getPanelParent().getCouleurController().majCouleurs();
        fenetreJeu.paintImmediately(0, 0, this.fenetreJeu.getPanelParent().getTaillePixelLargeur(),
                                          this.fenetreJeu.getPanelParent().getTaillePixelHauteur());
    }

    @Override
    public boolean canUndo() { return true; }

    @Override
    public void undo() throws CannotUndoException {
        niveau.getPlateauCourant().get(this.ligneArrivee).set(colonneArrivee, null);
        niveau.getPlateauCourant().get(this.ligneDepart).set(colonneDepart, new TuyauPlateau(tuyau));
        
        fenetreJeu.getPanelParent().getCouleurController().majCouleurs();
        fenetreJeu.paintImmediately(0, 0, this.fenetreJeu.getPanelParent().getTaillePixelLargeur(),
                                          this.fenetreJeu.getPanelParent().getTaillePixelHauteur());
    }
    
    
    
}
