package component.back_end.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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

    public Task get(int index) {
        return this.taskData_.get(index);
    }

    public List<Task> getAll() {
        return new ArrayList<>(this.taskData_.values());
    }

    public Task remove(int id) {
        // TODO: Check if ID does not exist

        return this.taskData_.remove(id);
    }
}
