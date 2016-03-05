package back_end.storage.relations;

import back_end.storage.base.SerialIdRelation;

import java.util.Date;

/**
 * Created by maianhvu on 5/3/16.
 */
public class Task extends SerialIdRelation {

    private String task_;
    private String details_;
    private Date creationTime_;
    private Date startTime_;
    private Date endTime_;

    public Task(String task, String details, Date startTime, Date endTime) {
        this.task_ = task;
        this.details_ = details;
        this.startTime_ = startTime;
        this.endTime_ = endTime;

        // Timestamp
        this.creationTime_ = new Date();
    }

    public Task(String task) {
        this(task, null, null, null);
    }

    /**
     * Getters
     */
    public String getTask() {
        return this.task_;
    }

    public String getDetails() {
        return this.details_;
    }

    public Date getCreationTime() {
        return this.creationTime_;
    }

    public Date getStartTime() {
        return this.startTime_;
    }

    public Date getEndTime() {
        return this.endTime_;
    }

    /**
     * Setters
     */
    public void setTask(String task) {
        this.task_ = task;
    }

    public void setDetails(String details) {
        this.details_ = details;
    }

    public void setStartTime(Date startTime) {
        this.startTime_ = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime_ = endTime;
    }
}
