package storage;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import exception.ExceptionHandler;

/**
 *
 * @@author Chng Hui Yie
 *
 */
public class DiskIOTest {

    private DiskIO _diskIO;
    private Storage _storage;

    @Before public void setUp() {
        this._storage = Storage.getInstance();
        this._diskIO = DiskIO.getInstance();

        // Clear remnants of previous test(s)
        this._storage.removeAll();
        this._storage.writeToDisk();
        File file = new File("tmp/ToDoData.csv");
        file.delete();
        this._diskIO.setFileName("tmp/ToDoData.csv");
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
        File file = new File("tmp/ToDoData.csv");
        file.delete();
        assertFalse(file.exists());
        assertEquals("tmp/ToDoData.csv", this._diskIO.getFileName());
        this._diskIO.read();
        // Check that file gets created after the read method is called
        assertTrue(file.exists());
    }

    @Test public void Read_function_extracts_tasks_data_from_file() {
        String taskString1 = "1,marketing pitch,client FGH,2016-03-01T08:00,2016-03-04T10:00,2016-03-04T12:00,true,2";
        String taskString2 = "2,sales meeting,client IJK,2016-03-02T09:00,2016-03-05T11:30,2016-03-05T13:30,false,2";
        String taskString3 = "3,sales meeting,internal,2016-03-04T07:00,2016-03-06T09:30,2016-03-06T11:30,false,1";

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
        assertEquals("tmp/ToDoData.csv", this._diskIO.getFileName());
        ArrayList<String> actualTaskList = this._diskIO.read();
        // Check that actual list matches expected list
        assertEquals(taskStrings, actualTaskList);
    }

    // ----------------------------------------------------------------------------------------
    //
    // II. Write Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Write_function_writes_data_into_file_correctly() throws IOException {

        String taskString1 = "1,marketing pitch,client XYZ,2016-03-01T08:00,2016-03-09T14:30,2016-03-09T16:30,false,3";
        String taskString2 = "2,sales meeting,client ABC,2016-03-04T07:00,2016-03-11T12:00,2016-03-11T14:30,true,2";
        ArrayList<String> taskStrings = new ArrayList<String>();
        taskStrings.add(taskString1);
        taskStrings.add(taskString2);

        this._diskIO.write(taskStrings);

        BufferedReader reader = new BufferedReader(new FileReader("tmp/ToDoData.csv"));
        assertEquals("1,marketing pitch,client XYZ,2016-03-01T08:00,2016-03-09T14:30,2016-03-09T16:30,false,3",
                reader.readLine());
        assertEquals("2,sales meeting,client ABC,2016-03-04T07:00,2016-03-11T12:00,2016-03-11T14:30,true,2",
                reader.readLine());
        reader.close();
    }

    // ----------------------------------------------------------------------------------------
    //
    // III. User Preferences Tests
    //
    // ----------------------------------------------------------------------------------------
    @Test public void Loading_user_preferences_from_file_correctly_assigns_custom_file_name() {
        // Write a json file to disk
        File file = new File("data/user/UserPreferences.json");
        file.delete();
        assertFalse(file.exists());
        File folder = new File("data/user/UserPreferences.json").getParentFile();
        folder.mkdirs();
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(("data/user/UserPreferences.json")));
            bufferedWriter.write("{\"todoDataPath\":\"/Users/Mary/Dropbox/ToDoData.csv\"}");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(file.exists());
        this._diskIO.processUserPreferencesFile();
        assertEquals("/Users/Mary/Dropbox/ToDoData.csv", this._diskIO.getFileName());
    }
}