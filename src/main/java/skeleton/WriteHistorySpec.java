package skeleton;

import logic.StorageWriteOperation;

/**
 * This component assists the Decision Engine by keeping track of all write operations performed on Storage.
 * This component will enable undo/redo operations.
 *
 * @@author A0124772E
 */
public interface WriteHistorySpec {

    public void addToHistory(StorageWriteOperation op);

    public String addToHistoryAfterExecuting(StorageWriteOperation op);

    public boolean undo(); // returns whether undo succeeded

    public boolean redo(); // returns whether redo succeeded

}
