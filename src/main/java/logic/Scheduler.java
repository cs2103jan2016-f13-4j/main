package logic;

import shared.CustomTime;
import shared.Task;
import shared.TemporalRange;
import skeleton.SchedulerSpec;
import storage.Storage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @@author Thenaesh Elango
 */
public class Scheduler implements SchedulerSpec {

    // singleton instance, constructor and accessor
    private static final Scheduler instance = new Scheduler(Storage.getInstance());
    public static Scheduler getInstance() {
        return instance;
    }
    protected Scheduler(Storage storage) {
        this._storage = storage;
    }

    // storage instance to be used by this scheduler
    private Storage _storage;


    /**
     * finds a time range in which to slot a task of specified duration in
     * @param durationInMinutes of task to slot in
     * @return time range to slot the task in
     */
    @Override
    public TemporalRange schedule(Integer durationInMinutes) {

        // get the task that ends the latest, if it exists
        Optional<CustomTime> endOfRangeToSearchIfItExists = this._storage.getAll()
                .stream()
                .filter(task -> task.getStartTime() != null && task.getEndTime() != null)
                .map(Task::getEndTime)
                .max(CustomTime::compareTo);

        // if such a task does not exist, then there are no tasks, so just schedule the new task immediately
        if (!endOfRangeToSearchIfItExists.isPresent()) {
            return new TemporalRange(CustomTime.now(), CustomTime.now().plusMinutes(durationInMinutes));
        }

        CustomTime startOfRangeToSearch = CustomTime.now();
        CustomTime endOfRangeToSearch = endOfRangeToSearchIfItExists.get();

        List<TemporalRange> freeSlots = this.getFreeSlots(startOfRangeToSearch, endOfRangeToSearch);

        List<TemporalRange> suitableFreeSlots = freeSlots
                .stream()
                .filter(range -> CustomTime.difference(range.getStart(), range.getEnd()) >= durationInMinutes)
                .map(range -> {
                    CustomTime startTime = range.getStart();
                    CustomTime endTime = new CustomTime(
                            LocalDateTime.of(startTime.getDate(), startTime.getTime())
                                    .plusMinutes(durationInMinutes));
                    return new TemporalRange(startTime, endTime);
                })
                .collect(Collectors.toList());

        // just choose the day after the end date if we can't find a suitable slot
        if (suitableFreeSlots.isEmpty()) {
            return new TemporalRange(endOfRangeToSearch.plusDays(1),
                    endOfRangeToSearch.plusDays(1).plusMinutes(durationInMinutes));
        } else {
            // get the first suitable free slot and return it
            return suitableFreeSlots.get(0);
        }
    }

    /**
     * checks task to be inserted for clashes (collisions) with pre-existing tasks
     * @param task to check for collision
     * @return does the task collide?
     */
    @Override
    public boolean isColliding(Task task) {
        TemporalRange taskRange = new TemporalRange(task.getStartTime(), task.getEndTime());

        if (taskRange.getStart() == null || taskRange.getEnd() == null) {
            return false;
        }

        return !this._storage.getAll()
                .stream()
                .map(task_ -> new TemporalRange(task_.getStartTime(), task_.getEndTime()))
                .filter(range -> range.getStart() != null && range.getEnd() != null) // ensure start and end time objects are there
                .filter(range_ -> taskRange.overlaps(range_))
                .collect(Collectors.toList())
                .isEmpty();
    }


    // HELPER METHODS //

    /**
     * gets free time slots within the specified bounds
     * the resulting list will be disjoint and sorted
     * @param lowerBound
     * @param upperBound
     * @return
     */
    protected List<TemporalRange> getFreeSlots(CustomTime lowerBound, CustomTime upperBound) {
        // if lowerBound >= upperBound, return an empty list for obvious reasons
        if (lowerBound.compareTo(upperBound) >= 0) {
            return new LinkedList<TemporalRange>();
        }

        // first get the disjoint list of occupied time slots, sorted in chronological order
        List<TemporalRange> occupiedRanges = this._storage.getAll()
                .stream()
                .map(task -> new TemporalRange(task.getStartTime(), task.getEndTime()))
                .collect(Collectors.toList());

        // remove tasks with missing start/end times
        occupiedRanges = this.collapseOverlappingRanges(occupiedRanges
                .stream()
                .filter(range -> range.getStart() != null && range.getEnd() != null)
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

    /**
     * takes a disjoint, sorted time range of time slots and adds time slots blocking out the qiet period from 0000-0800
     * @param ranges disjoint and sorted
     * @return a disjoint and sorted range expanded to include the additional slots denoting the quiet period
     */
    protected List<TemporalRange> blockOutQuietTime(List<TemporalRange> ranges) {
        LocalDate minDate = ranges
                .stream()
                .map(TemporalRange::getStart)
                .min(CustomTime::compareTo)
                .get().getDate();
        LocalDate maxDate = ranges
                .stream()
                .map(TemporalRange::getEnd)
                .max(CustomTime::compareTo)
                .get().getDate();

        List<LocalDate> dateRange = new LinkedList<>();
        for (LocalDate i = minDate; i.isBefore(maxDate) || i.isEqual(maxDate); i = i.plusDays(1)) {
            dateRange.add(i);
        }

        List<TemporalRange> blockedQuietTimeRanges = dateRange
                .stream()
                .map(date -> {
                    CustomTime start = new CustomTime(date, LocalTime.of(0, 0));
                    CustomTime end = new CustomTime(date, LocalTime.of(8, 0));
                    return new TemporalRange(start, end);
                })
                .collect(Collectors.toList());

        blockedQuietTimeRanges.addAll(this.collapseOverlappingRanges(ranges));
        return this.collapseOverlappingRanges(blockedQuietTimeRanges);
    }
}
