package component.back_end.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import exception.back_end.PrimaryKeyNotFoundException;

/**
 * Created by maianhvu on 7/3/16.
 */
public class TaskCollection {

    private TreeMap<Integer, Task> taskData_;

    public TaskCollection() {
        this.taskData_ = new TreeMap<>();
    }

    public int save(Task task) {
        // TODO: Check for potential time clashes

        if (task.getId() == null) {
            // TODO: Extract magic constant
            int newIndex = 1;

            if (!this.taskData_.isEmpty()) {
                // TODO: Extract magic constant
                newIndex = this.taskData_.lastKey() + 1;
            }

            task.setId(newIndex);
        }

        this.taskData_.put(task.getId(), task);
        return task.getId();
    }

    public Task get(int index) throws PrimaryKeyNotFoundException {
        // check if TreeMap contains the key that is queried
        if (!this.taskData_.containsKey(index)) {
            throw new PrimaryKeyNotFoundException(index);
        }
        // key exists, retrieve Task corresponding to key
        return this.taskData_.get(index);
    }
    
    /**
     * Returns a filtered list of Tasks that match the specified TaskDescriptor
     * Returns the full (unfiltered) list of Tasks when no TaskDescriptor is specified 
     * 
     * @param taskDescriptor
     * 
     * @return results which is a list of filtered Tasks that matches TaskDescriptor if one is specified,
     * else results is the full lis of Tasks stored in TreeMap
     * 
     */
    public List<Task> getAll(TaskDescriptor taskDescriptor) {
        ArrayList<Task> results = new ArrayList<>(this.taskData_.values());
        
        // when no task descriptor is specified, taskDescriptor is null
        if (taskDescriptor == null) {
            return results;
        }
        
        Iterator<Task> it = results.iterator();
        while (it.hasNext()) {
            Task task = it.next();
            if (!taskDescriptor.matches(task)) {
                // remove Task that does not match TaskDescriptor
                it.remove();
            }
        }
        
        // return filtered results as a List
        return results;
    }

    public Task remove(int id) {
        // TODO: Check if ID does not exist

        return this.taskData_.remove(id);
    }
}
