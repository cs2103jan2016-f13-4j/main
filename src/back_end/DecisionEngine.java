package back_end;

import back_end.storage.Storage;
import back_end.storage.relations.Task;
import objects.Command;
import objects.ExecutionResult;

/**
 * Created by maianhvu on 5/3/16.
 */
public class DecisionEngine {

    protected final Storage dataStorage_;

    public DecisionEngine() {
        this.dataStorage_ = new Storage();

        this.dataStorage_.initializeStorageFor(Task.class);
    }

    public ExecutionResult performCommand(Command command) {
        return null; // TODO: stub
    }
}
