package component.back_end.storage;

import java.time.LocalDateTime;

/**
 * The primary key of Task objects.
 * Has two attributes, namely task (String) and task start (LocalDateTime).
 * 
 * @author Huiyie
 *
 */

public class TaskPrimaryKey implements Comparable<TaskPrimaryKey> {
    
    private String task_;
    private LocalDateTime taskStart_;
    
    // Constructor
    public TaskPrimaryKey(String task, LocalDateTime taskStart) {
        this.task_ = task;
        this.taskStart_ = taskStart;
    }

    @Override
    public int compareTo(TaskPrimaryKey o) {
        if (this.getTask().equals(o.getTask())) {
            if (this.getTaskStart().equals(o.getTaskStart())) {
                return 0;
            }
        }
        return 1; // primary keys are only equal if both task and taskStart attributes match
    }

    public String getTask() {
        return task_;
    }

    public void setTask(String task) {
        task_ = task;
    }

    public LocalDateTime getTaskStart() {
        return taskStart_;
    }

    public void setTaskStart(LocalDateTime taskStart) {
        taskStart_ = taskStart;
    }

}
