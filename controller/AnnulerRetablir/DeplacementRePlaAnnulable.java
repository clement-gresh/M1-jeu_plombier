package projetIG.controller.AnnulerRetablir;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.TuyauPlateau;

public class DeplacementRePlaAnnulable extends AbstractUndoableEdit {
    private final Niveau niveau;
    private final TuyauPlateau tuyauPlateau;
    private final int lignePlateau;
    private final int colonnePlateau;
    private final int ligneReserve;
    private final int colonneReserve;

    public DeplacementRePlaAnnulable(Niveau niveau, TuyauPlateau tuyauPlateau, int lignePlateau,
                                 int colonnePlateau, int ligneReserve, int colonneReserve) {
        this.niveau = niveau;
        this.tuyauPlateau = tuyauPlateau;
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
        niveau.getPlateauCourant().get(this.lignePlateau).set(colonnePlateau, tuyauPlateau);
        niveau.getTuyauxReserve().get(ligneReserve).get(colonneReserve).diminuerNombre();
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void undo() throws CannotUndoException {
        niveau.getPlateauCourant().get(this.lignePlateau).set(colonnePlateau, null);
        niveau.getTuyauxReserve().get(ligneReserve).get(colonneReserve).augmenterNombre();
    }
    
    
    
}
