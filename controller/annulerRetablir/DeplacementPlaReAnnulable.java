package projetIG.controller.annulerRetablir;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.Tuyau;
import projetIG.model.niveau.TuyauPlateau;
import projetIG.view.FenetreJeu;

public class DeplacementPlaReAnnulable extends AbstractUndoableEdit {
    private final FenetreJeu fenetreJeu;
    private final Niveau niveau;
    private final Tuyau tuyau;
    private final int lignePlateau;
    private final int colonnePlateau;
    private final int ligneReserve;
    private final int colonneReserve;

    public DeplacementPlaReAnnulable(FenetreJeu fenetreJeu, Niveau niveau, Tuyau tuyau, int lignePlateau,
                                     int colonnePlateau, int ligneReserve, int colonneReserve) {
        this.fenetreJeu = fenetreJeu;
        this.niveau = niveau;
        this.tuyau = tuyau;
        this.lignePlateau = lignePlateau;
        this.colonnePlateau = colonnePlateau;
        this.ligneReserve = ligneReserve;
        this.colonneReserve = colonneReserve;
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public void redo() throws CannotRedoException {
        niveau.getPlateauCourant().get(this.lignePlateau).set(colonnePlateau, null);
        niveau.getTuyauxReserve().get(ligneReserve).get(colonneReserve).augmenterNombre();
        fenetreJeu.getPanelParent().getCouleurController().majCouleurs();
        fenetreJeu.paintImmediately(0, 0, this.fenetreJeu.getPanelParent().getTaillePixelLargeur(),
                                          this.fenetreJeu.getPanelParent().getTaillePixelHauteur());
        //fenetreJeu.getPanelParent().getCo
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void undo() throws CannotUndoException {
        niveau.getPlateauCourant().get(this.lignePlateau).set(colonnePlateau, new TuyauPlateau(tuyau));
        niveau.getTuyauxReserve().get(ligneReserve).get(colonneReserve).diminuerNombre();
        fenetreJeu.getPanelParent().getCouleurController().majCouleurs();
        fenetreJeu.paintImmediately(0, 0, this.fenetreJeu.getPanelParent().getTaillePixelLargeur(),
                                          this.fenetreJeu.getPanelParent().getTaillePixelHauteur());
    }
    
    
    
}
