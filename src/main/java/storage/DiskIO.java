package storage;

import skeleton.CollectionSpec;

import java.io.*;
import java.util.List;

public class DiskIO {


    private final TaskCollection _taskCollection;
    private String _fileName = "data/ToDoData.csv";
    private final DescriptorSpec<Task> _taskDescriptor = null;

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
}
