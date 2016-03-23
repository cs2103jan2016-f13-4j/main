package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import exception.ExceptionHandler;

/**
 * Handles reading from and writing to disk.
 * 
 * @@author Chng Hui Yie
 *
 */
public class DiskIO {

    private static final DiskIO instance = new DiskIO();

    public static DiskIO getInstance() {
        return instance;
    }

/**
     * Properties
     */

    private String _fileName;
    private final String DEFAULT_FILE_NAME = "data/ToDoData.csv";

    private DiskIO() {
        this._fileName = this.DEFAULT_FILE_NAME;

        // Try to create directory
        File folder = new File(this._fileName).getParentFile();
        folder.mkdirs();
    }

    public ArrayList<String> read() {
        // Create file if it does not already exist
        this.checkFileExists();
        ArrayList<String> taskStrings = new ArrayList<String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(this._fileName));

            String currLine;
            while ((currLine = reader.readLine()) != null) {
                taskStrings.add(currLine);
            }
            reader.close();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
        return taskStrings;
    }

    public ArrayList<String> write(ArrayList<String> taskStrings) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(this._fileName), true));
            for (String taskString : taskStrings) {
                writer.write(taskString);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            ExceptionHandler.handle(e);
        }
        return taskStrings;
    }

    private File checkFileExists() {
        File file = new File(this._fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.handle(e);
            }
        }
        return file;
    }
}
