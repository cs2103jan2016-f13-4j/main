package storage;

import exception.ExceptionHandler;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 
 * @@author Chng Hui Yie
 *
 */
public class UserPreferencesTest {

    @Test
    public void Default_data_file_name_is_assigned() {
        UserPreferences up = UserPreferences.getInstance();
        up.resetUserPreferences(); // reset to default values
        assertEquals("data/ToDoData.csv", up.getTodoDataPath());
        up.resetUserPreferences();
    }

    @Test
    public void Changes_to_file_name_are_saved_under_user_preferences() {
        UserPreferences up = UserPreferences.getInstance();
        up.resetUserPreferences(); // reset to default values
        up.setTodoDataPath("/Users/Jim/Dropbox/ToDoData.csv");
        assertEquals("/Users/Jim/Dropbox/ToDoData.csv", up.getTodoDataPath());
        up.resetUserPreferences();
    }

    @Test
    public void Serialization_works_correctly() {
        UserPreferences up = UserPreferences.getInstance();
        up.resetUserPreferences();
        up.setTodoDataPath("/Family/Mum/Documents/ToDoData.csv");
        assertEquals("{\"todoDataPath\":\"/Family/Mum/Documents/ToDoData.csv\"}", up.prepareJson());
        up.resetUserPreferences();
    }

    @Test
    public void Writing_to_json_file_works_correctly() {
        UserPreferences up = UserPreferences.getInstance();
        up.resetUserPreferences();
        up.setTodoDataPath("/Public/Guest/Downloads/ToDoData.csv");
        try {
            up.writeUserPreferencesToDisk();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
        File file = new File("data/user/UserPreferences.json");
        String read = "";
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            read = bufferedReader.readLine();
        } catch (FileNotFoundException e) {
            ExceptionHandler.handle(e);
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
        assertEquals("{\"todoDataPath\":\"/Public/Guest/Downloads/ToDoData.csv\"}", read);
        file.delete(); // delete file so that future tests will not be affected
        up.resetUserPreferences();
    }

    @Test
    public void Deserialization_works_correctly() {
        UserPreferences up = UserPreferences.getInstance();
        up.resetUserPreferences();
        String toDeserialize = "{\"todoDataPath\":\"/Users/Jane/GoogleDrive/ToDoData.csv\"}";
        up.handleJson(toDeserialize);
        assertEquals("/Users/Jane/GoogleDrive/ToDoData.csv", up.getTodoDataPath());
        up.resetUserPreferences();
    }

    @Test
    public void Reading_from_existing_user_preferences_data_file_on_disk_works_correctly() {
        // Write a json file to disk
        File file = new File("data/user/UserPreferences.json");
        file.delete();
        assertFalse(file.exists());
        File folder = new File("data/user/UserPreferences.json").getParentFile();
        folder.mkdirs();
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(("data/user/UserPreferences.json")));
            bufferedWriter.write("{\"todoDataPath\":\"/Home/XiaoMing/Dropbox/ToDoData.csv\"}");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(file.exists());

        UserPreferences up = UserPreferences.getInstance();
        up.createOrReadPreferencesFile();
        assertEquals("/Home/XiaoMing/Dropbox/ToDoData.csv", up.getTodoDataPath());

        file.delete();
        up.resetUserPreferences();
    }
}
