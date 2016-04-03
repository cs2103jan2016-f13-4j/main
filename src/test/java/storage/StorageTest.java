package storage;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import shared.CustomTime;
import shared.Task;

import static org.junit.Assert.*;

/**
 * 
 * @@author Chng Hui Yie
 *
 */

public class StorageTest {

    private Storage _storage;

    @Before public void setUp() {
        this._storage = Storage.getInstance();

        // Clear all pre-existing data in TaskCollection
        this._storage.removeAll();
    }

    // ----------------------------------------------------------------------------------------
    //
    // I. Save Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Save_returns_correct_Task_ID() {
        int returnedID = this._storage.save(new Task(null, "marketing pitch", "client XYZ",
                new CustomTime(LocalDateTime.of(2016, 3, 9, 14, 30)), new CustomTime(LocalDateTime.of(2016, 3, 9, 16, 30))));
        assertEquals(1, returnedID);
    }

    @Test public void Save_sets_ID_when_ID_is_null() {
        // create Task with null value as ID
        Task taskNullID = new Task(null, "assignment", "cs3230", new CustomTime(LocalDateTime.of(2016, 3, 4, 14, 30)),
                new CustomTime(LocalDateTime.of(2016, 3, 5, 14, 30)));

        // the next assigned ID will be 1
        this._storage.save(taskNullID);
        assertSame(1, this._storage.get(1).getId());
        assertEquals("assignment", this._storage.get(1).getTaskName());
        assertEquals("cs3230", this._storage.get(1).getDescription());
        assertEquals(new CustomTime(LocalDateTime.of(2016, 3, 4, 14, 30)), this._storage.get(1).getStartTime());
        assertEquals(new CustomTime(LocalDateTime.of(2016, 3, 5, 14, 30)), this._storage.get(1).getEndTime());

    }

    @Test public void Write_to_disk_method_works_correctly() {
        File actualFile = new File("tmp/ToDoData.csv");
        this._storage.getDiskIO().setFileName("tmp/ToDoData.csv");

        Task task1 = new Task(null, "marketing pitch", "client XYZ", new CustomTime(LocalDateTime.of(2016, 3, 9, 14, 30)),
                new CustomTime(LocalDateTime.of(2016, 3, 9, 16, 30)));
        Task task2 = new Task(null, "sales meeting", "client ABC", new CustomTime(LocalDateTime.of(2016, 3, 11, 12, 00)),
                new CustomTime(LocalDateTime.of(2016, 3, 11, 14, 30)));
        this._storage.save(task1);
        this._storage.save(task2);
        this._storage.writeToDisk();

        // check that file gets created when writeToFile() is called
        assertTrue(actualFile.isFile());

        // delete the file for future testing of writing file function
        actualFile.delete();

        // check that file does not exist in the end after deletion
        assertFalse(actualFile.isFile());
    }

    // ----------------------------------------------------------------------------------------
    //
    // II. Get Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Get_returns_correct_Task() {
        Task task3 = new Task(null, "sales training", "HR", new CustomTime(LocalDateTime.of(2016, 3, 10, 14, 30)),
                new CustomTime(LocalDateTime.of(2016, 3, 10, 16, 30)));
        Task task4 = new Task(null, "meeting to discuss proposal", "sales team", new CustomTime(LocalDateTime.of(2016, 3, 11, 12, 00)),
                new CustomTime(LocalDateTime.of(2016, 3, 11, 14, 30)));
        this._storage.save(task3);
        this._storage.save(task4);
        task3.setId(1);
        task4.setId(2);

        assertEquals(task3, this._storage.get(1));
        assertEquals(task4, this._storage.get(2));

    }

    // ----------------------------------------------------------------------------------------
    //
    // III. GetAll Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Get_all_method_with_null_parameter_returns_list_correctly() {
        Task task5 = new Task(null, "tutorial", "nm2101", new CustomTime(LocalDateTime.of(2016, 3, 7, 14, 30)),
                new CustomTime(LocalDateTime.of(2016, 3, 8, 14, 30)));
        Task task6 = new Task(null, "essay submission", "nm3238", new CustomTime(LocalDateTime.of(2016, 3, 8, 12, 00)),
                new CustomTime(LocalDateTime.of(2016, 3, 9, 15, 30)));
        this._storage.save(task5);
        this._storage.save(task6);
        task5.setId(1);
        task6.setId(2);

        ArrayList<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(task5);
        expectedTaskList.add(task6);
        // assert that expected and actual ArrayLists are equal
        assertEquals(expectedTaskList, this._storage.getAll());
    }

    // ----------------------------------------------------------------------------------------
    //
    // IV. Remove Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Remove_deletes_correct_Task() {
        Task task7 = new Task(null, "minor project", "cs1020", new CustomTime(LocalDateTime.of(2016, 3, 7, 14, 30)),
                new CustomTime(LocalDateTime.of(2016, 3, 8, 14, 30)));
        Task task8 = new Task(null, "major project", "cs2102", new CustomTime(LocalDateTime.of(2016, 3, 8, 12, 00)),
                new CustomTime(LocalDateTime.of(2016, 3, 9, 15, 30)));
        this._storage.save(task7);
        this._storage.save(task8);

        // check that task7 was deleted
        Task deletedTask = this._storage.remove(1);
        assertEquals(task7, deletedTask);
        // check that isDeleted flag has been change to true
        assertEquals(true, deletedTask.isDeleted());

        // check that get() returns null
        assertNull(this._storage.get(1));
        assertNotNull(this._storage.get(2));

        // check that getAll() does not return the deleted Task, but keeps the
        // undeleted Task
        ArrayList<Task> taskList = new ArrayList<Task>(this._storage.getAll());
        assertFalse(taskList.contains(task7));
        assertTrue(taskList.contains(task8));
    }

    @Test public void Removing_invalid_index_returns_null_value() {
        Task task7 = new Task(null, "minor project", "cs1020", new CustomTime(LocalDateTime.of(2016, 3, 7, 14, 30)),
                new CustomTime(LocalDateTime.of(2016, 3, 8, 14, 30)));
        Task task8 = new Task(null, "major project", "cs2102", new CustomTime(LocalDateTime.of(2016, 3, 8, 12, 00)),
                new CustomTime(LocalDateTime.of(2016, 3, 9, 15, 30)));
        this._storage.save(task7);
        this._storage.save(task8);

        assertNull(this._storage.remove(3));
    }

    @Test public void Remove_all_clears_all_data() {
        Task task9 = new Task(null, "assignment 3", "cs3223", new CustomTime(LocalDateTime.of(2016, 3, 10, 14, 30)),
                new CustomTime(LocalDateTime.of(2016, 3, 12, 12, 00)));
        Task task10 = new Task(null, "homework", "ma1101r", new CustomTime(LocalDateTime.of(2016, 3, 8, 12, 00)),
                new CustomTime(LocalDateTime.of(2016, 3, 10, 15, 30)));
        this._storage.save(task9);
        this._storage.save(task10);

        this._storage.removeAll();
    }

    // ----------------------------------------------------------------------------------------
    //
    // V. Search by Time Range Tests
    //
    // ----------------------------------------------------------------------------------------

    public ArrayList<Task> Set_up_for_search_by_time_range_tests() {
        ArrayList<Task> taskList = new ArrayList<Task>();

        String TASK_1_NAME = "homework";
        String TASK_1_DESCRIPTION = "cs2103t";
        LocalDateTime TASK_1_START = LocalDateTime.of(2016, 3, 4, 10, 00);
        LocalDateTime TASK_1_END = LocalDateTime.of(2016, 3, 5, 12, 30);
        Task TASK_1 = new Task(null, TASK_1_NAME, TASK_1_DESCRIPTION, TASK_1_START, TASK_1_END);
        this._storage.save(TASK_1);
        TASK_1.setId(1);
        taskList.add(TASK_1);

        String TASK_2_NAME = "assignment";
        String TASK_2_DESCRIPTION = "cs3230";
        LocalDateTime TASK_2_START = LocalDateTime.of(2016, 3, 5, 13, 30);
        LocalDateTime TASK_2_END = LocalDateTime.of(2016, 3, 6, 13, 30);
        Task TASK_2 = new Task(null, TASK_2_NAME, TASK_2_DESCRIPTION, TASK_2_START, TASK_2_END);
        this._storage.save(TASK_2);
        TASK_2.setId(2);
        taskList.add(TASK_2);

        String TASK_3_NAME = "essay submission";
        String TASK_3_DESCRIPTION = "nm2101";
        LocalDateTime TASK_3_START = LocalDateTime.of(2016, 3, 6, 14, 30);
        LocalDateTime TASK_3_END = LocalDateTime.of(2016, 3, 7, 14, 30);
        Task TASK_3 = new Task(null, TASK_3_NAME, TASK_3_DESCRIPTION, TASK_3_START, TASK_3_END);
        this._storage.save(TASK_3);
        TASK_3.setId(3);
        taskList.add(TASK_3);

        String TASK_4_NAME = "tutorial";
        String TASK_4_DESCRIPTION = "nm2213";
        LocalDateTime TASK_4_START = LocalDateTime.of(2016, 3, 7, 15, 30);
        LocalDateTime TASK_4_END = LocalDateTime.of(2016, 3, 8, 16, 30);
        Task TASK_4 = new Task(null, TASK_4_NAME, TASK_4_DESCRIPTION, TASK_4_START, TASK_4_END);
        this._storage.save(TASK_4);
        TASK_4.setId(4);
        taskList.add(TASK_4);

        String TASK_5_NAME = "project report";
        String TASK_5_DESCRIPTION = "cs2102";
        LocalDateTime TASK_5_START = LocalDateTime.of(2016, 3, 8, 17, 30);
        LocalDateTime TASK_5_END = LocalDateTime.of(2016, 3, 9, 19, 30);
        Task TASK_5 = new Task(null, TASK_5_NAME, TASK_5_DESCRIPTION, TASK_5_START, TASK_5_END);
        this._storage.save(TASK_5);
        TASK_5.setId(5);
        taskList.add(TASK_5);

        return taskList;
    }

    @Test public void Searching_by_start_before_returns_list_correctly() {
        ArrayList<Task> fullTaskList = Set_up_for_search_by_time_range_tests();
        ArrayList<Task> expectedTaskList = new ArrayList<Task>(fullTaskList.subList(0, 3));

        // assert that expected results and actual search results are the same
//        assertEquals(expectedTaskList, this._storage.searchstartBefore(LocalDateTime.of(2016, 3, 6, 14, 30)));
    }

    @Test public void Searching_by_start_after_returns_list_correctly() {
        ArrayList<Task> fullTaskList = Set_up_for_search_by_time_range_tests();
        ArrayList<Task> expectedTaskList = new ArrayList<Task>(fullTaskList.subList(2, 5));

        // assert that expected results and actual search results are the same
//        assertEquals(expectedTaskList, this._storage.searchStartAfter(LocalDateTime.of(2016, 3, 6, 14, 30)));
    }

    @Test public void Searching_by_end_before_returns_list_correctly() {
        ArrayList<Task> fullTaskList = Set_up_for_search_by_time_range_tests();
        ArrayList<Task> expectedTaskList = new ArrayList<Task>(fullTaskList.subList(0, 3));

        // assert that expected results and actual search results are the same
//        assertEquals(expectedTaskList, this._storage.searchEndBefore(LocalDateTime.of(2016, 3, 7, 14, 30)));
    }

    @Test public void Searching_by_end_after_returns_list_correctly() {
        ArrayList<Task> fullTaskList = Set_up_for_search_by_time_range_tests();
        ArrayList<Task> expectedTaskList = new ArrayList<Task>(fullTaskList.subList(2, 5));

        // assert that expected results and actual search results are the same
//        assertEquals(expectedTaskList, this._storage.searchEndAfter(LocalDateTime.of(2016, 3, 7, 14, 30)));
    }

    @Test public void Searching_by_date_time_range_returns_list_correctly() {
        ArrayList<Task> fullTaskList = Set_up_for_search_by_time_range_tests();
        ArrayList<Task> expectedTaskList = new ArrayList<Task>(fullTaskList.subList(1, 4));

        // assert that expected results and actual search results are the same
//        assertEquals(expectedTaskList, this._storage.searchByDateTimeRange(LocalDateTime.of(2016, 3, 5, 13, 30),
//                LocalDateTime.of(2016, 3, 8, 16, 30)));
    }

    // ----------------------------------------------------------------------------------------
    //
    // VI. Updating Start and End Time Trees Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Task_entry_in_tree_gets_shifted_when_start_time_changes() {
        ArrayList<Task> fullTaskList = Set_up_for_search_by_time_range_tests();
        Task task1 = fullTaskList.get(0);

        // check that the list initially contains the old task
//        assertTrue(this._storage.getStartTimeTree().get(LocalDateTime.of(2016, 3, 4, 10, 00)).contains(task1));

        Task editedTask1 = new Task(1, "homework", "cs2103t", LocalDateTime.of(2016, 3, 5, 00, 00),
                LocalDateTime.of(2016, 3, 5, 12, 30));
        // edit the start time of task1
        this._storage.save(editedTask1);

        // check that the list corresponding to the old start time no longer
        // contains the task
//        assertFalse(this._storage.getStartTimeTree().get(LocalDateTime.of(2016, 3, 4, 10, 00)).contains(task1));

        // check that the list corresponding to the new start time now contains
        // the updated task
//        assertTrue(this._storage.getStartTimeTree().get(LocalDateTime.of(2016, 3, 5, 00, 00)).contains(editedTask1));
    }

    @Test public void Task_entry_in_tree_gets_shifted_when_end_time_changes() {
        ArrayList<Task> fullTaskList = Set_up_for_search_by_time_range_tests();
        Task task2 = fullTaskList.get(1);

        // check that the list initially contains the old task
//        assertTrue(this._storage.getEndTimeTree().get(LocalDateTime.of(2016, 3, 6, 13, 30)).contains(task2));

        Task editedTask2 = new Task(2, "assignment", "cs3230", LocalDateTime.of(2016, 3, 5, 13, 30),
                LocalDateTime.of(2016, 3, 6, 00, 30));
        // edit the end time of task2
        this._storage.save(editedTask2);

        // check that the list corresponding to the old end time no longer
        // contains the task
//        assertFalse(this._storage.getEndTimeTree().get(LocalDateTime.of(2016, 3, 6, 13, 30)).contains(task2));

        // check that the list corresponding to the new end time now contains
        // the updated task
//        assertTrue(this._storage.getEndTimeTree().get(LocalDateTime.of(2016, 3, 6, 00, 30)).contains(editedTask2));
    }

    // ----------------------------------------------------------------------------------------
    //
    // VII. Read From Disk Method Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Read_from_disk_method_works_correctly() {
        String taskString1 = "1,marketing pitch,client XYZ,2016-03-01T00:01,2016-03-09T14:30,2016-03-09T16:30,true,3";
        String taskString2 = "2,sales meeting,client ABC,2016-03-03T12:05,2016-03-11T12:00,2016-03-11T14:30,false,1";
        ArrayList<String> taskStrings = new ArrayList<String>();
        taskStrings.add(taskString1);
        taskStrings.add(taskString2);

        this._storage.removeAll();
        this._storage.readFromDisk(taskStrings);
        assertEquals((Integer) 1, this._storage.get(1).getId());
        assertEquals("marketing pitch", this._storage.get(1).getTaskName());
        assertEquals(LocalDateTime.parse("2016-03-01T00:01"), this._storage.get(1).getCreationTime());
        assertEquals(CustomTime.fromString("2016-03-09T14:30"), this._storage.get(1).getStartTime());
        assertEquals(CustomTime.fromString("2016-03-09T16:30"), this._storage.get(1).getEndTime());
        assertTrue(this._storage.get(1).isCompleted());
        assertEquals(Task.Priority.LOW, this._storage.get(1).getPriority());
        assertEquals((Integer) 2, this._storage.get(2).getId());
        assertEquals("sales meeting", this._storage.get(2).getTaskName());
        assertEquals(LocalDateTime.parse("2016-03-03T12:05"), this._storage.get(2).getCreationTime());
        assertEquals(CustomTime.fromString("2016-03-11T12:00"), this._storage.get(2).getStartTime());
        assertEquals(CustomTime.fromString("2016-03-11T14:30"), this._storage.get(2).getEndTime());
        assertFalse(this._storage.get(2).isCompleted());
        assertEquals(Task.Priority.HIGH, this._storage.get(2).getPriority());
    }

}