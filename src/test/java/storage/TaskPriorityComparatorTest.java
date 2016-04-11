package storage;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import exception.ExceptionHandler;
import shared.CustomTime;
import shared.Task;
import shared.Task.Priority;

/**
 * Tests for TaskPriorityComparator class
 *
 * @@author A0127357B
 */
public class TaskPriorityComparatorTest {

    private Storage storage_;

    @Before public void Set_up() {
        this.storage_ = Storage.getInstance();
        this.storage_.removeAll();
    }

    @Test public void Tasks_with_different_priorities_are_ordered_correctly() {
        CustomTime standardStartTime = new CustomTime(LocalDateTime.of(2016, 3, 2, 11, 59));
        CustomTime standardEndTime = new CustomTime(LocalDateTime.of(2016, 4, 20, 18, 00));

        Task highPriorityTask = new Task(null, "report submission", null, standardStartTime, standardEndTime);
        highPriorityTask.setPriority(Priority.HIGH);
        Task mediumPriorityTask = new Task(null, "sales team meeting", null, standardStartTime, standardEndTime);
        mediumPriorityTask.setPriority(Priority.MEDIUM);
        Task lowPriorityTask = new Task(null, "submit v0.1", null, standardStartTime, standardEndTime);
        lowPriorityTask.setPriority(Priority.LOW);

        this.storage_.save(mediumPriorityTask); // mediumPriorityTask ID: 1
        this.storage_.save(lowPriorityTask); // lowPriorityTask ID: 2
        this.storage_.save(highPriorityTask); // highPriorityTask ID: 3
        List<Task> taskList = this.storage_.getAll();

        // sort
        Collections.sort(taskList, new TaskPriorityComparator());

        // check that high priority task comes before low priority task
        assertEquals(highPriorityTask, taskList.get(0));
        assertEquals(mediumPriorityTask, taskList.get(1));
        assertEquals(lowPriorityTask, taskList.get(2));
    }

    @Test public void Mix_of_tasks_with_priority_values_and_null_priorities_are_ordered_correctly() {
        CustomTime standardStartTime = new CustomTime(LocalDateTime.of(2016, 3, 30, 10, 00));
        CustomTime standardEndTime = new CustomTime(LocalDateTime.of(2016, 4, 1, 14, 30));

        // default priority is null
        Task nullPriorityTask = new Task(null, "draft progress report", null, standardStartTime, standardEndTime);
        Task lowPriorityTask = new Task(null, "make software demo slides", null, standardStartTime, standardEndTime);
        lowPriorityTask.setPriority(Priority.LOW);
        Task highPriorityTask = new Task(null, "v0.4 milestone delivery", null, standardStartTime, standardEndTime);
        highPriorityTask.setPriority(Priority.HIGH);

        this.storage_.save(nullPriorityTask); // nullPriorityTask ID: 1
        this.storage_.save(lowPriorityTask); // lowPriorityTask ID: 2
        this.storage_.save(highPriorityTask); // highPriorityTask ID: 3
        List<Task> taskList = this.storage_.getAll();

        // sort
        Collections.sort(taskList, new TaskPriorityComparator());

        // check that low priority task comes last, right below null priority task
        assertEquals(highPriorityTask, taskList.get(0));
        assertEquals(nullPriorityTask, taskList.get(1));
        assertEquals(lowPriorityTask, taskList.get(2));
    }

    @Test public void Tasks_with_same_priorities_but_different_start_times_are_ordered_correctly() {
        CustomTime standardEndTime = new CustomTime(LocalDateTime.of(2016, 4, 20, 18, 00));

        Task earlyStartTask = new Task(null, "proposal v0.1", null, new CustomTime(LocalDateTime.of(2016, 4, 1, 12, 00)), standardEndTime);
        Task middleStartTask = new Task(null, "project manual", null, new CustomTime(LocalDateTime.of(2016, 4, 2, 15, 30)), standardEndTime);
        Task lateStartTask = new Task(null, "oral presentation", null, new CustomTime(LocalDateTime.of(2016, 4, 3, 11, 00)), standardEndTime);
        this.storage_.save(lateStartTask); // lateStartTask ID: 1
        this.storage_.save(earlyStartTask); // earlyStartTask ID: 2
        this.storage_.save(middleStartTask); // middleStartTask ID: 3
        List<Task> taskList = this.storage_.getAll();

        // sort
        Collections.sort(taskList, new TaskPriorityComparator());

        // check that task with earlier start time comes before task with later start time
        assertEquals(earlyStartTask, taskList.get(0)); // earlyStartTask
        assertEquals(middleStartTask, taskList.get(1)); // middleStartTask
        assertEquals(lateStartTask, taskList.get(2)); // lateStartTask
    }

    @Test public void Tasks_are_ordered_correctly_when_start_time_is_null() {
        CustomTime standardEndTime = new CustomTime(LocalDateTime.of(2016, 3, 21, 23, 00));

        Task earlyStartTask = new Task(null, "v0.1 demo", null, new CustomTime(LocalDateTime.of(2016, 3, 1, 13, 00)), standardEndTime);
        Task lateStartTask = new Task(null, "v0.2 demo", null, new CustomTime(LocalDateTime.of(2016, 3, 7, 12, 30)), standardEndTime);
        Task nullStartTask = new Task(null, "future developments of Your MOM", null, null, standardEndTime);
        this.storage_.save(lateStartTask); // lateStartTask ID: 1
        this.storage_.save(nullStartTask); // nullStartTask ID: 2
        this.storage_.save(earlyStartTask); // earlyStartTask ID: 3
        List<Task> taskList = this.storage_.getAll();

        // sort
        Collections.sort(taskList, new TaskPriorityComparator());

        // check that task with early start time comes first, task with null start time comes last
        assertEquals(earlyStartTask, taskList.get(0));
        assertEquals(lateStartTask, taskList.get(1));
        assertEquals(nullStartTask, taskList.get(2));
    }

    @Test public void Tasks_with_same_priorities_and_start_times_but_different_end_times_are_ordered_correctly() {
        CustomTime standardStartTime = new CustomTime(LocalDateTime.of(2016, 3, 2, 11, 59));

        Task earlyEndTask = new Task(null, "reflection 1", null, standardStartTime, new CustomTime(LocalDateTime.of(2016, 3, 7, 17, 00)));
        Task middleEndTask = new Task(null, "progress report", null, standardStartTime, new CustomTime(LocalDateTime.of(2016, 3, 14, 20, 00)));
        Task lateEndTask = new Task(null, "reflection 2", null, standardStartTime, new CustomTime(LocalDateTime.of(2016, 3, 21, 23, 59)));
        this.storage_.save(middleEndTask); // middleEndTask ID: 1
        this.storage_.save(lateEndTask); // lateEndTask ID: 2
        this.storage_.save(earlyEndTask); // earlyEndTask ID: 3
        List<Task> taskList = this.storage_.getAll();

        // sort
        Collections.sort(taskList, new TaskPriorityComparator());

        // check that task with earlier end time comes before task with later end time
        assertEquals(earlyEndTask, taskList.get(0)); // earlyEndTask
    }

    @Test public void Tasks_are_ordered_correctly_when_end_time_is_null() {
        CustomTime standardStartTime = new CustomTime(LocalDateTime.of(2016, 3, 9, 23, 59));

        Task earlyEndTask = new Task(null, "v0.1 demo", null, standardStartTime, new CustomTime(LocalDateTime.of(2016, 3, 20, 12, 00)));
        Task lateEndTask = new Task(null, "v0.2 demo", null, standardStartTime, new CustomTime(LocalDateTime.of(2016, 4, 1, 23, 59)));
        Task nullEndTask = new Task(null, "future developments of Your MOM", null, standardStartTime, null);
        this.storage_.save(nullEndTask); // nullEndTask ID: 1
        this.storage_.save(earlyEndTask); // earlyEndTask ID: 2
        this.storage_.save(lateEndTask); // lateEndTask ID: 3
        List<Task> taskList = this.storage_.getAll();

        // sort
        Collections.sort(taskList, new TaskPriorityComparator());

        // check that task with early start time comes first, task with null start time comes last
        assertEquals(earlyEndTask, taskList.get(0));
        assertEquals(lateEndTask, taskList.get(1));
        assertEquals(nullEndTask, taskList.get(2));
    }

    @Test public void Tasks_with_same_priorities_and_start_times_and_same_end_times_but_different_creation_times_are_ordered_correctly() {
        CustomTime standardStartTime = new CustomTime(LocalDateTime.of(2016, 3, 9, 12, 30));
        CustomTime standardEndTime = new CustomTime(LocalDateTime.of(2016, 3, 9, 13, 00));

        try {
            Task earlierCreationTask = new Task(null, "submit progress report", null,
                    standardStartTime, standardEndTime);
            Thread.sleep(3000); // sleep for 3 seconds
            Task middleCreationTask = new Task(null, "marketing pitch", null,
                    standardStartTime, standardEndTime);
            Thread.sleep(3000); // sleep for 3 seconds
            Task laterCreationTask = new Task(null, "sales meeting", null,
                    standardStartTime, standardEndTime);

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

}
