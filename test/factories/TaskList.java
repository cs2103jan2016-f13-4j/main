package factories;

import back_end.storage.base.Index;
import back_end.storage.relations.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by maianhvu on 5/3/16.
 */
public class TaskList {

    private static final int QUANTITY_BUILD_DEFAULT = 3;

    public static List<Task> buildGeneric(int quantity) {
        ArrayList<Task> taskList = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            Task task = new Task("Task" + i);
            task.setPrimaryKey(new Index((long) i));
            taskList.add(task);
        }
        return taskList;
    }

    public static List<Task> buildGeneric() {
        return buildGeneric(QUANTITY_BUILD_DEFAULT);
    }

    public static List<Task> buildRandom(int quantity) {
        ArrayList<Task> taskList = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= quantity; i++) {
            Task task = new Task("Task" + i);
            task.setPrimaryKey(new Index(random.nextLong()));
            taskList.add(task);
        }

        // Sort list
        taskList.sort(null);

        return taskList;
    }

    public static List<Task> buildRandom() {
        return buildRandom(QUANTITY_BUILD_DEFAULT);
    }
}
