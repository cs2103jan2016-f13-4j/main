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
    
    private final int TASK_1_ID = 1;
    private final String TASK_1_NAME = "homework";
    private final String TASK_1_DESCRIPTION = "cs2103t";
    private final LocalDateTime TASK_1_START = LocalDateTime.of(2016, 3, 4, 14, 30);
    private final LocalDateTime TASK_1_END = LocalDateTime.of(2016, 3, 5, 14, 30);
    private Task task1_;
    
    private final int TASK_2_ID = 2;
    private final String TASK_2_NAME = "assignment";
    private final String TASK_2_DESCRIPTION = "cs3230";
    private final LocalDateTime TASK_2_START = LocalDateTime.of(2016, 3, 5, 14, 30);
    private final LocalDateTime TASK_2_END = LocalDateTime.of(2016, 3, 6, 14, 30);
    private Task task2_;
    
    private final int TASK_3_ID = 3;
    private final String TASK_3_NAME = "tutorial";
    private final String TASK_3_DESCRIPTION = "nm2101";
    private final LocalDateTime TASK_3_START = LocalDateTime.of(2016, 3, 6, 14, 30);
    private final LocalDateTime TASK_3_END = LocalDateTime.of(2016, 3, 7, 14, 30);
    private Task task3_;
    
    private final int TASK_4_ID = 4;
    private final String TASK_4_NAME = "tutorial";
    private final String TASK_4_DESCRIPTION = "nm2101";
    private final LocalDateTime TASK_4_START = LocalDateTime.of(2016, 3, 7, 14, 30);
    private final LocalDateTime TASK_4_END = LocalDateTime.of(2016, 3, 8, 14, 30);
    private Task task4_;
    
    private final int TASK_5_ID = 5;
    private final String TASK_5_NAME = "tutorial";
    private final String TASK_5_DESCRIPTION = "nm2101";
    private final LocalDateTime TASK_5_START = LocalDateTime.of(2016, 3, 8, 14, 30);
    private final LocalDateTime TASK_5_END = LocalDateTime.of(2016, 3, 9, 14, 30);
    private Task task5_;
    
    @Before
    public void setUp() {
        this.taskCollection = new TaskCollection();
        this.task1_ = new Task (this.TASK_1_ID, this.TASK_1_NAME, this.TASK_1_DESCRIPTION, this.TASK_1_START, this.TASK_1_END);
        this.task2_ = new Task (this.TASK_2_ID, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this.task3_ = new Task (this.TASK_3_ID, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);
        this.task4_ = new Task (this.TASK_4_ID, this.TASK_4_NAME, this.TASK_4_DESCRIPTION, this.TASK_4_START, this.TASK_4_END);
        this.task5_ = new Task (this.TASK_5_ID, this.TASK_5_NAME, this.TASK_5_DESCRIPTION, this.TASK_5_START, this.TASK_5_END);
        
        this.taskCollection.save(this.task1_);
        this.taskCollection.save(this.task2_);
        this.taskCollection.save(this.task3_);
        this.taskCollection.save(this.task4_);
        this.taskCollection.save(this.task5_);
    }
    
    @Test
    public void Save_returns_correct_Task_ID() {
        int returnedID = this.taskCollection.save(this.task1_);
        assertEquals(this.TASK_1_ID, returnedID);
    }
    
    @Test
    public void Get_returns_correct_Task() throws PrimaryKeyNotFoundException {
        Task returnedTask = this.taskCollection.get(this.TASK_1_ID);
        assertEquals(this.task1_, returnedTask);
    }
    
    @Test
    public void Save_sets_ID_when_ID_is_null() throws PrimaryKeyNotFoundException {
        // create Task with null value as ID
        Task taskNullID = new Task (null, this.TASK_1_NAME, this.TASK_1_DESCRIPTION, this.TASK_1_START, this.TASK_1_END);
        
        // the next assigned ID will be 6
        this.taskCollection.save(taskNullID);
        assertEquals(this.TASK_1_NAME, this.taskCollection.get(6).getTaskName());
        assertEquals(this.TASK_1_DESCRIPTION, this.taskCollection.get(6).getDescription());
        assertEquals(this.TASK_1_START, this.taskCollection.get(6).getStartTime());
        assertEquals(this.TASK_1_END, this.taskCollection.get(6).getEndTime());
    }

    @Test(expected = PrimaryKeyNotFoundException.class)
    public void Remove_deletes_correct_Task() throws PrimaryKeyNotFoundException {
        this.taskCollection.remove(this.TASK_1_ID);
        this.taskCollection.get(this.TASK_1_ID);
    }
    
    @Test
    public void Get_all_method_with_null_parameter_returns_list_correctly() {
        ArrayList<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(this.task1_);
        expectedTaskList.add(this.task2_);
        expectedTaskList.add(this.task3_);
        expectedTaskList.add(this.task4_);
        expectedTaskList.add(this.task5_);
        
        // assert that expected and actual ArrayLists are equal
        assertEquals(expectedTaskList, this.taskCollection.getAll(null));
    }
    
    @Test
    public void Get_all_method_with_task_descriptor_checking_TaskName_returns_list_correctly() {
        TaskDescriptor taskDescriptor = new TaskDescriptor() {
          @Override
          public boolean matches(Task task) {
              return task.getTaskName().contains("tut");
          }
        };
        
        // create an ArrayList with Task entries containing 'assign' in task name
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task3_);
        expectedTaskList.add(this.task4_);
        expectedTaskList.add(this.task5_);
        
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
        
        // create an ArrayList with Task entries containing 'cs' in task description
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task1_);
        expectedTaskList.add(this.task2_);
        
        // assert that expected results and actual filtered results are the same
        assertEquals(expectedTaskList, this.taskCollection.getAll(taskDescriptor));
    }
    
    @Test
    public void Searching_by_start_before_returns_list_correctly() {           
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task1_);
        expectedTaskList.add(this.task2_);
        expectedTaskList.add(this.task3_);
        
     // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.startBefore(this.TASK_3_START));
    }
    
    @Test
    public void Searching_by_start_after_returns_list_correctly() {        
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task3_);
        expectedTaskList.add(this.task4_);
        expectedTaskList.add(this.task5_);
        
        // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.startAfter(this.TASK_3_START));
    }
    
    @Test
    public void Searching_by_end_before_returns_list_correctly() {   
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task1_);
        expectedTaskList.add(this.task2_);
        expectedTaskList.add(this.task3_);
        
        // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.endBefore(this.TASK_3_END));
    }
    
    @Test
    public void Searching_by_end_after_returns_list_correctly() {    
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task3_);
        expectedTaskList.add(this.task4_);
        expectedTaskList.add(this.task5_);
        
        // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.endAfter(this.TASK_3_END));
    }
    
    @Test
    public void Searching_by_date_time_range_returns_list_correctly() {  
        ArrayList<Task> expectedTaskList = new ArrayList<Task>();
        expectedTaskList.add(this.task2_);
        expectedTaskList.add(this.task3_);
        expectedTaskList.add(this.task4_);
        
        // assert that expected results and actual search results are the same
        assertEquals(expectedTaskList, this.taskCollection.searchByDateTimeRange(this.TASK_3_START, this.TASK_3_END));
    }
}
