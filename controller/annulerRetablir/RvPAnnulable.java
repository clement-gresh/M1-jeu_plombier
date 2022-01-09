package projetIG.controller.annulerRetablir;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import static projetIG.controller.DragDropController.PLATEAU;
import static projetIG.controller.DragDropController.RESERVE;
import projetIG.model.niveau.Niveau;
import projetIG.model.niveau.Tuyau;
import projetIG.view.PanelJeu;

public class RvPAnnulable extends AbstractAnnulable {
    public RvPAnnulable(PanelJeu fenetreJeu, Niveau niveau, Tuyau tuyau,
                        int lDepart, int cDepart, int lArrivee, int cArrivee) {
        super(fenetreJeu, niveau, tuyau, lDepart, cDepart, lArrivee, cArrivee);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.ajouterTuyau(lArrivee, cArrivee, PLATEAU);
        super.enleverTuyau(lDepart, cDepart, RESERVE);
        super.maj();
    }

    @Override
    public void undo() throws CannotUndoException {
        super.enleverTuyau(lArrivee, cArrivee, PLATEAU);
        super.ajouterTuyau(lDepart, cDepart, RESERVE);
        super.maj();
    }
}