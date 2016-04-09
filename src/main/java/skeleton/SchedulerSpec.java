package skeleton;

import shared.*;

import java.util.List;

/**
 * @@author Thenaesh Elango
 */
public interface SchedulerSpec {
    public boolean isColliding(Task task);

    public List<TemporalRange> getFreeSlots(CustomTime lowerBound, CustomTime upperBound);
}
