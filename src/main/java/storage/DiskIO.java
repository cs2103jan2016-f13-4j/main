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
 * @author Huiyie
 *
 */
public class DiskIO {

    private static final DiskIO instance = new DiskIO();

    public static DiskIO getInstance() {
        return instance;
    }

    private String _fileName;
    // private final Storage _taskCollection;
    private final String DEFAULT_FILE_NAME = "tmp/ToDoData.csv";

    private DiskIO() {
        this._fileName = this.DEFAULT_FILE_NAME;

        // Try to create directory
        File folder = new File(this._fileName).getParentFile();
        folder.mkdirs();

    }

    public ArrayList<String> read() throws IOException {
        // Create file if it does not already exist
        this.checkFileExists();

        BufferedReader reader = new BufferedReader(new FileReader(this._fileName));
        ArrayList<String> taskStringList = new ArrayList<String>();
        String currLine;
        while ((currLine = reader.readLine()) != null) {
            taskStringList.add(currLine);
        }
        reader.close();
        return taskStringList;
    }

    public String write(String line) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(this._fileName), true));

        writer.write(line);
        writer.newLine();

        writer.close();
        return line;
    }

    public boolean checkIsFile() {
        return new File(this._fileName).isFile();
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
