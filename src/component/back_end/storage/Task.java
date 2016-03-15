package component.back_end.storage;

import java.time.LocalDateTime;

/**
 * Created by maianhvu on 7/3/16.
 */

public class Task implements Comparable<Task> {
    
    private Integer _id;
    private String _taskName;
    private String _description;
    private final LocalDateTime _creationTime;
    private LocalDateTime _startTime;
    private LocalDateTime _endTime;
    private boolean _isCompleted;

    private final int NUMBER_OF_ATTRIBUTES_TO_SERIALIZE = 5; 
    private final String CSV_DELIMITER = "\", \"";

    public Task(Integer id, String taskName, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this._id = id;
        this._taskName = taskName;
        this._description = description;
        this._startTime = startTime;
        this._endTime = endTime;

        this._creationTime = LocalDateTime.now();
    }
    
    public String encodeTaskToString() {
        StringBuilder sb = new StringBuilder();
        String[] attributesArr = this.taskAttributesToStringArray();
        // the last attribute will be appended outside the loop
        for (int i = 0; i < attributesArr.length - 1; i++) {
            sb.append(attributesArr[i]).append(", ");
        }
        sb.append(attributesArr[attributesArr.length - 1]);
        return sb.toString();
    }
    
    public String[] taskAttributesToStringArray() {
        String[] attributesArr = new String[this.NUMBER_OF_ATTRIBUTES_TO_SERIALIZE];
        
        // wrap strings in quotes
        attributesArr[0] = "\"" + this._id.toString() + "\"";
        attributesArr[1] = "\"" + this._taskName + "\"";
        attributesArr[2] = "\"" + this._description + "\"";
        attributesArr[3] = "\"" + this._startTime.toString() + "\"";
        attributesArr[4] = "\"" + this._endTime.toString() + "\"";
        return attributesArr;
    }
    
    public void decodeTaskFromString(String line) {
        // use comma as separator
        String[] taskStringArr = line.split(this.CSV_DELIMITER);
        
        if (taskStringArr.length != 5) {
            throw new IllegalArgumentException();
        }
        
        // use substring to remove surrounding quotes
        // first array element has the ending quote removed due to the chosen delimiter
        this._id = Integer.parseInt(taskStringArr[0].substring(1, taskStringArr[0].length()));

        // second array element has both starting and ending quotes removed
        this._taskName = taskStringArr[1];
        
        // third array element has both starting and ending quotes removed
        this._description = taskStringArr[2];
        
        // fourth array element has both starting and ending quotes removed
        this._startTime = LocalDateTime.parse(taskStringArr[3]);
                
        // fifth array element has the starting quote removed
        this._endTime = LocalDateTime.parse(taskStringArr[4].substring(0, taskStringArr[4].length() - 1));
        
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
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;
        return this.getId().equals(task.getId());
    }
    
    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    /**
     * Getters
     */
    public Integer getId() {
        return this._id;
    }

    public String getTaskName() {
        return this._taskName;
    }

    public String getDescription() {
        return this._description;
    }

    public LocalDateTime getCreationTime() {
        return this._creationTime;
    }

    public LocalDateTime getStartTime() {
        return this._startTime;
    }

    public LocalDateTime getEndTime() {
        return this._endTime;
    }
    
    public boolean isCompleted() {
        return this._isCompleted;
    }

    /**
     * Setters
     */
    public void setId(Integer id) {
        this._id = id;
    }
    
    public void setCompleted(boolean isCompleted) {
        this._isCompleted = isCompleted;
    }

}
