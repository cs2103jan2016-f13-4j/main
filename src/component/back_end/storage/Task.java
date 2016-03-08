package component.back_end.storage;

import java.time.LocalDateTime;

/**
 * Created by maianhvu on 7/3/16.
 */
public class Task implements Comparable<Task> {
    private Integer id_;
    private String taskName_;
    private String description_;
    private LocalDateTime creationTime_;
    private LocalDateTime startTime_;
    private LocalDateTime endTime_;

    public Task(Integer id, String taskName, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.id_ = id;
        this.taskName_ = taskName;
        this.description_ = description;
        this.startTime_ = startTime;
        this.endTime_ = endTime;

        this.creationTime_ = LocalDateTime.now();
    }
    
    @Override
    public int compareTo(Task o) {
        if (this.getId() > o.getId()) {
            return 1;
        } else if (this.getId() < o.getId()) {
            return -1;
        } else {
            return 1;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this.getId() == ((Task) o).getId()) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    /**
     * Getters
     */
    public Integer getId() {
        return this.id_;
    }

    public String getTaskName() {
        return this.taskName_;
    }

    public String getDescription() {
        return this.description_;
    }

    public LocalDateTime getCreationTime() {
        return this.creationTime_;
    }

    public LocalDateTime getStartTime() {
        return this.startTime_;
    }

    public LocalDateTime getEndTime() {
        return this.endTime_;
    }

    /**
     * Setters
     */
    public void setId(int id) {
        this.id_ = id;
    }

}
