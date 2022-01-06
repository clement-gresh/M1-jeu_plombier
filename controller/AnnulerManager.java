package projetIG.controller;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import projetIG.Plumber;
import projetIG.controller.action.ActionAnnuler;
import projetIG.controller.action.ActionRetablir;

public class AnnulerManager extends UndoManager {
    private ActionAnnuler annuler;
    private ActionRetablir retablir;

    public AnnulerManager(Plumber panelPlumber) {
        this.annuler = new ActionAnnuler(panelPlumber, this);
        this.retablir = new ActionRetablir(panelPlumber, this);
        
        this.annuler.setEnabled(false);
        this.retablir.setEnabled(false);
        
        //this.annuler.addActionListener( (event) -> AnnulerManager.this.undo());
        //this.retablir.addActionListener( (event) -> AnnulerManager.this.redo());
    }

    @Override
    public synchronized boolean addEdit(UndoableEdit anEdit) {
        boolean b = super.addEdit(anEdit);
        this.updateItems();
        return b;
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        this.updateItems();
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        this.updateItems();
    }
    
    private void updateItems(){
        retablir.setEnabled(canRedo());
        annuler.setEnabled(canUndo());
    }
    
    // GETTERS
    public ActionAnnuler getAnnuler() {
        return annuler;
    }

    public ActionRetablir getRetablir() {
        return retablir;
    }
}
