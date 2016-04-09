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
    private static final Scheduler instance = new Scheduler(Storage.getInstance());
    public static Scheduler getInstance() {
        return instance;
    }

    protected Scheduler(Storage storage) {
        this._storage = storage;
    }


    private Storage _storage;


    @Override
    public boolean isColliding(Task task) {
        TemporalRange taskRange = new TemporalRange(task.getStartTime(), task.getEndTime());

        return !this._storage.getAll()
                .stream()
                .map(task_ -> new TemporalRange(task_.getStartTime(), task_.getEndTime()))
                .filter(range_ -> taskRange.overlaps(range_))
                .collect(Collectors.toList())
                .isEmpty();
    }

    @Override
    public CustomTime getFreeSlot(CustomTime lowerBound, CustomTime upperBound) {
        List<TemporalRange> occupiedRanges = this.collapseOverlappingRanges(this._storage.getAll()
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

    /**
     * takes a list of temporal ranges and returns a new list of disjoint ranges that represent
     * the time spanned by all the ranges
     *
     * @param ranges the original ranges
     * @return new list of disjoint ranges sorted by start time
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
