package component.back_end.storage;

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
    
    private TaskCollection taskCollection_;
    private TaskDescriptor taskDescriptor_ = null;
    private List<Task> taskList_;
    private BufferedWriter writer_;
    private String fileName_ = "ToDoData.csv";
    
    public TaskCollectionWriter(TaskCollection collection) throws IOException {
        this.taskCollection_ = collection;  
    }
    
    public TaskCollectionWriter(TaskCollection collection, String fileName) throws IOException {
        this.taskCollection_ = collection;
        this.fileName_ = fileName;
    }
    
    public void save() throws IOException {
        this.writer_ = new BufferedWriter(new FileWriter(this.fileName_));
        this.taskList_ = this.taskCollection_.getAll(this.taskDescriptor_);
        for (Task task : this.taskList_) {
            this.writer_.write(task.encodeTaskToString());
            this.writer_.newLine();
        }
        this.writer_.close();
    }

}
