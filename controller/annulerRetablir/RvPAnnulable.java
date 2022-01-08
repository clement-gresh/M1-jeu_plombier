
package projetIG.controller.annulerRetablir;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.Tuyau;
import projetIG.model.niveau.TuyauPlateau;
import projetIG.view.PanelJeu;

public class RvPAnnulable extends AbstractUndoableEdit {
    private final PanelJeu fenetreJeu;
    private final Niveau niveau;
    private final Tuyau tuyau;
    private final int ligneReserve;
    private final int colonneReserve;
    private final int lignePlateau;
    private final int colonnePlateau;

    public RvPAnnulable(PanelJeu fenetreJeu, Niveau niveau, Tuyau tuyau,
            int ligneReserve, int colonneReserve, int lignePlateau, int colonnePlateau) {
        this.fenetreJeu = fenetreJeu;
        this.niveau = niveau;
        this.tuyau = tuyau;
        this.ligneReserve = ligneReserve;
        this.colonneReserve = colonneReserve;
        this.lignePlateau = lignePlateau;
        this.colonnePlateau = colonnePlateau;
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public void redo() throws CannotRedoException {
        niveau.getPlateauCourant().get(this.lignePlateau).set(colonnePlateau, new TuyauPlateau(tuyau));
        niveau.getTuyauxReserve().get(ligneReserve).get(colonneReserve).diminuerNombre();
        
        fenetreJeu.getNiveauCourant().majCouleurs();
        fenetreJeu.paintImmediately(0, 0, this.fenetreJeu.getTaillePixelLargeur(),
                                          this.fenetreJeu.getTaillePixelHauteur());
        //fenetreJeu.getPanelParent().getCo
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void undo() throws CannotUndoException {
        niveau.getPlateauCourant().get(this.lignePlateau).set(colonnePlateau, null);
        niveau.getTuyauxReserve().get(ligneReserve).get(colonneReserve).augmenterNombre();
        
        fenetreJeu.getNiveauCourant().majCouleurs();
        fenetreJeu.paintImmediately(0, 0, this.fenetreJeu.getTaillePixelLargeur(),
                                          this.fenetreJeu.getTaillePixelHauteur());
    }
    
    
    
}