package storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

        File file = new File(this._fileName);

        // Try to create directory
        File folder = new File(this._fileName).getParentFile();
        folder.mkdirs();

        // Try to create file
        try {
            file.createNewFile();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }

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

}
