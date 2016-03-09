package component.back_end.storage;

import java.time.LocalDateTime;

/**
 * A task descriptor that matches a Task iff every attribute (name, start time, end time) matches exactly.
 * This descriptor is guaranteed to match exactly one Task in a set of Tasks.
 * 
 * Use this when you want to extract a single Task from a collection, and you are able to describe it exactly.
 * 
 * created by thenaesh on 10 Mar 2016
 *
 */
public class FullTaskDescriptor extends TaskDescriptor {
    protected String nameDesc_;
    protected LocalDateTime startDesc_;
    protected LocalDateTime endDesc_;

    public FullTaskDescriptor(String nameDesc, LocalDateTime startDesc, LocalDateTime endDesc) {
        this.nameDesc_ = nameDesc;
        this.startDesc_ = startDesc;
        this.endDesc_ = endDesc;
    }
    @Override
    public boolean matches(Task task) {
        boolean isMatch = true; // initially
        
        isMatch = isMatch && task.getTaskName().equals(this.nameDesc_);
        isMatch = isMatch && task.getStartTime().equals(this.startDesc_);
        isMatch = isMatch && task.getEndTime().equals(this.endDesc_);
        
        return isMatch;
    }

}
