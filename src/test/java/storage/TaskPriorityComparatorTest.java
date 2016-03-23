package storage;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import exception.ExceptionHandler;
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

        this.storage_.save(mediumPriorityTask);
        this.storage_.save(lowPriorityTask);
        this.storage_.save(highPriorityTask);
        List<Task> taskList = this.storage_.getAll();

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
        this.storage_.save(middleEndTask);
        this.storage_.save(laterEndTask);
        this.storage_.save(earlierEndTask);
        List<Task> taskList = this.storage_.getAll();

        // sort
        Collections.sort(taskList, new TaskPriorityComparator());

        // check that task with the earliest deadline comes first
        assertEquals(earlierEndTask, taskList.get(0));
        assertEquals(middleEndTask, taskList.get(1));
        assertEquals(laterEndTask, taskList.get(2));
    }

    @Test public void Tasks_with_same_priorites_and_same_end_dates_but_different_creation_times_are_ordered_correctly() {

        try {
            Task earlierCreationTask = new Task(null, "submit progress report", null,
                    LocalDateTime.of(2016, 3, 9, 12, 30), LocalDateTime.of(2016, 3, 9, 13, 00));
            Thread.sleep(3000); // sleep for 3 seconds
            Task middleCreationTask = new Task(null, "marketing pitch", null, LocalDateTime.of(2016, 3, 8, 12, 30),
                    LocalDateTime.of(2016, 3, 9, 13, 00));
            Thread.sleep(3000); // sleep for 3 seconds
            Task laterCreationTask = new Task(null, "sales meeting", null, LocalDateTime.of(2016, 3, 7, 12, 30),
                    LocalDateTime.of(2016, 3, 9, 13, 00));

            this.storage_.save(middleCreationTask);
            this.storage_.save(laterCreationTask);
            this.storage_.save(earlierCreationTask);
            List<Task> taskList = this.storage_.getAll();

            // sort
            Collections.sort(taskList, new TaskPriorityComparator());

            // check that task with the earliest creation time comes first
            assertEquals(earlierCreationTask, taskList.get(0));
            assertEquals(middleCreationTask, taskList.get(1));
            assertEquals(laterCreationTask, taskList.get(2));

        } catch (InterruptedException e) {
            ExceptionHandler.handle(e);
        }

    }

    @Test public void Tasks_with_same_priority_end_times_and_creation_times_are_sorted_correctly() {
        Task taskIndex1 = new Task(null, "report submission", null, LocalDateTime.of(2016, 3, 8, 14, 30),
                LocalDateTime.of(2016, 3, 8, 17, 00));
        Task taskIndex2 = new Task(null, "sales team meeting", null, LocalDateTime.of(2016, 3, 8, 12, 00),
                LocalDateTime.of(2016, 3, 8, 17, 00));
        Task taskIndex3 = new Task(null, "submit v0.1", null, LocalDateTime.of(2016, 3, 7, 12, 30),
                LocalDateTime.of(2016, 3, 8, 17, 00));

        this.storage_.save(taskIndex1);
        this.storage_.save(taskIndex2);
        this.storage_.save(taskIndex3);
        List<Task> taskList = this.storage_.getAll();

        // sort
        Collections.sort(taskList, new TaskPriorityComparator());

        // check that task with index 1 comes first
        assertEquals(taskIndex1, taskList.get(0));
        assertEquals(taskIndex2, taskList.get(1));
        assertEquals(taskIndex3, taskList.get(2));
    }

}
