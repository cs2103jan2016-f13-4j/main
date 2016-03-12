package component.back_end.storage.persistence;

import component.back_end.storage.Task;
import component.back_end.storage.TaskCollection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Reads a collection of Tasks from file
 * @author Huiyie
 *
 */
public class TaskCollectionReader {

    private final TaskCollection taskCollection_;
    private String fileName_ = "ToDoData.csv";
    
    public TaskCollectionReader() {
        this.taskCollection_ = new TaskCollection();
    }
    
    public TaskCollectionReader(String fileName) {
        this.taskCollection_ = new TaskCollection();
        this.fileName_ = fileName;
    }
    
    public void read() throws IOException {
        BufferedReader reader_ = new BufferedReader(new FileReader(this.fileName_));
        String currLine;
        while ((currLine = reader_.readLine()) != null) {
            Task task = new Task(null, null, null, null, null);
            task.decodeTaskFromString(currLine);
            task.setId(null);
            this.taskCollection_.save(task);
        }
        reader_.close();
    }
    
    public TaskCollection getTaskCollection() {
        return taskCollection_;
    }

}
