package skeleton;

import shared.*;

/**
 * @@author Thenaesh Elango
 */
public interface SchedulerSpec {
    public boolean isColliding(Task task);

    public CustomTime getFreeSlot(CustomTime lowerBound, CustomTime upperBound);
}
