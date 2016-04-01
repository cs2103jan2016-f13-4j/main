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
        int priorityComparison = task1.getPriority().compareTo(task2.getPriority());
        if (priorityComparison != 0) return priorityComparison;

        // Start time comparison
        if (task1.getStartTime() == null ^ task2.getStartTime() == null) {
            if (task1.getStartTime() == null) return -1;
            if (task2.getStartTime() == null) return 1;
        }

        if (task1.getStartTime() != null && task2.getStartTime() != null) {
            return task1.getStartTime().compareTo(task2.getStartTime());
        }

        // End time comparison
        if (task1.getEndTime() == null ^ task2.getEndTime() == null) {
            if (task1.getEndTime() == null) return -1;
            if (task2.getEndTime() == null) return 1;
        }

        if (task1.getEndTime() != null && task2.getEndTime() != null) {
            return task1.getEndTime().compareTo(task2.getEndTime());
        }

        // Creation time comparision
        return task1.getCreationTime().compareTo(task2.getCreationTime());
    }
}
