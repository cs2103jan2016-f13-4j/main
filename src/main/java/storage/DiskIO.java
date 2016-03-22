package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Handles reading from and writing to disk.
 * 
 * @author Huiyie
 *
 */
public class DiskIO {

    private String _fileName;
    private final TaskCollection _taskCollection;
    private final String DEFAULT_FILE_NAME = "tmp/ToDoData.csv";

    public DiskIO(TaskCollection taskCollection, String fileName) {
        this._taskCollection = taskCollection;
        if (fileName == null || fileName.equals(null) || fileName.equals("")) {
            // assign default file name
            fileName = this.DEFAULT_FILE_NAME;
        }
        this._fileName = fileName;

        if (this._fileName != null) {
            // Try to create directory
            File folder = new File(this._fileName).getParentFile();
            folder.mkdirs();
        }
    }

    public DiskIO(TaskCollection taskCollection) {
        this(taskCollection, null);
    }

    public TaskCollection read() throws IOException {
        if (this._fileName == null) {
            // TODO: Return null
            return null;
        }

        boolean isFile = this.checkIsFile();
        if (!isFile) {
            File f = new File(this._fileName);
            isFile = f.createNewFile();
            return this._taskCollection;
        }

        BufferedReader reader = new BufferedReader(new FileReader(this._fileName));
        String currLine;
        while ((currLine = reader.readLine()) != null) {
            Task task = new Task(null, null, null, null, null);
            task.decodeTaskFromString(currLine);
            task.setId(null); // set null id to indicate this is a new task to
                              // be added, not an update
            this._taskCollection.save(task);
        }
        reader.close();
        return this._taskCollection;
    }

    public List<Task> write() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(this._fileName)));
        List<Task> taskList = this._taskCollection.getAll();
        for (Task task : taskList) {
            writer.write(task.encodeTaskToString());
            writer.newLine();
        }
        writer.close();
        return taskList;
    }

    public boolean checkIsFile() {
        return new File(this._fileName).isFile();
    }

}
