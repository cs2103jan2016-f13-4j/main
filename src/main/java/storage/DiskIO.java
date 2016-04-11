package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exception.ExceptionHandler;
import shared.ApplicationContext;

/**
 * Handles reading from and writing to disk.
 * 
 * @@author A0127357B
 *
 */
public class DiskIO {

    /**
     * Singleton implementation
     */
    private static final DiskIO instance = new DiskIO();
    public static DiskIO getInstance() {
        return instance;
    }

    /**
     * Properties
     */
    private String _fileName;
    private UserPreferences _userPreferences;

    /**
     * Constructs a new DiskIO instance.
     */
    private DiskIO() {
        this._userPreferences = UserPreferences.getInstance();
        processUserPreferencesFile();
        this.createDirectory();
    }

    /**
     * Processes user preferences file stored on disk.
     */
    public void processUserPreferencesFile() {
        this._userPreferences.createOrReadPreferencesFile();
        this._fileName = this._userPreferences.getTodoDataPath();
    }

    /**
     * Creates directory for writing file to disk
     */
    public void createDirectory() {
        // Try to create directory
        File folder = new File(this._fileName).getParentFile();
        folder.mkdirs();
    }

    /**
     * Reads file from disk
     *
     * @return lines read from file as a List of String values
     */
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

    /**
     * Writes file to disk
     *
     * @param taskStrings
     *                     List of String values to be written to file
     * @return
     */
    public List<String> write(List<String> taskStrings) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this._fileName));
            for (String taskString : taskStrings) {
                writer.write(taskString);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
        return taskStrings;
    }

    /**
     * Checks that a file with the specified file name already exists
     *
     * @return
     */
    private File checkFileExists() {
        File file = new File(this._fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
        }
        return file;
    }

    /**
     * Getters
     */
    public String getFileName() {
        return this._fileName;
    }

    /**
     * Setters
     */
    public void setFileName(String fileName) {
        this._fileName = fileName;
        createDirectory();
    }
}
