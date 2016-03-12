package component.back_end.storage.persistence;

import component.back_end.storage.Task;
import component.back_end.storage.TaskCollection;
import component.back_end.storage.query.TaskDescriptor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Writes a collection of Tasks into file
 * @author Huiyie
 *
 */
public class TaskCollectionWriter {
    
    private final TaskCollection taskCollection_;
    private final TaskDescriptor taskDescriptor_ = null;
    private String fileName_ = "data/ToDoData.csv";
    
    public TaskCollectionWriter(TaskCollection collection) {
        this.taskCollection_ = collection;  
    }
    
    public TaskCollectionWriter(TaskCollection collection, String fileName) {
        this.taskCollection_ = collection;
        this.fileName_ = fileName;
    }
    
    public void save() throws IOException {
        BufferedWriter writer_ = new BufferedWriter(new FileWriter(this.fileName_));
        List<Task> taskList_ = this.taskCollection_.getAll(this.taskDescriptor_);
        for (Task task : taskList_) {
            writer_.write(task.encodeTaskToString());
            writer_.newLine();
        }
        writer_.close();
    }

}
