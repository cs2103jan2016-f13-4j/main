package logic;

import skeleton.TaskSchedulerSpec;

/**
 * Created by maianhvu on 20/03/2016.
 */
public class TaskScheduler implements TaskSchedulerSpec {
    private static final TaskScheduler instance = new TaskScheduler();

    public static TaskScheduler getInstance() {
        return instance;
    }

    private TaskScheduler() {
    }
}
