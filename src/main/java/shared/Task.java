package shared;

import java.time.LocalDateTime;
import java.util.ArrayList;

import exception.ExceptionHandler;

/**
 * @@author Chng Hui Yie
 */
public class Task implements Comparable<Task> {

    /**
     * Constants
     */
    private final int NUMBER_OF_ATTRIBUTES_TO_SERIALIZE = 5;
    private final String CSV_DELIMITER = ",";

    /**
     * Properties
     */
    private Integer _id;
    private String _taskName;
    private String _description;
    private LocalDateTime _startTime;
    private LocalDateTime _endTime;
    private final LocalDateTime _creationTime;
    private boolean _isCompleted;
    private Priority _priority;
    private boolean _isDeleted;

    /**
     * Priority types
     * 
     * @author Huiyie
     *
     */
    public enum Priority {
        // We want Tasks with high priority to appear nearer the start of the
        // list
        LOW(3), MEDIUM(2), HIGH(1); // assign the smallest numeric value to
                                    // "high"

        private final int PRIORITY_VALUE;

        Priority(int priority) {
            this.PRIORITY_VALUE = priority;
        }

        public int getPriorityValue() {
            return this.PRIORITY_VALUE;
        }
    };

    /**
     * TODO: Write JavaDoc
     * 
     * @param id
     * @param taskName
     * @param description
     * @param startTime
     * @param endTime
     */
    public Task(Integer id, String taskName, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this._id = id;
        this._taskName = taskName;
        this._description = description;
        this._startTime = startTime;
        this._endTime = endTime;
        this._priority = Priority.LOW; // default priority is set to low
        this._creationTime = LocalDateTime.now();
        this._isDeleted = false;
    }

    // copy ctor, used to construct an identical copy in the clone method
    private Task(Task o) {
        this(o._id, o._taskName, o._description, o._startTime, o._endTime);
    }

    @Override public Task clone() {
        return new Task(this);
    }

    // ----------------------------------------------------------------------------------------
    //
    // Methods for encoding Task to String
    //
    // ----------------------------------------------------------------------------------------

    public String encodeTaskToString() {
        StringBuilder sb = new StringBuilder();
        String[] attributesArr = this.taskAttributesToStringArray();
        // the last attribute will be appended outside the loop
        for (int i = 0; i < attributesArr.length - 1; i++) {
            sb.append(attributesArr[i]).append(",");
        }
        sb.append(attributesArr[attributesArr.length - 1]);
        return sb.toString();
    }

    public String[] taskAttributesToStringArray() {
        String[] attributesArr = new String[this.NUMBER_OF_ATTRIBUTES_TO_SERIALIZE];

        attributesArr[0] = this._id.toString(); // int value, does not contain
                                                // special characters

        if (checkContainsSpecialCharacters(this._taskName)) {
            attributesArr[1] = "\"" + escapeSpecialCharacters(this._taskName) + "\"";

        } else {
            attributesArr[1] = this._taskName;
        }

        if (checkContainsSpecialCharacters(this._description)) {
            attributesArr[2] = "\"" + escapeSpecialCharacters(this._description) + "\"";
        } else {
            attributesArr[2] = this._description;
        }

        // LocalDateTime does not contain special characters
        attributesArr[3] = this._startTime.toString();
        attributesArr[4] = this._endTime.toString();
        return attributesArr;
    }

    public boolean checkContainsSpecialCharacters(String attribute) {
        // special characters are comma, quote and backslash
        if (attribute.contains(",") || attribute.contains("\"") || attribute.contains("\\")) {
            return true;
        } else {
            return false;
        }
    }

    public String escapeSpecialCharacters(String attribute) {
        // Convert all backslashes to double backslashes
        attribute = attribute.replace("\\", "\\\\");

        // Convert all quotes to backslash quote
        attribute = attribute.replace("\"", "\\\"");

        return attribute;
    }

    // ----------------------------------------------------------------------------------------
    //
    // Methods for decoding Task from String
    //
    // ----------------------------------------------------------------------------------------

    public void decodeTaskFromString(String line) {
        ArrayList<String> taskAttributesList = new ArrayList<String>();
        int start = 0;
        String tempTaskString = "";

        for (int end = 1; end < line.length(); end++) {
            char startChar = line.charAt(start);
            char endChar = line.charAt(end);

            // Case 1: Substring is not enclosed with quotes, is not the last
            // attribute
            if (startChar != '\"' && endChar == ',' && line.charAt(end - 1) != '\\') {
                tempTaskString = line.substring(start, end);
                taskAttributesList.add(tempTaskString);
                start = end + 1;
                end = end + 2;
            }
            // Case 2: Substring is enclosed with quotes, is not the last
            // attribute
            else if (startChar == '\"'
                    && ((endChar == '\"' && line.charAt(end - 1) == '\\' && line.charAt(end - 2) == '\\')
                            || (endChar == '\"' && line.charAt(end - 1) != '\\'))
                    && line.charAt(end + 1) == ',') {
                tempTaskString = line.substring(start, end + 1);
                taskAttributesList.add(tempTaskString);
                start = end + 2;
                end = end + 3;
            }
            // Case 3: Substring is the last attribute
            else if ((startChar != '\"' && end == line.length() - 1)
                    || (startChar == '\"' && endChar == '\"' && end == line.length() - 1)) {
                tempTaskString = line.substring(start);
                taskAttributesList.add(tempTaskString);
            }
        }

        checkNumberOfDecodedAttributes(taskAttributesList);

        // get rid of extra backslashes that were added in during the Task
        // encoding process
        taskAttributesList = removeAdditionalBackslashes(taskAttributesList);

        assignDecodedValuesToAttributes(taskAttributesList);
    }

    public void checkNumberOfDecodedAttributes(ArrayList<String> taskAttributesList) {
        try {
            if (taskAttributesList.size() != 5) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handle(e);
        }
    }

    public void assignDecodedValuesToAttributes(ArrayList<String> taskAttributesList) {
        for (int i = 0; i < this.NUMBER_OF_ATTRIBUTES_TO_SERIALIZE; i++) {
            String tempAttribute = taskAttributesList.get(i);
            tempAttribute = removeSurroundingQuotes(tempAttribute);

            switch (i) {
            case 0:
                this._id = Integer.parseInt(tempAttribute);
                break;
            case 1:
                this._taskName = tempAttribute;
                break;
            case 2:
                this._description = tempAttribute;
                break;
            case 3:
                this._startTime = LocalDateTime.parse(tempAttribute);
                break;
            case 4:
                this._endTime = LocalDateTime.parse(tempAttribute);
                break;
            }
        }
    }

    public ArrayList<String> removeAdditionalBackslashes(ArrayList<String> unprocessedList) {
        ArrayList<String> processedList = new ArrayList<String>();
        for (String attribute : unprocessedList) {
            // Convert double backslashes to backslash
            attribute = attribute.replace("\\\\", "\\");

            // Convert backslash quote to quote
            attribute = attribute.replace("\\\"", "\"");

            processedList.add(attribute);
        }
        return processedList;
    }

    public String removeSurroundingQuotes(String attributeString) {
        if (attributeString.charAt(0) == '\"' && attributeString.charAt(attributeString.length() - 1) == '\"') {
            attributeString = attributeString.substring(1, attributeString.length() - 1);
        }
        return attributeString;
    }

    @Override public int compareTo(Task o) {
        return this.getId().compareTo(o.getId());
    }

    @Override public boolean equals(Object o) {
        if (o == null)
            return false;
        if (this == o)
            return true;
        if (!(o instanceof Task))
            return false;

        Task task = (Task) o;
        return this.getId().equals(task.getId());
    }

    @Override public int hashCode() {
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

    public Priority getPriority() {
        return this._priority;
    }

    public boolean isCompleted() {
        return this._isCompleted;
    }

    public boolean isDeleted() {
        return this._isDeleted;
    }

    /**
     * Setters
     */
    public void setDeletedStatus(boolean isDeleted) {
        this._isDeleted = isDeleted;
    }

    public void setId(Integer id) {
        this._id = id;
    }

    public void setTaskName(String taskname) {
        this._taskName = taskname;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public void setStartTime(LocalDateTime start) {
        this._startTime = start;
    }

    public void setEndTime(LocalDateTime end) {
        this._endTime = end;
    }

    public void setPriority(Priority priority) {
        this._priority = priority;
    }

    public void setCompleted(boolean isCompleted) {
        this._isCompleted = isCompleted;
    }
}
