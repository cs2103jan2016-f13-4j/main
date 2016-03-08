package component.back_end.storage;

import java.time.LocalDateTime;

/**
 * Created by maianhvu on 7/3/16.
 */
public class Task {
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
