package storage;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import storage.Task.Priority;

/**
 * 
 * @@author Chng Hui Yie
 *
 */
public class TaskPriorityComparatorTest {

    private Storage storage_;

    @Before public void Set_up() {
        this.storage_ = Storage.getInstance();
        this.storage_.removeAll();
    }

    @Test public void Tasks_with_different_priorities_are_ordered_correctly() {
        Task highPriorityTask = new Task(null, "report submission", null, LocalDateTime.of(2016, 3, 8, 14, 30),
                LocalDateTime.of(2016, 3, 8, 17, 00));
        highPriorityTask.setPriority(Priority.HIGH);
        Task mediumPriorityTask = new Task(null, "sales team meeting", null, LocalDateTime.of(2016, 3, 8, 12, 00),
                LocalDateTime.of(2016, 3, 8, 16, 00));
        mediumPriorityTask.setPriority(Priority.MEDIUM);
        Task lowPriorityTask = new Task(null, "submit v0.1", null, LocalDateTime.of(2016, 3, 7, 12, 30),
                LocalDateTime.of(2016, 3, 8, 23, 59));
        lowPriorityTask.setPriority(Priority.LOW);

        Storage storage = Storage.getInstance();
        storage.save(mediumPriorityTask);
        storage.save(lowPriorityTask);
        storage.save(highPriorityTask);
        List<Task> taskList = storage.getAll();

        // sort
        Collections.sort(taskList, new TaskPriorityComparator());

        // check that high priority task comes before low priority task
        assertEquals(highPriorityTask, taskList.get(0));
        assertEquals(mediumPriorityTask, taskList.get(1));
        assertEquals(lowPriorityTask, taskList.get(2));
    }

    @Test public void Tasks_with_same_priorities_but_different_end_dates_are_ordered_correctly() {
        Task earlierEndTask = new Task(null, "submit reflection", null, LocalDateTime.of(2016, 3, 8, 12, 30),
                LocalDateTime.of(2016, 3, 8, 14, 00));
        Task middleEndTask = new Task(null, "submit v0.1", null, LocalDateTime.of(2016, 3, 8, 12, 30),
                LocalDateTime.of(2016, 3, 8, 23, 59));
        Task laterEndTask = new Task(null, "submit project manual", null, LocalDateTime.of(2016, 3, 7, 12, 30),
                LocalDateTime.of(2016, 3, 9, 7, 00));
        Storage storage = Storage.getInstance();
        storage.save(middleEndTask);
        storage.save(laterEndTask);
        storage.save(earlierEndTask);
        List<Task> taskList = storage.getAll();

        // sort
        Collections.sort(taskList, new TaskPriorityComparator());

        // check that task with the earliest deadline comes first
        assertEquals(earlierEndTask, taskList.get(0));
        assertEquals(middleEndTask, taskList.get(1));
        assertEquals(laterEndTask, taskList.get(2));
    }
}
