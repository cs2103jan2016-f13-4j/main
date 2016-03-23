package logic;

import skeleton.SchedulerSpec;

/**
 * @@author Thenaesh Elango
 */
public class Scheduler implements SchedulerSpec {
    private static final Scheduler instance = new Scheduler();

    public static Scheduler getInstance() {
        return instance;
    }

    private Scheduler() {
    }
}
