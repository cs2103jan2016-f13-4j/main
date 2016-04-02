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
        System.out.println("Enter compare method!");

        // Priority comparison
        int priorityComparison = task2.getPriority().compareTo(task1.getPriority());
        System.out.println("Enter priority comparison");
        if (priorityComparison != 0) return priorityComparison;

        // Start time comparison
        if (task1.getStartTime() == null ^ task2.getStartTime() == null) {
            System.out.println("Enter start time null comparison");
            if (task1.getStartTime() == null) return 1;
            if (task2.getStartTime() == null) return -1;
        }

        if (task1.getStartTime() != null && task2.getStartTime() != null) {
            System.out.println("Enter start time comparison");
            int startTimeComparison = task1.getStartTime().compareTo(task2.getStartTime());
            if (startTimeComparison != 0) return startTimeComparison;
        }

        // End time comparison
        if (task1.getEndTime() == null ^ task2.getEndTime() == null) {
            System.out.println("Enter end time null comparison");
            if (task1.getEndTime() == null) return 1;
            if (task2.getEndTime() == null) return -1;
        }

        if (task1.getEndTime() != null && task2.getEndTime() != null) {
            System.out.println("task 1: " + task1.getEndTime().toString());
            System.out.println("task 2: " + task2.getEndTime().toString());
            System.out.println("Enter end time null comparison");
            int endTimeComparison = task1.getEndTime().compareTo(task2.getEndTime());
            if (endTimeComparison != 0) return endTimeComparison;
        }

        // Creation time comparison
        System.out.println("Enter creation time null comparison");
        return task1.getCreationTime().compareTo(task2.getCreationTime());
    }
}
