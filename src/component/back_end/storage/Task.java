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

    private final int NUMBER_OF_ATTRIBUTES_TO_SERIALIZE = 5; 
    private final String CSV_DELIMITER = ", ";

    public Task(Integer id, String taskName, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.id_ = id;
        this.taskName_ = taskName;
        this.description_ = description;
        this.startTime_ = startTime;
        this.endTime_ = endTime;

        this.creationTime_ = LocalDateTime.now();
    }
    
    public String encodeTaskToString() {
        StringBuilder sb = new StringBuilder();
        String[] attributesArr = this.taskAttributesToStringArray();
        for (String attribute : attributesArr) {
            sb.append(attribute).append(this.CSV_DELIMITER);
        }
        return sb.toString();
    }
    
    public String[] taskAttributesToStringArray() {
        String[] attributesArr = new String[this.NUMBER_OF_ATTRIBUTES_TO_SERIALIZE];
        attributesArr[0] = this.id_.toString();
        attributesArr[1] = this.taskName_;
        attributesArr[2] = this.description_;
        attributesArr[3] = this.startTime_.toString();
        attributesArr[4] = this.endTime_.toString();
        return attributesArr;
    }
    
    public void decodeTaskFromString(String line) {
        // use comma as separator
        String[] taskStringArr = line.split(this.CSV_DELIMITER);
        
        if (taskStringArr.length != 5) {
            throw new IllegalArgumentException();
        }
                
        this.id_ = Integer.parseInt(taskStringArr[0]);
        this.taskName_ = taskStringArr[1];
        this.description_ = taskStringArr[2];
        this.startTime_ = LocalDateTime.parse(taskStringArr[3]);
        this.endTime_ = LocalDateTime.parse(taskStringArr[4]);

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
