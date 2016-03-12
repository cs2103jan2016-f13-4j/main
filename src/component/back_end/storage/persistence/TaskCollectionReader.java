package component.back_end.storage.persistence;

import component.back_end.storage.Task;
import component.back_end.storage.TaskCollection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Reads a collection of Tasks from file
 * @author Huiyie
 *
 */
public class TaskCollectionReader {

    private final TaskCollection taskCollection_;
    private String fileName_ = "data/ToDoData.csv";
    private boolean isFile_ = false;
    
    public TaskCollectionReader(TaskCollection taskCollection) {
        this.taskCollection_ = taskCollection;
        this.isFile_ = new File(this.fileName_).isFile();
    }
    
    public void read() throws IOException {
        if (!this.isFile_) {
            File file = new File(this.fileName_);
            // no data file exists, create new file and return
            this.isFile_ = file.createNewFile();
            return;
        }
        
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
