package component.back_end.storage;

import java.util.List;
import java.util.function.Function;

import entity.Task;
import entity.TaskDescriptor;

/**
 * The Data Store is the gatekeeper for the actual task data stored on disk.
 * It exposes functionality to create, read, update and delete the task data.
 * 
 * @author Huiyie
 *
 */

public class DataStore extends DataStoreSpec {

    @Override
    public void addTask(Task task) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Task> getTasks(TaskDescriptor descriptor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Task> getTasks(TaskDescriptor descriptor, Function<List<Task>, Void> modifierFunction) {
        // TODO Auto-generated method stub
        return null;
    }

}
