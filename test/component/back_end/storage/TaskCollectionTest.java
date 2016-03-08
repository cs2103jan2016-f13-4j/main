package component.back_end.storage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import component.back_end.storage.Task;
import component.back_end.storage.TaskCollection;
import exception.back_end.PrimaryKeyNotFoundException;

/**
 * 
 * @author Huiyie
 *
 */

public class TaskCollectionTest {
    
    private TaskCollection taskCollection;
    
    private final int TASK_ID = 1;
    private final String TASK_NAME = "homework";
    private final String TASK_DESCRIPTION = "cs2103t";
    private final LocalDateTime TASK_START = LocalDateTime.of(2016, 3, 4, 14, 30);
    private final LocalDateTime TASK_END = LocalDateTime.of(2016, 3, 5, 14, 30);
    private Task task;
    
    private final int TASK_2_ID = 2;
    private final String TASK_2_NAME = "assignment";
    private final String TASK_2_DESCRIPTION = "cs3230";
    private final LocalDateTime TASK_2_START = LocalDateTime.of(2016, 3, 5, 14, 30);
    private final LocalDateTime TASK_2_END = LocalDateTime.of(2016, 3, 6, 14, 30);
    private Task task2;
    
    private final int TASK_3_ID = 3;
    private final String TASK_3_NAME = "tutorial";
    private final String TASK_3_DESCRIPTION = "nm2101";
    private final LocalDateTime TASK_3_START = LocalDateTime.of(2016, 3, 6, 14, 30);
    private final LocalDateTime TASK_3_END = LocalDateTime.of(2016, 3, 7, 14, 30);
    private Task task3;
    
    private final int TASK_4_ID = 4;
    private final String TASK_4_NAME = "tutorial";
    private final String TASK_4_DESCRIPTION = "nm2101";
    private final LocalDateTime TASK_4_START = LocalDateTime.of(2016, 3, 7, 14, 30);
    private final LocalDateTime TASK_4_END = LocalDateTime.of(2016, 3, 8, 14, 30);
    private Task task4;
    
    private final int TASK_5_ID = 5;
    private final String TASK_5_NAME = "tutorial";
    private final String TASK_5_DESCRIPTION = "nm2101";
    private final LocalDateTime TASK_5_START = LocalDateTime.of(2016, 3, 8, 14, 30);
    private final LocalDateTime TASK_5_END = LocalDateTime.of(2016, 3, 9, 14, 30);
    private Task task5;
    
    @Before
    public void setUp() {
        this.taskCollection = new TaskCollection();
        this.task = new Task (this.TASK_ID, this.TASK_NAME, this.TASK_DESCRIPTION, this.TASK_START, this.TASK_END);
        
    }
    
    @Test
    public void Save_returns_correct_Task_ID() {
        int returnedID = this.taskCollection.save(this.task);
        assertEquals(this.TASK_ID, returnedID);
    }
    
    @Test
    public void Get_returns_correct_Task() throws PrimaryKeyNotFoundException {
        this.taskCollection.save(this.task);
        Task returnedTask = this.taskCollection.get(this.TASK_ID);
        assertEquals(this.task, returnedTask);
    }
    
    @Test
    public void Save_sets_ID_when_ID_is_null() throws PrimaryKeyNotFoundException {
        // create Task with null value as ID
        Task taskNullID = new Task (null, this.TASK_NAME, this.TASK_DESCRIPTION, this.TASK_START, this.TASK_END);
        
        this.taskCollection.save(taskNullID);
        assertEquals(this.TASK_NAME, this.taskCollection.get(1).getTaskName());
        assertEquals(this.TASK_DESCRIPTION, this.taskCollection.get(1).getDescription());
        assertEquals(this.TASK_START, this.taskCollection.get(1).getStartTime());
        assertEquals(this.TASK_END, this.taskCollection.get(1).getEndTime());
    }

    @Test(expected = PrimaryKeyNotFoundException.class)
    public void Remove_deletes_correct_Task() throws PrimaryKeyNotFoundException {
        this.taskCollection.save(this.task);
        this.taskCollection.remove(this.TASK_ID);
        this.taskCollection.get(this.TASK_ID);
    }
    
    @Test
    public void Get_all_method_with_null_parameter_returns_list_correctly() {
        
        // create two additional Tasks for adding into TreeMap
        this.task2 = new Task (this.TASK_2_ID, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this.task3 = new Task (this.TASK_3_ID, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);

        // create expected ArrayList consisting of three Tasks
        ArrayList<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(this.task);
        expectedTaskList.add(this.task2);
        expectedTaskList.add(this.task3);
        
        // add all three Tasks
        this.taskCollection.save(this.task);
        this.taskCollection.save(this.task2);
        this.taskCollection.save(this.task3);
        
        // assert that expected and actual ArrayLists are equal
        assertEquals(expectedTaskList, this.taskCollection.getAll(null));
    }
    
    @Test
    public void Get_all_method_with_task_descriptor_checking_TaskName_returns_list_correctly() {
        TaskDescriptor taskDescriptor = new TaskDescriptor() {
          @Override
          public boolean matches(Task task) {
              return task.getTaskName().contains("assign");
          }
        };
        
        // create two additional Tasks for adding into TreeMap
        this.task2 = new Task (this.TASK_2_ID, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this.task3 = new Task (this.TASK_3_ID, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);
        
        // add all three Tasks
        this.taskCollection.save(this.task);
        this.taskCollection.save(this.task2);
        this.taskCollection.save(this.task3);
        
        // create an ArrayList with Task entries containing 'assign' in task name
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task2);
        
        // assert that expected results and actual filtered results are the same
        assertEquals(expectedTaskList, this.taskCollection.getAll(taskDescriptor));
    }
    
    @Test
    public void Get_all_method_with_task_descriptor_checking_TaskDescription_returns_list_correctly() {
        TaskDescriptor taskDescriptor = new TaskDescriptor() {
          @Override
          public boolean matches(Task task) {
              return task.getDescription().contains("cs");
          }
        };
        
        // create two additional Tasks for adding into TreeMap
        this.task2 = new Task (this.TASK_2_ID, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this.task3 = new Task (this.TASK_3_ID, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);
        
        // add all three Tasks
        this.taskCollection.save(this.task);
        this.taskCollection.save(this.task2);
        this.taskCollection.save(this.task3);
        
        // create an ArrayList with Task entries containing 'cs' in task description
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task);
        expectedTaskList.add(this.task2);
        
        // assert that expected results and actual filtered results are the same
        assertEquals(expectedTaskList, this.taskCollection.getAll(taskDescriptor));
    }
    
    @Test
    public void Searching_by_start_before_returns_list_correctly() {
        // create four additional Tasks for adding into TreeMap
        this.task2 = new Task (this.TASK_2_ID, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this.task3 = new Task (this.TASK_3_ID, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);
        this.task4 = new Task (this.TASK_4_ID, this.TASK_4_NAME, this.TASK_4_DESCRIPTION, this.TASK_4_START, this.TASK_4_END);
        this.task5 = new Task (this.TASK_5_ID, this.TASK_5_NAME, this.TASK_5_DESCRIPTION, this.TASK_5_START, this.TASK_5_END);
        
        // add five three Tasks
        this.taskCollection.save(this.task);
        this.taskCollection.save(this.task2);
        this.taskCollection.save(this.task3);
        this.taskCollection.save(this.task4);
        this.taskCollection.save(this.task5);
        
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task);
        expectedTaskList.add(this.task2);
        expectedTaskList.add(this.task3);
        
     // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.startBefore(this.TASK_3_START));
    }
    
    @Test
    public void Searching_by_start_after_returns_list_correctly() {
        // create four additional Tasks for adding into TreeMap
        this.task2 = new Task (this.TASK_2_ID, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this.task3 = new Task (this.TASK_3_ID, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);
        this.task4 = new Task (this.TASK_4_ID, this.TASK_4_NAME, this.TASK_4_DESCRIPTION, this.TASK_4_START, this.TASK_4_END);
        this.task5 = new Task (this.TASK_5_ID, this.TASK_5_NAME, this.TASK_5_DESCRIPTION, this.TASK_5_START, this.TASK_5_END);
        
        // add all five Tasks
        this.taskCollection.save(this.task);
        this.taskCollection.save(this.task2);
        this.taskCollection.save(this.task3);
        this.taskCollection.save(this.task4);
        this.taskCollection.save(this.task5);
        
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task3);
        expectedTaskList.add(this.task4);
        expectedTaskList.add(this.task5);
        
        // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.startAfter(this.TASK_3_START));
    }
    
    @Test
    public void Searching_by_end_before_returns_list_correctly() {
        // create four additional Tasks for adding into TreeMap
        this.task2 = new Task (this.TASK_2_ID, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this.task3 = new Task (this.TASK_3_ID, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);
        this.task4 = new Task (this.TASK_4_ID, this.TASK_4_NAME, this.TASK_4_DESCRIPTION, this.TASK_4_START, this.TASK_4_END);
        this.task5 = new Task (this.TASK_5_ID, this.TASK_5_NAME, this.TASK_5_DESCRIPTION, this.TASK_5_START, this.TASK_5_END);
        
        // add all five Tasks
        this.taskCollection.save(this.task);
        this.taskCollection.save(this.task2);
        this.taskCollection.save(this.task3);
        this.taskCollection.save(this.task4);
        this.taskCollection.save(this.task5);
        
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task);
        expectedTaskList.add(this.task2);
        expectedTaskList.add(this.task3);
        
        // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.endBefore(this.TASK_3_END));
    }
    
    @Test
    public void Searching_by_end_after_returns_list_correctly() {
        // create four additional Tasks for adding into TreeMap
        this.task2 = new Task (this.TASK_2_ID, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this.task3 = new Task (this.TASK_3_ID, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);
        this.task4 = new Task (this.TASK_4_ID, this.TASK_4_NAME, this.TASK_4_DESCRIPTION, this.TASK_4_START, this.TASK_4_END);
        this.task5 = new Task (this.TASK_5_ID, this.TASK_5_NAME, this.TASK_5_DESCRIPTION, this.TASK_5_START, this.TASK_5_END);
        
        // add all five Tasks
        this.taskCollection.save(this.task);
        this.taskCollection.save(this.task2);
        this.taskCollection.save(this.task3);
        this.taskCollection.save(this.task4);
        this.taskCollection.save(this.task5);
        
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task3);
        expectedTaskList.add(this.task4);
        expectedTaskList.add(this.task5);
        
        // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.endAfter(this.TASK_3_END));
    }
    
    @Test
    public void Searching_by_date_time_range_returns_list_correctly() {
        // create four additional Tasks for adding into TreeMap
        this.task2 = new Task (this.TASK_2_ID, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this.task3 = new Task (this.TASK_3_ID, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);
        this.task4 = new Task (this.TASK_4_ID, this.TASK_4_NAME, this.TASK_4_DESCRIPTION, this.TASK_4_START, this.TASK_4_END);
        this.task5 = new Task (this.TASK_5_ID, this.TASK_5_NAME, this.TASK_5_DESCRIPTION, this.TASK_5_START, this.TASK_5_END);
        
        // add all five Tasks
        this.taskCollection.save(this.task);
        this.taskCollection.save(this.task2);
        this.taskCollection.save(this.task3);
        this.taskCollection.save(this.task4);
        this.taskCollection.save(this.task5);
        
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task2);
        expectedTaskList.add(this.task3);
        expectedTaskList.add(this.task4);
        
        // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.searchByDateTimeRange(this.TASK_3_START, this.TASK_3_END));
    }
}
