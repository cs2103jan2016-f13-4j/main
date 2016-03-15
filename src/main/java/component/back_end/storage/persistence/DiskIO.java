package component.back_end.storage.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import component.back_end.storage.Task;
import component.back_end.storage.TaskCollection;
import component.back_end.storage.query.TaskDescriptor;

/**
 * Handles reading from and writing to disk.
 * @author Huiyie
 *
 */
public class DiskIO {

    private final TaskCollection _taskCollection;
    private String _fileName = "data/ToDoData.csv";
    private final TaskDescriptor _taskDescriptor = null;

    public DiskIO(TaskCollection taskCollection, String fileName) {
        this._taskCollection = taskCollection;
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

        boolean isFile = new File(this._fileName).isFile();
        if (!isFile) {
            File f = new File(this._fileName);
            isFile = f.createNewFile();
            return this._taskCollection;
        }

        BufferedReader reader = new BufferedReader(new FileReader(this._fileName));
        String currLine;
        while((currLine = reader.readLine()) != null) {
            Task task = new Task(null, null, null, null, null);
            task.decodeTaskFromString(currLine);
            task.setId(null); // set null id to indicate this is a new task to be added, not an update
            this._taskCollection.save(task);
        }
        reader.close();
        return this._taskCollection;
    }

    public List<Task> write() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(this._fileName));
        List<Task> taskList = this._taskCollection.getAll(this._taskDescriptor);
        for (Task task : taskList) {
            writer.write(task.encodeTaskToString());
            writer.newLine();
        }
        writer.close();
        return taskList;
    }

    public static void main(String[] args) throws IOException {
        DiskIO io = new DiskIO(new TaskCollection());
        io.write();
    }

}
