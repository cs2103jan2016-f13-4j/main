package storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import exception.ExceptionHandler;
import exception.PrimaryKeyNotFoundException;

/**
 *
 * @author Huiyie
 *
 */
public class DiskIOTest {

    private DiskIO _diskIO;
    private Storage _taskCollection;

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

    @Before public void setUp() throws IOException {
        // Ensure test read directory exists
        (new File("tmp/testWrite")).mkdirs();
        (new File("tmp/testRead")).mkdirs();
        (new File("tmp/testNoFile")).mkdirs();

        this._taskCollection = Storage.getInstance();
        // Clear remnants of previous test(s)
        this._taskCollection.removeAll();
        this._taskCollection.writeToDisk();
        File file = new File("tmp/ToDoData.csv");
        file.delete();

        this._task1 = new Task(1, this.TASK_1_NAME, this.TASK_1_DESCRIPTION, this.TASK_1_START, this.TASK_1_END);
        this._task2 = new Task(2, this.TASK_2_NAME, this.TASK_2_DESCRIPTION, this.TASK_2_START, this.TASK_2_END);
        this._task3 = new Task(3, this.TASK_3_NAME, this.TASK_3_DESCRIPTION, this.TASK_3_START, this.TASK_3_END);
        this._task4 = new Task(4, this.TASK_4_NAME, this.TASK_4_DESCRIPTION, this.TASK_4_START, this.TASK_4_END);
        this._task5 = new Task(5, this.TASK_5_NAME, this.TASK_5_DESCRIPTION, this.TASK_5_START, this.TASK_5_END);
    }

    @Test public void File_does_not_exist_upon_setting_up() throws IOException {
        File file = new File("tmp/ToDoData.csv");
        assertFalse(file.exists());
    }

    // ----------------------------------------------------------------------------------------
    //
    // I. Read Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Read_function_creates_new_data_file_if_none_exists() {
        this._diskIO = DiskIO.getInstance();
        File file = new File("tmp/ToDoData.csv");
        file.delete();
        assertFalse(file.exists());

        try {
            this._diskIO.read();
            // Check that file gets created after the read method is called
            assertTrue(file.exists());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            ExceptionHandler.handle(e);
        }
    }

    @Test public void Read_function_extracts_tasks_data_from_file() {
        String taskString1 = "\"1\", \"marketing pitch\", \"client FGH\", \"2016-03-04T10:00\", \"2016-03-04T12:00\"";
        String taskString2 = "\"2\", \"sales meeting\", \"client IJK\", \"2016-03-05T11:30\", \"2016-03-05T13:30\"";
        String taskString3 = "\"3\", \"sales meeting\", \"internal\", \"2016-03-06T09:30\", \"2016-03-06T11:30\"";

        ArrayList<String> taskStrings = new ArrayList<String>();
        taskStrings.add(taskString1);
        taskStrings.add(taskString2);
        taskStrings.add(taskString3);

        // stub
        // write the data to file as a string
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("tmp/ToDoData.csv"));
            for (String taskString : taskStrings) {
                writer.write(taskString);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            ExceptionHandler.handle(e);
        }

        this._diskIO = DiskIO.getInstance();
        try {
            ArrayList<String> actualTaskList = this._diskIO.read();
            // Check that actual list matches expected list
            assertEquals(taskStrings, actualTaskList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            ExceptionHandler.handle(e);
        }
    }

    // ----------------------------------------------------------------------------------------
    //
    // II. Write Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Write_function_works() throws IOException, PrimaryKeyNotFoundException {
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
        this._taskCollection.writeToDisk();

        BufferedReader reader = new BufferedReader(new FileReader("tmp/ToDoData.csv"));

        String currLine;
        int index = 1;
        while ((currLine = reader.readLine()) != null) {
            assertEquals(this._taskCollection.get(index).encodeTaskToString(), currLine);
            index++;
        }
        reader.close();
    }

    @Test public void Write_function_writes_data_into_file_correctly() throws IOException {
        this._diskIO = DiskIO.getInstance();

        String taskString1 = "\"1\", \"marketing pitch\", \"client XYZ\", \"2016-03-09T14:30\", \"2016-03-09T16:30\"";
        String taskString2 = "\"2\", \"sales meeting\", \"client ABC\", \"2016-03-11T12:00\", \"2016-03-11T14:30\"";
        ArrayList<String> taskStrings = new ArrayList<String>();
        taskStrings.add(taskString1);
        taskStrings.add(taskString2);

        this._diskIO.write(taskStrings);

        BufferedReader reader = new BufferedReader(new FileReader("tmp/ToDoData.csv"));
        assertEquals("\"1\", \"marketing pitch\", \"client XYZ\", \"2016-03-09T14:30\", \"2016-03-09T16:30\"",
                reader.readLine());
        assertEquals("\"2\", \"sales meeting\", \"client ABC\", \"2016-03-11T12:00\", \"2016-03-11T14:30\"",
                reader.readLine());
        reader.close();
    }

}