package back_end.storage;

import java.util.List;
import java.util.function.*;

import communication_objects.*;

/**
 * The Data Store is the gatekeeper for the actual task data stored on disk.
 * It exposes functionality to create, read, update and delete the task data.
 * 
 * Rank: Staff Sergeant (reports to CPT Decision Engine)
 *
 * created by thenaesh on Mar 5, 2016
 *
 */
public abstract class DataStore {
    
    /**
     * Add a task into the data store
     * 
     * Note that the Task object must be fully defined (by the Task Scheduler if necessary)
     * before adding it. This will be checked by assertions.
     * @param task
     */
    public void addTask(Task task) {
    }
    
    /**
     * Get a list of tasks that match the task descriptor specified (see {@link TaskDescriptor})
     * @param descriptor
     * @return
     */
    public List<Task> getTasks(TaskDescriptor descriptor) {
        return null;
    }
    
    /**
     * Get a list of tasks that match the task descriptor specified (see {@link TaskDescriptor})
     * and then applies the modifier function to the list before returning it
     * 
     * This may be used to, for instance, sort the Task list by some metric before returning it.
     * 
     * @param descriptor
     * @param modifierFunction
     * @return
     */
    public List<Task> getTasks(TaskDescriptor descriptor, Function<List<Task>, Void> modifierFunction) {
        return null;
    }
}
