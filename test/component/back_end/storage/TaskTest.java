package component.back_end.storage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Huiyie
 *
 */

public class TaskTest {
    
    private final Integer TASK_ID = 1;
    private final String TASK_NAME = "homework";
    private final String TASK_DESCRIPTION = "cs2103t";
    private final LocalDateTime TASK_START = LocalDateTime.of(2016, 3, 6, 14, 30);
    private final LocalDateTime TASK_END = LocalDateTime.of(2016, 3, 8, 14, 30);
    private Task task_;
    
    @Before
    public void setUp() {
        this.task_ = new Task (this.TASK_ID, this.TASK_NAME, this.TASK_DESCRIPTION, this.TASK_START, this.TASK_END);
    }
    
    @Test
    public void GetId_method_retrieves_Task_ID_correctly() {
        assertEquals(this.TASK_ID, this.task_.getId());
    }
    
    @Test
    public void GetTaskName_method_retrieves_Task_Name_correctly() {
        assertEquals(this.TASK_NAME, this.task_.getTaskName());
    }
    
    @Test
    public void GetTaskDescription_method_retrieves_Task_Description_correctly() {
        assertEquals(this.TASK_DESCRIPTION, this.task_.getDescription());
    }
    
    @Test
    public void GetStartTime_method_retrieves_Task_Start_Time_correctly() {
        assertEquals(this.TASK_START, this.task_.getStartTime());
    }
    
    @Test
    public void GetEndTime_method_retrieves_Task_End_Time_correctly() {
        assertEquals(this.TASK_END, this.task_.getEndTime());
    }
    
    @Test
    public void SetId_method_successfully_assign_ID_to_Task() {
        // create Task with null ID
        this.task_ = new Task (null, this.TASK_NAME, this.TASK_DESCRIPTION, this.TASK_START, this.TASK_END);
        
        // assign an integer ID
        this.task_.setId(this.TASK_ID);
        
        assertNotNull(this.task_.getId()); // check that ID is no longer null
        assertEquals(this.TASK_ID, this.task_.getId()); // check that ID equals the new assigned value
    
    }

}
