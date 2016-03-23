package storage;

import java.util.Comparator;

public class TaskPriorityComparator implements Comparator<Task> {

    @Override public int compare(Task o1, Task o2) {
        if (o1.getPriority().getPriorityValue() > o2.getPriority().getPriorityValue()) {
            return 1;
        } else if (o1.getPriority().getPriorityValue() < o2.getPriority().getPriorityValue()) {
            return -1;
        } else {
            // TODO: Priority are equal, compare number of days to deadline
            if (o1.getEndTime().isAfter(o2.getEndTime())) {
                return 1;
            } else if (o1.getEndTime().isBefore(o2.getEndTime())) {
                return -1;
            } else {
                // TODO: Priority and end times are equal, compare number of
                // days since creation of Task
                if (o1.getCreationTime().isAfter(o2.getCreationTime())) {
                    // The earlier the creation time, the more urgent the Task
                    return 1;
                } else if (o2.getCreationTime().isBefore(o2.getCreationTime())) {
                    return -1;
                }
                // Priority, end time and creation time are all equal
                // Order by Task ID
                return o1.getId().compareTo(o2.getId());
            }
        }
    }

}
