package storage;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import storage.Task.Priority;

public class TaskPriorityComparatorTest {
    @Test public void Tasks_with_different_priorities_are_ordered_correctly() {
        Task highPriorityTask = new Task(null, "report submission", null, LocalDateTime.of(2016, 3, 8, 14, 30),
                LocalDateTime.of(2016, 3, 8, 17, 00));
        highPriorityTask.setPriority(Priority.HIGH);
        Task lowPriorityTask = new Task(null, "sales team meeting", null, LocalDateTime.of(2016, 3, 8, 12, 00),
                LocalDateTime.of(2016, 3, 8, 16, 00));
        lowPriorityTask.setPriority(Priority.MEDIUM);
        Storage storage = Storage.getInstance();
        storage.save(lowPriorityTask); // save low priority task first
        storage.save(highPriorityTask);
        List<Task> taskList = storage.getAll();

        // sort
        Collections.sort(taskList, new TaskPriorityComparator());

        // check that high priority task comes before low priority task
        assertEquals(highPriorityTask, taskList.get(0));
    }
}
