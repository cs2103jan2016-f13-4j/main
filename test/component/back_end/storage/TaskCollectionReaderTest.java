package component.back_end.storage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import component.back_end.storage.persistence.TaskCollectionReader;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Huiyie
 *
 */
public class TaskCollectionReaderTest {
    
    private TaskCollectionReader taskCollectionReader_;
    private TaskCollection taskCollection_;
    
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
    
    @Before
    public void setUp() {
        this.taskCollectionReader_ = new TaskCollectionReader("tmp/test1.csv");
        
        this.task1_ = new Task (1, this.TASK_1_NAME, this.TASK_1_DESCRIPTION, this.TASK_1_START, this.TASK_1_END);
        this.task2_ = new Task (2, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this.task3_ = new Task (3, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);
        this.task4_ = new Task (4, this.TASK_4_NAME, this.TASK_4_DESCRIPTION, this.TASK_4_START, this.TASK_4_END);
        this.task5_ = new Task (5, this.TASK_5_NAME, this.TASK_5_DESCRIPTION, this.TASK_5_START, this.TASK_5_END);

    }

    @Test
    public void Write_function_extracts_Tasks_data_from_file() throws IOException {
        this.taskCollectionReader_.read();
        this.taskCollection_ = this.taskCollectionReader_.getTaskCollection();
        List<Task> taskList = this.taskCollection_.getAll(null);
        
        assertEquals(this.task1_, taskList.get(0));
        assertEquals(this.task2_, taskList.get(1));
        assertEquals(this.task3_, taskList.get(2));
        assertEquals(this.task4_, taskList.get(3));
        assertEquals(this.task5_, taskList.get(4));
    }
}