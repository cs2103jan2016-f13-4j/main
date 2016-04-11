package skeleton;

import shared.*;

import java.util.List;

/**
 * The Scheduler is called upon by the Decision Engine to schedule a floating task,
 * as well as to check if a task is colliding.
 *
 * @@author Thenaesh Elango
 */
public interface SchedulerSpec {

    public TemporalRange schedule(Integer durationInMinutes);

    public boolean isColliding(Task task);
}
