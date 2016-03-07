package component.back_end.storage;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Huiyie
 *
 */

public class TaskCollectionTest {
    
    private TaskCollection taskData_;
    private Task task_;
    
    @Before
    public void setUp() {
        this.taskData_ = new TaskCollection();
        
        // initialize attributes
        String taskName = "homework";
        String description = "cs2103t";
        LocalDateTime taskStart = LocalDateTime.of(2016, 3, 6, 14, 30);
        LocalDateTime taskEnd = LocalDateTime.of(2016, 3, 7, 14, 30);
        
        // create tuple
        this.task_ = new Task(null, taskName, description, taskStart, taskEnd);
    }

    @Test
    public void Primary_key_of_TreeMap_leads_to_correct_tuple() {
        // execute add
        int taskId = this.taskData_.save(this.task_);
        
        // check that the tuples are the same
        assertEquals(this.taskData_.get(taskId), this.task_);
    }
    
    @Test
    public void Different_primary_keys_point_to_different_tuples_in_TreeMap() {
        
        // initialize attributes
        String taskName = "floorball training";
        String description = "in MPSH";
        LocalDateTime taskStart = LocalDateTime.of(2016, 3, 6, 14, 30);
        LocalDateTime taskEnd = LocalDateTime.of(2016, 3, 7, 14, 30);
        
        // create tuple
        Task task2 = new Task(null, taskName, description, taskStart, taskEnd);
        
        // execute adds
        int index1 = this.taskData_.save(this.task_);
        int index2 = this.taskData_.save(task2);
        
        // use primary keys of task_ and tuple2 to retrieve them from HashMap
        assertThat(this.taskData_.get(index1), is(not(equalTo(this.taskData_.get(index2)))));
    }
    
    @Test
    public void Edit_function_overwrites_data() {
        
    }
}
