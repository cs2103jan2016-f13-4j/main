package logic;

import skeleton.SchedulerSpec;

/**
 * Created by maianhvu on 20/03/2016.
 */
public class Scheduler implements SchedulerSpec {
    private static final Scheduler instance = new Scheduler();

    public static Scheduler getInstance() {
        return instance;
    }

    private Scheduler() {
    }
}
