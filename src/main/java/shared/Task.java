package shared;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @@author A0127357B
 */
public class Task implements Comparable<Task> {

    /**
     * Constants
     */
    private static final char DELIMITER_CSV = ',';

    /**
     * Properties
     */
    private Integer _id;
    private String _taskName;
    private String _description;
    private CustomTime _startTime;
    private CustomTime _endTime;
    private final LocalDateTime _creationTime;
    private Boolean _isCompleted;
    private Priority _priority;
    private boolean _isDeleted;

    /**
     * Priority types
     * 
     * @@author A0127357B
     *
     */
    public enum Priority {
        LOW(0), NULL(1), MEDIUM(2), HIGH(3);

        private final int _priorityValue;

        Priority(int priority) {
            this._priorityValue = priority;
        }

        public int getPriorityValue() {
            return this._priorityValue;
        }

        @Override public String toString() {
            return Integer.toString(_priorityValue);
        }
    };

    /**
     * Construct a new Task instance.
     *
     * @param id
     * @param taskName
     * @param description
     * @param startTime
     * @param endTime
     */
    public Task(Integer id, String taskName, String description, CustomTime startTime, CustomTime endTime) {
        this(id, taskName, description, LocalDateTime.now(), startTime, endTime, false, Priority.NULL, false);
    }

    /**
     * Construct a new Task instance.
     *
     * @param id
     * @param taskName
     * @param description
     * @param startTime
     * @param endTime
     */
    public Task(Integer id, String taskName, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this(id, taskName, description, new CustomTime(startTime), new CustomTime(endTime));
    }

    /**
     * Copy constructor that constructs an identical copy in the clone method.
     *
     * @param o
     */
    private Task(Task o) {
        this(o._id,
                o._taskName,
                o._description,
                o._creationTime,
                o._startTime,
                o._endTime,
                o._isCompleted,
                o._priority,
                o._isDeleted);
    }

    /**
     * Construct a new Task instance.
     *
     * @param id
     * @param taskName
     * @param description
     * @param creationTime
     * @param startTime
     * @param endTime
     * @param isCompleted
     * @param priority
     * @param isDeleted
     */
    private Task(Integer id, String taskName, String description, LocalDateTime creationTime, CustomTime startTime,
            CustomTime endTime, boolean isCompleted, Priority priority, boolean isDeleted) {
        this._id = id;
        this._taskName = taskName;
        this._description = description;
        this._creationTime = creationTime;
        this._startTime = startTime;
        this._endTime = endTime;
        this._isCompleted = isCompleted;
        this._priority = priority;
        this._isDeleted = isDeleted;
    }

    /**
     * Clones a Task instance.
     *
     * @return the cloned Task
     */
    @Override public Task clone() {
        return new Task(this);
    }

    // ----------------------------------------------------------------------------------------
    //
    // Methods for encoding Task to String
    //
    // ----------------------------------------------------------------------------------------

    /**
     * Encodes Task instance to String.
     *
     * @return String representation of the Task instance
     */
    public String encodeTaskToString() {
        return String.join(Character.toString(DELIMITER_CSV), this.taskAttributesToStringArray());
    }

    private Object[] attributesToSerialize() {
        return new Object[] { this._id, this._taskName, this._description, this._creationTime, this._startTime,
                this._endTime, this._isCompleted, this._priority };
    }

    /**
     * Sanitise Task attribute to output as String.
     *
     * @param attribute
     * @return sanitised String representation of the Task attribute
     */
    private String sanitise(Object attribute) {
        // Empty or null objects are treated similarly
        if (attribute == null) {
            return "";
        }

        // Convert the object to String
        String sanitised = attribute.toString();

        // Detect presence of commas
        if (sanitised.contains(Character.toString(DELIMITER_CSV))) {
            // Sanitise backslashes
            sanitised = sanitised.replace("\\", "\\\\");
            // Sanitise quotes
            sanitised = sanitised.replace("\"", "\\\"");
            // Has space, wrap around quotes
            sanitised = String.format("\"%s\"", sanitised);
        }

        return sanitised;
    }

    /**
     * Converts attributes of this Task to a new String array.
     *
     * @return a String array representation of Task attributes
     */
    public String[] taskAttributesToStringArray() {
        // Sanitise data
        return Arrays.stream(this.attributesToSerialize()).map(this::sanitise).collect(Collectors.toList())
                .toArray(new String[] {});
    }

    // ----------------------------------------------------------------------------------------
    //
    // Methods for decoding Task from String
    //
    // ----------------------------------------------------------------------------------------

    /**
     * Decodes Task instance from a line of CSV.
     *
     * @param line
     *              CSV representation of Task instance
     * @return Task instance representation by the line of CSV
     */
    public static Task decodeTaskFromString(String line) {
        List<String> taskValues = decodeStringFromCsv(line);

        // Decode Task attributes from String array
        int id = decodeTaskID(taskValues.get(0));
        String taskName = taskValues.get(1);
        String description = taskValues.get(2);
        LocalDateTime creationTime = decodeCreationTime(taskValues.get(3));
        CustomTime startTime = decodeStartTimeOrEndTime(taskValues.get(4));
        CustomTime endTime = decodeStartTimeOrEndTime(taskValues.get(5));
        boolean isCompleted = decodeIsCompleted(taskValues.get(6));
        Priority priority = decodePriority(taskValues.get(7));

        return new Task(id, taskName, description, creationTime, startTime, endTime, isCompleted, priority, false);
    }

    /**
     * Decodes a line of CSV into String representation of Task attributes.
     *
     * @param line
     *              CSV representation of Task attributes
     * @return a List of String representation of Task attributes
     */
    public static List<String> decodeStringFromCsv(String line) {
        List<String> taskValues = new ArrayList<>();

        int begin = 0;
        boolean isDecodingSpecialValue = false;
        for (int i = 0; i < line.length(); i++) {
            if (i == begin && line.charAt(i) == '"') {
                isDecodingSpecialValue = true;
                continue;
            }
            if (line.charAt(i) == '"' && isDecodingSpecialValue) {
                // Fake quotes
                if (i > 0 && line.charAt(i - 1) == '\\') {
                    continue;
                }

                String specialValue = line.substring(begin + 1, i);
                specialValue = specialValue.replace("\\\"", "\"");
                specialValue = specialValue.replace("\\\\", "\\");
                taskValues.add(specialValue);

                begin = i + 2;
                i += 1;
                isDecodingSpecialValue = false;
                continue;
            }
            if (line.charAt(i) == DELIMITER_CSV && !isDecodingSpecialValue) {
                String value = line.substring(begin, i);
                taskValues.add(value);
                begin = i + 1;
            }
        }
        // Account for last leftover value
        if (begin < line.length() && begin >= 0) {
            taskValues.add(line.substring(begin));
        }

        return taskValues;
    }

    /**
     * Decodes Task ID from its String representation.
     *
     * @param id
     *            String representation of Task ID
     * @return assigned ID of Task instance
     */
    public static int decodeTaskID(String id) {
        return Integer.parseInt(id);
    }

    /**
     * Decodes Task creation time from its String representation.
     *
     * @param creationTime
     *                      String representation of Task creation time
     * @return creation time of Task instance
     */
    public static LocalDateTime decodeCreationTime(String creationTime) {
        return LocalDateTime.parse(creationTime);
    }

    /**
     * Decodes Task start time or end time from its String representation.
     *
     * @param time
     *              String representation of Task start time or end time
     * @return CustomTime
     */
    public static CustomTime decodeStartTimeOrEndTime(String time) {
        return CustomTime.fromString(time.trim());
    }

    /**
     * Decodes Task isCompleted status from its String representation.
     *
     * @param isCompleted
     *                     String representation of Task isCompleted status
     * @return isCompleted status of Task instance
     */
    public static boolean decodeIsCompleted(String isCompleted) {
        return isCompleted.trim().toLowerCase().equals("true");
    }

    /**
     * Decodes Task priority from its String representation.
     *
     * @param priority
     *                  String representation of Task priority
     * @return priority of Task instance
     */
    public static Priority decodePriority(String priority) {
        final Priority[] priorityArr = new Priority[] { Priority.LOW };
        int priorityValue = Integer.parseInt(priority);
        Arrays.stream(Priority.values()).filter(p -> p.getPriorityValue() == priorityValue).findFirst()
                .ifPresent(p -> priorityArr[0] = p);
        return priorityArr[0];
    }

    /**
     * Compare this object with the specified object for order
     *
     * @param o
     *           the object to be compared
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
     * or greater than the specified object.
     */
    @Override public int compareTo(Task o) {
        return this.getId().compareTo(o.getId());
    }

    /**
     * Indicates whether some other object is "equal to" this one
     *
     * @param o
     *           the reference object with which to compare
     * @return true if this object is the same as the o argument; false otherwise
     */
    @Override public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }

        Task task = (Task) o;
        return this.getId().equals(task.getId());
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     */
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

    public CustomTime getStartTime() {
        return this._startTime;
    }

    public CustomTime getEndTime() {
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

    public void setStartTime(CustomTime start) {
        this._startTime = start;
    }

    public void setEndTime(CustomTime end) {
        this._endTime = end;
    }

    public void setPriority(Priority priority) {
        this._priority = priority;
    }

    public void setCompleted(boolean isCompleted) {
        this._isCompleted = isCompleted;
    }
}
