package component.back_end.storage;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import exception.back_end.PrimaryKeyNotFoundException;

public class TaskCollectionWriterTest {
    
    private TaskCollection taskCollection_;
    private TaskCollectionWriter taskCollectionWriter_;
    private final String FILE_NAME = "test.csv";
    
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
    public void setUp() throws IOException {
        this.taskCollection_ = new TaskCollection();
        this.task1_ = new Task (null, this.TASK_1_NAME, this.TASK_1_DESCRIPTION, this.TASK_1_START, this.TASK_1_END);
        this.task2_ = new Task (null, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this.task3_ = new Task (null, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);
        this.task4_ = new Task (null, this.TASK_4_NAME, this.TASK_4_DESCRIPTION, this.TASK_4_START, this.TASK_4_END);
        this.task5_ = new Task (null, this.TASK_5_NAME, this.TASK_5_DESCRIPTION, this.TASK_5_START, this.TASK_5_END);
        
        this.taskCollection_.save(this.task1_);
        this.taskCollection_.save(this.task2_);
        this.taskCollection_.save(this.task3_);
        this.taskCollection_.save(this.task4_);
        this.taskCollection_.save(this.task5_);
        
        this.taskCollectionWriter_ = new TaskCollectionWriter(this.taskCollection_, this.FILE_NAME);
    }
    
    @Test
    public void Save_function_writes_Tasks_into_file_correctly() throws IOException, PrimaryKeyNotFoundException {
        this.taskCollectionWriter_.save();
        BufferedReader reader = new BufferedReader(new FileReader(this.FILE_NAME));
        String currLine;
        int index = 1;
        while ((currLine = reader.readLine()) != null) {
            assertEquals(this.taskCollection_.get(index).encodeTaskToString(), currLine);
            index++;
        }
        reader.close();
    }

}
