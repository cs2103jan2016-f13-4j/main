package storage;

import shared.Task;

import java.util.Comparator;

/**
 * 
 * @@author Chng Hui Yie
 *
 */
public class TaskPriorityComparator implements Comparator<Task> {

    @Override public int compare(Task o1, Task o2) {

        // Compare priority value
        if (o1.getPriority().getPriorityValue() > o2.getPriority().getPriorityValue()) {
            return 1;
        } else if (o1.getPriority().getPriorityValue() < o2.getPriority().getPriorityValue()) {
            return -1;
        } else {
            // Priority are equal, compare number of days to deadline
            if (o1.getEndTime().isAfter(o2.getEndTime())) {
                return 1;
            } else if (o1.getEndTime().isBefore(o2.getEndTime())) {
                return -1;
            } else {
                // Priority and deadline are equal, compare number of days since
                // task creation
                if (o1.getCreationTime().isAfter(o2.getCreationTime())) {
                    return 1;
                } else if (o1.getCreationTime().isBefore(o2.getCreationTime())) {
                    return -1;
                } else {
                    // Priority, end time and creation time are all equal
                    // Order by Task ID
                    return o1.getId().compareTo(o2.getId());
                }
            }
        }
    }
}
