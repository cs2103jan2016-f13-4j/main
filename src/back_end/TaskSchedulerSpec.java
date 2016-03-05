package back_end;

import java.time.LocalDateTime;
import java.util.List;
import objects.*;


/**
 * 
 * created by thenaesh on Mar 5, 2016
 *
 */
public abstract class TaskSchedulerSpec {
    public LocalDateTime findSlot(List<Task> existingTasks, long duration) {
        return null;
    }
}
