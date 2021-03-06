package logic;

import skeleton.WriteHistorySpec;

import java.util.*;



/**
 * This component assists the Decision Engine by keeping track of all write operations performed on Storage.
 * This component will enable undo/redo operations.
 *
 * @@author A0124772E
 */
public class WriteHistory implements WriteHistorySpec {

    // singleton declaration
    private static WriteHistory instance = null;
    public static WriteHistory getInstance() {
        if (instance == null) {
            instance = new WriteHistory();
        }
        return instance;
    }
    private WriteHistory() {
    }


    // fields
    private LinkedList<StorageWriteOperation> _opSequence = new LinkedList<>();
    private int _opIndex = -1; // refers to latest operation performed (without being cancelled by an undo)


    // methods
    @Override
    public void addToHistory(StorageWriteOperation op) {
        this.checkIndexInvariant();

        this.chopOffAfterCurrentIndex();
        this._opSequence.add(op);
        this._opIndex++;

        this.checkIndexInvariant();
    }

    @Override
    public String addToHistoryAfterExecuting(StorageWriteOperation op) {
        String errorMsg = op.getInitialOperation().apply(null);
        this.addToHistory(op);
        return errorMsg;
    }


    /**
     * @return false if there were no ops to undo, true otherwise
     */
    @Override
    public boolean undo() {
        this.checkIndexInvariant();

        if (!this.existsOperationsToUndo()) {
            return false;
        }

        // get the operation we wish to undo
        StorageWriteOperation op = this._opSequence.get(this._opIndex--);

        // apply undo operation if the operation was actually performed, otherwise skip over it
        if (!op.isOperationExecuted()) {
            return this.undo();
        }

        boolean isUndoSuccessful = op.getUndoOperation().apply(null);
        assert isUndoSuccessful;

        this.checkIndexInvariant();

        return true;
    }

    /**
     * @return false if there were no ops to redo, true otherwise
     */
    @Override
    public boolean redo() {
        this.checkIndexInvariant();

        if (!this.existsOperationsToRedo()) {
            return false;
        }

        // get the operation we wish to redo
        StorageWriteOperation op = this._opSequence.get(++this._opIndex);

        // apply redo operation if the operation was actually performed, otherwise skip over it
        if (!op.isOperationExecuted()) {
            this.redo();
        }

        boolean isRedoSuccessful = op.getRedoOperation().apply(null);
        assert isRedoSuccessful;

        this.checkIndexInvariant();

        return true;
    }


    // helper methods
    private void chopOffAfter(int index) {
        this.checkIndexInvariant();
        this._opSequence.subList(index + 1, this._opSequence.size()).clear();
        this.checkIndexInvariant();
    }

    private void chopOffAfterCurrentIndex() {
        this.checkIndexInvariant();
        this.chopOffAfter(this._opIndex);
        this.checkIndexInvariant();
    }

    private boolean existsOperationsToUndo() {
        return this._opIndex >= 0;
    }

    private boolean existsOperationsToRedo() {
        return this._opIndex + 1 < this._opSequence.size();
    }

    // blows up if the index invariant is not satisfied;
    private void checkIndexInvariant() {
        boolean isLowerBoundInvariantSatisfied = this._opIndex >= -1;
        boolean isUpperBoundInvariantSatisfied = this._opIndex < this._opSequence.size();

        assert isLowerBoundInvariantSatisfied && isUpperBoundInvariantSatisfied;
    }
}
