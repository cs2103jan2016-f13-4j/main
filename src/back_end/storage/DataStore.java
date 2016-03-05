package back_end.storage;

import java.util.List;
import objects.*;

/**
 *
 * created by thenaesh on Mar 5, 2016
 *
 */
public abstract class DataStore {
    public void addTask(Task task) {
    }
    public List<Task> getTasks(TaskDescriptor descriptor) {
        return null;
    }
    public List<Task> getTasks(TaskDescriptor descriptor, StoredProcedure procedure) {
        return null;
    }
}
