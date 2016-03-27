package shared;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @@author Chng Hui Yie
 */
public class Task implements Comparable<Task> {

    /**
     * Constants
     */
    private static final int NUMBER_OF_ATTRIBUTES_TO_SERIALIZE = 5;
    private static final char DELIMITER_CSV = ',';

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

        @Override public String toString() { return Integer.toString(PRIORITY_VALUE); }
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
        this(id, taskName, description, LocalDateTime.now(), startTime, endTime, Priority.LOW);
        this._isDeleted = false;
    }

    // copy ctor, used to construct an identical copy in the clone method
    private Task(Task o) {
        this(o._id, o._taskName, o._description, o._startTime, o._endTime);
    }

    private Task(Integer id, String taskName, String description, LocalDateTime creationTime, LocalDateTime startTime,
                 LocalDateTime endTime, Priority priority) {
        this._id = id;
        this._taskName = taskName;
        this._description = description;
        this._creationTime = creationTime;
        this._startTime = startTime;
        this._endTime = endTime;
        this._priority = priority;
    }

    @Override
    public Task clone() {
        return new Task(this);
    }

    public String encodeTaskToString() {
        return String.join(
                Character.toString(DELIMITER_CSV),
                this.taskAttributesToStringArray()
        );
    }

    private Object[] attributesToSerialize() {
        return new Object[] {
                this._id,
                this._taskName,
                this._description,
                this._creationTime,
                this._startTime,
                this._endTime,
                this._priority
        };
    }

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

    public String[] taskAttributesToStringArray() {
        // Sanitise data
        return Arrays.stream(this.attributesToSerialize())
                .map(this::sanitise)
                .collect(Collectors.toList()).toArray(new String[] {});
    }

    public static Task decodeTaskFromString(String line) {
        // Begin dynamic decoding
        List<String> taskValues = new ArrayList<>();

        int begin = 0;
        boolean isDecodingSpecialValue = false;
        for (int i = 0; i < line.length(); i++) {
            if (i == begin && line.charAt(i) == '"') {
                isDecodingSpecialValue = true;
                continue;
            }
            System.out.printf("[%d] = %s\t%s\n", i, line.charAt(i), isDecodingSpecialValue);
            if (line.charAt(i) == '"' && isDecodingSpecialValue) {
                // Fake quotes
                if (i > 0 && line.charAt(i-1) == '\\') { continue; }

                String specialValue = line.substring(begin+1, i);
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
                begin = i+1;
            }
        }
        // Account for last leftover value
        if (begin < line.length() && begin >= 0) {
            taskValues.add(line.substring(begin));
        }

        // Begin decoding values
        int id = Integer.parseInt(taskValues.get(0));
        String taskName = taskValues.get(1);
        String description = taskValues.get(2);
        LocalDateTime creationTime = LocalDateTime.parse(taskValues.get(3));
        LocalDateTime startTime = taskValues.get(4).trim().isEmpty() ?
                null : LocalDateTime.parse(taskValues.get(4));
        LocalDateTime endTime = taskValues.get(5).trim().isEmpty() ?
                null : LocalDateTime.parse(taskValues.get(5));

        int priorityValue = Integer.parseInt(taskValues.get(6));
        final Priority[] priority = new Priority[] { Priority.LOW };
        Arrays.stream(Priority.values()).filter(p -> p.getPriorityValue() == priorityValue)
                .findFirst().ifPresent(p -> priority[0] = p);

        return new Task(id, taskName, description, creationTime, startTime, endTime, priority[0]);
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
