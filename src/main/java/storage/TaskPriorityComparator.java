package storage;

import java.util.Comparator;

import shared.Task;

/**
 * 
 * @@author Chng Hui Yie
 *
 */
public class TaskPriorityComparator implements Comparator<Task> {

    private static TaskPriorityComparator instance = new TaskPriorityComparator();

    public static TaskPriorityComparator getInstance() {
        return instance;
    }

    @Override public int compare(Task task1, Task task2) {
        // Completion comparison
        Integer task1Completed = task1.isCompleted() ? 1 : 0;
        Integer task2Completed = task2.isCompleted() ? 1 : 0;
        int completionComparison = task1Completed.compareTo(task2Completed);
        if (completionComparison != 0) {
            return completionComparison;
        }

        // Priority comparison
        int priorityComparison = task2.getPriority().compareTo(task1.getPriority());
        if (priorityComparison != 0) return priorityComparison;

        // Start time comparison
        if (task1.getStartTime() == null ^ task2.getStartTime() == null) {
            if (task1.getStartTime() == null) return 1;
            if (task2.getStartTime() == null) return -1;
        }

        if (task1.getStartTime() != null && task2.getStartTime() != null) {
            int startTimeComparison = task1.getStartTime().compareTo(task2.getStartTime());
            if (startTimeComparison != 0) return startTimeComparison;
        }

        // End time comparison
        if (task1.getEndTime() == null ^ task2.getEndTime() == null) {
            if (task1.getEndTime() == null) return 1;
            if (task2.getEndTime() == null) return -1;
        }

        if (task1.getEndTime() != null && task2.getEndTime() != null) {
            int endTimeComparison = task1.getEndTime().compareTo(task2.getEndTime());
            if (endTimeComparison != 0) return endTimeComparison;
        }

        // Creation time comparison
        return task1.getCreationTime().compareTo(task2.getCreationTime());
    }
}
