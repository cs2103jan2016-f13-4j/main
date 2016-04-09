package logic;

import shared.CustomTime;
import shared.Task;
import shared.TemporalRange;
import skeleton.SchedulerSpec;
import storage.Storage;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public boolean isColliding(Task task) {
        return !Storage.getInstance().getAll()
                .stream()
                .filter(otherTask -> {
                    CustomTime startOfTask = task.getStartTime();
                    CustomTime endOfTask = task.getEndTime();
                    CustomTime startOfOtherTask = otherTask.getStartTime();
                    CustomTime endOfOtherTask = otherTask.getEndTime();

                    boolean endsStrictlyBefore = endOfTask.compareTo(startOfOtherTask) <= 0;
                    boolean startsStrictlyAfter = startOfTask.compareTo(endOfOtherTask) >= 0;

                    return !(endsStrictlyBefore || startsStrictlyAfter);
                }).collect(Collectors.toList()).isEmpty();
    }

    @Override
    public CustomTime getFreeSlot(CustomTime lowerBound, CustomTime upperBound) {
        List<TemporalRange> occupiedRanges = this.collapseOverlappingRanges(Storage.getInstance().getAll()
        .stream()
        .map(task -> new TemporalRange(task.getStartTime(), task.getEndTime()))
        .collect(Collectors.toList()))
                .stream()
                .filter(task -> lowerBound.compareTo(task.getStart()) <= 0 && upperBound.compareTo(task.getEnd()) >= 0)
                .collect(Collectors.toList());

        return null;
    }


    /*
     * helper methods
     */

    protected List<TemporalRange> collapseOverlappingRanges(List<TemporalRange> ranges) {
        List<TemporalRange> collapsedRanges = new LinkedList<>();
        List<TemporalRange> originalRanges = ranges
                .stream()
                .sorted((range1, range2) -> range1.getStart().compareTo(range2.getStart()))
                .collect(Collectors.toList());

        TemporalRange currentRange = TemporalRange.getUniversalRange();

        for (TemporalRange range : originalRanges) {
            if (range.overlaps(currentRange) || range.isAdjacentTo(currentRange)) {
                // the range accumulator can still be extended with this one
                currentRange = currentRange.merge(range);
            } else {
                // freeze the current accumulator and add it to the list of collapsed ranges, start a new range
                collapsedRanges.add(currentRange);
                currentRange = range;
            }
        }
        collapsedRanges.add(currentRange);

        return collapsedRanges;
    }
}
