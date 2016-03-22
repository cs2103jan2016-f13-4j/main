package storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import exception.PrimaryKeyNotFoundException;

/**
 * 
 * @author Huiyie
 *
 */

public class TaskCollectionTest {

    private TaskCollection taskCollection;

    private final String TASK_1_NAME = "homework";
    private final String TASK_1_DESCRIPTION = "cs2103t";
    private final LocalDateTime TASK_1_START = LocalDateTime.of(2016, 3, 4, 14, 30);
    private final LocalDateTime TASK_1_END = LocalDateTime.of(2016, 3, 5, 14, 30);
    private Task task1_;

    private final String TASK_2_NAME = "assignment";
    private final String TASK_2_DESCRIPTION = "cs3230";
    private final LocalDateTime TASK_2_START = LocalDateTime.of(2016, 3, 5, 14, 30);
    private final LocalDateTime TASK_2_END = LocalDateTime.of(2016, 3, 6, 14, 30);
    private Task task2_;

    private final String TASK_3_NAME = "tutorial";
    private final String TASK_3_DESCRIPTION = "nm2101";
    private final LocalDateTime TASK_3_START = LocalDateTime.of(2016, 3, 6, 14, 30);
    private final LocalDateTime TASK_3_END = LocalDateTime.of(2016, 3, 7, 14, 30);
    private Task task3_;

    private final String TASK_4_NAME = "tutorial";
    private final String TASK_4_DESCRIPTION = "nm2101";
    private final LocalDateTime TASK_4_START = LocalDateTime.of(2016, 3, 7, 14, 30);
    private final LocalDateTime TASK_4_END = LocalDateTime.of(2016, 3, 8, 14, 30);
    private Task task4_;

    private final String TASK_5_NAME = "tutorial";
    private final String TASK_5_DESCRIPTION = "nm2101";
    private final LocalDateTime TASK_5_START = LocalDateTime.of(2016, 3, 8, 14, 30);
    private final LocalDateTime TASK_5_END = LocalDateTime.of(2016, 3, 9, 14, 30);
    private Task task5_;

    @Before public void setUp() throws IOException {
        // Ensure task write directory exists
        (new File("tmp/testWrite")).mkdirs();

        this.taskCollection = TaskCollection.getInstance();

        // Clear all pre-existing data in TaskCollection
        this.taskCollection.removeAll();

        this.task1_ = new Task(null, this.TASK_1_NAME, this.TASK_1_DESCRIPTION, this.TASK_1_START, this.TASK_1_END);
        this.task2_ = new Task(null, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this.task3_ = new Task(null, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);
        this.task4_ = new Task(null, this.TASK_4_NAME, this.TASK_4_DESCRIPTION, this.TASK_4_START, this.TASK_4_END);
        this.task5_ = new Task(null, this.TASK_5_NAME, this.TASK_5_DESCRIPTION, this.TASK_5_START, this.TASK_5_END);

        this.taskCollection.save(this.task1_);
        this.taskCollection.save(this.task2_);
        this.taskCollection.save(this.task3_);
        this.taskCollection.save(this.task4_);
        this.taskCollection.save(this.task5_);
    }

    // ----------------------------------------------------------------------------------------
    //
    // I. Save Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Save_returns_correct_Task_ID() {
        int returnedID = this.taskCollection.save(this.task1_);
        assertEquals(1, returnedID);
    }

    @Test public void Save_sets_ID_when_ID_is_null() throws PrimaryKeyNotFoundException {
        // create Task with null value as ID
        Task taskNullID = new Task(null, this.TASK_1_NAME, this.TASK_1_DESCRIPTION, this.TASK_1_START, this.TASK_1_END);

        // the next assigned ID will be 6
        this.taskCollection.save(taskNullID);
        assertEquals(this.TASK_1_NAME, this.taskCollection.get(6).getTaskName());
        assertEquals(this.TASK_1_DESCRIPTION, this.taskCollection.get(6).getDescription());
        assertEquals(this.TASK_1_START, this.taskCollection.get(6).getStartTime());
        assertEquals(this.TASK_1_END, this.taskCollection.get(6).getEndTime());
    }

    @Test public void Write_to_disk_method_in_TaskCollection_works_correctly() throws IOException {
        File file = new File("tmp/testWrite/writeToDisk.csv");
        // check that file does not exist in the beginning
        assertFalse(file.isFile());

        DiskIO diskIO = new DiskIO(this.taskCollection, "tmp/testWrite/writeToDisk.csv");
        this.taskCollection.setDiskIO(diskIO);

        this.taskCollection.writeToDisk();
        // check that file gets created when writeToFile() is called
        assertTrue(file.isFile());

        // delete the file for future testing of writing file function
        file.delete();
    }

    // ----------------------------------------------------------------------------------------
    //
    // II. Get Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Get_returns_correct_Task() throws PrimaryKeyNotFoundException {
        Task returnedTask = this.taskCollection.get(1);
        assertEquals(this.task1_, returnedTask);
    }

    // ----------------------------------------------------------------------------------------
    //
    // III. GetAll Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Get_all_method_with_null_parameter_returns_list_correctly() {
        ArrayList<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(this.task1_);
        expectedTaskList.add(this.task2_);
        expectedTaskList.add(this.task3_);
        expectedTaskList.add(this.task4_);
        expectedTaskList.add(this.task5_);

        // assert that expected and actual ArrayLists are equal
        assertEquals(expectedTaskList, this.taskCollection.getAll());
    }

    // ----------------------------------------------------------------------------------------
    //
    // IV. Remove Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test(expected = PrimaryKeyNotFoundException.class) public void Remove_deletes_correct_Task()
            throws PrimaryKeyNotFoundException {
        this.taskCollection.remove(1);
        this.taskCollection.get(1);
    }

    @Test public void Remove_all_clears_all_data() {
        this.taskCollection.removeAll();
        assertEquals(0, this.taskCollection.getDataTree().size());
        assertEquals(0, this.taskCollection.getStartTimeTree().size());
        assertEquals(0, this.taskCollection.getEndTimeTree().size());
    }

    // ----------------------------------------------------------------------------------------
    //
    // V. Search by Time Range Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Searching_by_start_before_returns_list_correctly() {
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task1_);
        expectedTaskList.add(this.task2_);
        expectedTaskList.add(this.task3_);

        // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.searchstartBefore(this.TASK_3_START));
    }

    @Test public void Searching_by_start_after_returns_list_correctly() {
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task3_);
        expectedTaskList.add(this.task4_);
        expectedTaskList.add(this.task5_);

        // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.searchStartAfter(this.TASK_3_START));
    }

    @Test public void Searching_by_end_before_returns_list_correctly() {
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task1_);
        expectedTaskList.add(this.task2_);
        expectedTaskList.add(this.task3_);

        // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.searchEndBefore(this.TASK_3_END));
    }

    @Test public void Searching_by_end_after_returns_list_correctly() {
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task3_);
        expectedTaskList.add(this.task4_);
        expectedTaskList.add(this.task5_);

        // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.searchEndAfter(this.TASK_3_END));
    }

    @Test public void Searching_by_date_time_range_returns_list_correctly() {
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task2_);
        expectedTaskList.add(this.task3_);
        expectedTaskList.add(this.task4_);

        // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.searchByDateTimeRange(this.TASK_3_START, this.TASK_3_END));
    }

    // ----------------------------------------------------------------------------------------
    //
    // VI. Updating Start and End Time Trees Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Task_entry_in_tree_gets_shifted_when_start_time_changes() throws PrimaryKeyNotFoundException {
        // check that the list initially contains the old task
        assertTrue(this.taskCollection.getStartTimeTree().get(this.TASK_1_START).contains(this.task1_));

        Task newTask = new Task(1, this.TASK_1_NAME, this.TASK_1_DESCRIPTION, this.TASK_2_START, this.TASK_1_END);
        // edit the start time of task1
        this.taskCollection.save(newTask);

        // check that the list corresponding to the old start time no longer
        // contains the task
        assertFalse(this.taskCollection.getStartTimeTree().get(this.TASK_1_START).contains(this.task1_));

        // check that the list corresponding to the new start time now contains
        // the updated task
        assertTrue(this.taskCollection.getStartTimeTree().get(this.TASK_2_START).contains(newTask));
    }

    @Test public void Task_entry_in_tree_gets_shifted_when_end_time_changes() throws PrimaryKeyNotFoundException {
        // check that the list initially contains the old task
        assertTrue(this.taskCollection.getEndTimeTree().get(this.TASK_2_END).contains(this.task2_));

        Task newTask = new Task(2, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_3_END);
        // edit the end time of task2
        this.taskCollection.save(newTask);

        // check that the list corresponding to the old end time no longer
        // contains the task
        assertFalse(this.taskCollection.getEndTimeTree().get(this.TASK_2_END).contains(this.task2_));

        // check that the list corresponding to the new end time now contains
        // the updated task
        assertTrue(this.taskCollection.getEndTimeTree().get(this.TASK_3_END).contains(newTask));
    }

}