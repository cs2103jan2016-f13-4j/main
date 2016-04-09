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
    public List<TemporalRange> getFreeSlots(CustomTime lowerBound, CustomTime upperBound) {
        // if lowerBound >= upperBound, return an empty list for obvious reasons
        if (lowerBound.compareTo(upperBound) >= 0) {
            return new LinkedList<TemporalRange>();
        }

        // first get the disjoint list of occupied time slots, sorted in chronological order
        List<TemporalRange> occupiedRanges = this.collapseOverlappingRanges(this._storage.getAll()
                .stream()
                .map(task -> new TemporalRange(task.getStartTime(), task.getEndTime()))
                .collect(Collectors.toList()));

        // strip tasks that end before the lower bound or start after the upper bound
        occupiedRanges = occupiedRanges
                .stream()
                .filter(task -> !(lowerBound.compareTo(task.getEnd()) >= 0 || upperBound.compareTo(task.getStart()) <= 0))
                .collect(Collectors.toList());

        /*
         * run through the whole list of occupied ranges
         * iteratively build the free ranges starting from the lower bound
         * for each occupied range,
         *     its start ends the free range just prior to it
         *     its end starts the free range just after it
         */
        List<TemporalRange> freeSlots = new LinkedList<>();
        CustomTime currentFreeSlotStartTime = lowerBound;

        for (TemporalRange range : occupiedRanges) {
            TemporalRange freeRangeToAdd = new TemporalRange(currentFreeSlotStartTime, range.getStart());
            freeSlots.add(freeRangeToAdd);
            currentFreeSlotStartTime = range.getEnd(); // the next free range starts only after the current occupied range ends
        }
        freeSlots.add(new TemporalRange(currentFreeSlotStartTime, upperBound));

        // fix potential problems at the endpoints if there are occupied ranges that cross the lower or upper bounds
        freeSlots = freeSlots
                .stream()
                .filter(range -> range.getStart().compareTo(range.getEnd()) < 0)
                .collect(Collectors.toList());

        return freeSlots;
    }


    /*
     * helper methods
     */

    /**
     * takes a list of temporal ranges and returns a new list of disjoint ranges that represent
     * the time spanned by all the ranges
     * the list is first sorted by start time, which ultimately results in a list sorted in chronological order
     *
     * @param ranges the original ranges
     * @return new list of disjoint ranges sorted in chronological order
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
