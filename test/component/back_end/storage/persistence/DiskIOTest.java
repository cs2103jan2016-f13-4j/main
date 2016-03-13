package component.back_end.storage.persistence;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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
public class DiskIOTest {
    
    private DiskIO _diskIO;
    private TaskCollection _taskCollection;
    
    private final String TASK_1_NAME = "homework";
    private final String TASK_1_DESCRIPTION = "cs2103t";
    private final LocalDateTime TASK_1_START = LocalDateTime.of(2016, 3, 4, 14, 30);
    private final LocalDateTime TASK_1_END = LocalDateTime.of(2016, 3, 5, 14, 30);
    private Task _task1;
    
    private final String TASK_2_NAME = "assignment";
    private final String TASK_2_DESCRIPTION = "cs3230";
    private final LocalDateTime TASK_2_START = LocalDateTime.of(2016, 3, 5, 14, 30);
    private final LocalDateTime TASK_2_END = LocalDateTime.of(2016, 3, 6, 14, 30);
    private Task _task2;
    
    private final String TASK_3_NAME = "tutorial";
    private final String TASK_3_DESCRIPTION = "nm2101";
    private final LocalDateTime TASK_3_START = LocalDateTime.of(2016, 3, 6, 14, 30);
    private final LocalDateTime TASK_3_END = LocalDateTime.of(2016, 3, 7, 14, 30);
    private Task _task3;
    
    private final String TASK_4_NAME = "tutorial";
    private final String TASK_4_DESCRIPTION = "nm2101";
    private final LocalDateTime TASK_4_START = LocalDateTime.of(2016, 3, 7, 14, 30);
    private final LocalDateTime TASK_4_END = LocalDateTime.of(2016, 3, 8, 14, 30);
    private Task _task4;
    
    private final String TASK_5_NAME = "tutorial";
    private final String TASK_5_DESCRIPTION = "nm2101";
    private final LocalDateTime TASK_5_START = LocalDateTime.of(2016, 3, 8, 14, 30);
    private final LocalDateTime TASK_5_END = LocalDateTime.of(2016, 3, 9, 14, 30);
    private Task _task5;
    
    @Before
    public void setUp() throws IOException {
        // Ensure test read directory exists
        (new File("data/testRead")).mkdirs();
        (new File("data/testNoFile")).mkdirs();

        this._taskCollection = new TaskCollection();
        
        this._task1 = new Task (1, this.TASK_1_NAME, this.TASK_1_DESCRIPTION, this.TASK_1_START, this.TASK_1_END);
        this._task2 = new Task (2, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this._task3 = new Task (3, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);
        this._task4 = new Task (4, this.TASK_4_NAME, this.TASK_4_DESCRIPTION, this.TASK_4_START, this.TASK_4_END);
        this._task5 = new Task (5, this.TASK_5_NAME, this.TASK_5_DESCRIPTION, this.TASK_5_START, this.TASK_5_END);
    }
    
    @Test
    public void Read_function_extracts_tasks_data_from_file() throws IOException {
        // saved a .csv file with data of tasks 1 to 5
        // check if DiskIO read method works correctly
        this._diskIO = new DiskIO(this._taskCollection, "data/testRead/ToDoData.csv");
        this._diskIO.read();
        List<Task> taskList = this._taskCollection.getAll(null);
        
        assertEquals(this._task1, taskList.get(0));
        assertEquals(this._task2, taskList.get(1));
        assertEquals(this._task3, taskList.get(2));
        assertEquals(this._task4, taskList.get(3));
        assertEquals(this._task5, taskList.get(4));
    }
    
    @Test
    public void Read_function_creates_new_data_file_if_none_exists() throws IOException {
        // navigate to a directory where the data file does not exist
        // check that file does not exist at the start
        String pathName = "data/testNoFile/ToDoData.csv";
        File file = new File(pathName);
        assertFalse(file.isFile());
        
        this._diskIO = new DiskIO(this._taskCollection, pathName);
        this._diskIO.read();
        
        // check that the file gets created after the read method is called
        assertTrue(new File(pathName).isFile());
        
        // delete the file for future testing of creating file function
        file.delete();
    }
    
    @Test
    public void Write_function_writes_data_into_file_correctly() throws IOException, PrimaryKeyNotFoundException {
        // set all id to null to indicate that these are new tasks
        this._task1.setId(null);
        this._task2.setId(null);
        this._task3.setId(null);
        this._task4.setId(null);
        this._task5.setId(null);
        
        this._taskCollection.save(this._task1);
        this._taskCollection.save(this._task2);
        this._taskCollection.save(this._task3);
        this._taskCollection.save(this._task4);
        this._taskCollection.save(this._task5);
        
        String pathName = "data/testWrite/ToDoData.csv";
        File file = new File(pathName);
        this._diskIO = new DiskIO(this._taskCollection, pathName);
        this._diskIO.write();
        
        BufferedReader reader = new BufferedReader(new FileReader(pathName));
        String currLine;
        int index = 1;
        while ((currLine = reader.readLine()) != null) {
            assertEquals(this._taskCollection.get(index).encodeTaskToString(), currLine);
            index++;
        }
        reader.close();
        
        // delete the file for future testing of writing file function
        file.delete();
    }

}