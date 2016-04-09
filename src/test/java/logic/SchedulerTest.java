package logic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import shared.CustomTime;
import shared.Task;
import shared.TemporalRange;
import storage.Storage;

import java.time.Month;
import java.util.LinkedList;
import java.util.List;


/**
 * @@author Thenaesh Elango
 */
public class SchedulerTest {
    /**
     * class to enable dependency injection of Storage
     * @@author Thenaesh Elango
     */
    static class StorageTester extends Storage {
        public StorageTester() {
            super();
        }
    }


    public static final CustomTime time1 = new CustomTime(2016, Month.APRIL, 1, 8, 0);
    public static final CustomTime time2 = new CustomTime(2016, Month.APRIL, 2, 16, 0);
    public static final CustomTime time3 = new CustomTime(2016, Month.APRIL, 3, 12, 0);
    public static final CustomTime time4 = new CustomTime(2016, Month.APRIL, 4, 7, 30);

    public static final TemporalRange range1 = new TemporalRange(time1, time2);
    public static final TemporalRange range2 = new TemporalRange(time2, time3);
    public static final TemporalRange range3 = new TemporalRange(time3, time4);
    public static final TemporalRange range4 = new TemporalRange(time1, time3);
    public static final TemporalRange range5 = new TemporalRange(time2, time4);
    public static final TemporalRange range6 = new TemporalRange(time1, time4);

    public static final Task task1 = new Task(null, "1", "", time1, time2);
    public static final Task task2 = new Task(null, "2", "", time2, time3);
    public static final Task task3 = new Task(null, "3", "", time3, time4);
    public static final Task task4 = new Task(null, "4", "", time1, time3);
    public static final Task task5 = new Task(null, "5", "", time2, time4);
    public static final Task task6 = new Task(null, "6", "", time1, time4);


    // dependency injection of Storage
    private Storage _storage;
    private Scheduler _scheduler;

    @Before
    public void setUp() {
        this._storage = new StorageTester();
        this._scheduler = new Scheduler(this._storage);
    }


    @Test
    public void Collapsing_disjoint_ranges_works() {
        List<TemporalRange> originalRanges = new LinkedList<>();
        List<TemporalRange> expectedRanges = new LinkedList<>();

        originalRanges.add(range1);
        originalRanges.add(range3);

        expectedRanges.add(range1);
        expectedRanges.add(range3);

        assertEquals(this._scheduler.collapseOverlappingRanges(originalRanges), expectedRanges);
    }

    @Test
    public void Collapsing_adjacent_ranges_works() {
        List<TemporalRange> originalRanges = new LinkedList<>();
        List<TemporalRange> expectedRanges = new LinkedList<>();

        originalRanges.add(range1);
        originalRanges.add(range2);
        originalRanges.add(range3);

        expectedRanges.add(range6);

        assertEquals(this._scheduler.collapseOverlappingRanges(originalRanges), expectedRanges);
    }

    @Test
    public void Collapsing_nested_ranges_works() {
        List<TemporalRange> originalRanges1 = new LinkedList<>(), originalRanges2 = new LinkedList<>();
        List<TemporalRange> expectedRanges1 = new LinkedList<>(), expectedRanges2 = new LinkedList<>();

        originalRanges1.add(range1);
        originalRanges1.add(range4);
        originalRanges2.add(range3);
        originalRanges2.add(range5);
        originalRanges2.add(range3);

        expectedRanges1.add(range4);
        expectedRanges2.add(range5);

        assertEquals(this._scheduler.collapseOverlappingRanges(originalRanges1), expectedRanges1);
        assertEquals(this._scheduler.collapseOverlappingRanges(originalRanges2), expectedRanges2);
    }

    @Test
    public void Collapsing_general_ranges_works() {
        List<TemporalRange> originalRanges = new LinkedList<>();
        List<TemporalRange> expectedRanges = new LinkedList<>();

        originalRanges.add(range4);
        originalRanges.add(range5);

        expectedRanges.add(range6);

        assertEquals(this._scheduler.collapseOverlappingRanges(originalRanges), expectedRanges);
    }

    @Test
    public void Collision_check_works_for_disjoint_tasks() {
        this._storage.save(task1);

        assertFalse(this._scheduler.isColliding(task3));
    }

    @Test
    public void Collision_check_works_for_adjacent_tasks() {
        this._storage.save(task1);
        this._storage.save(task2);

        assertFalse(this._scheduler.isColliding(task3));
    }

    @Test
    public void Collision_check_works_for_nested_tasks() {
        this._storage.save(task1);

        assertTrue(this._scheduler.isColliding(task4));
    }

    @Test
    public void Collision_check_works_for_general_tasks() {
        this._storage.save(task4);

        assertTrue(this._scheduler.isColliding(task5));
    }

    @Test
    public void Free_slot_computation_works() {
        this._storage.save(task1);
        this._storage.save(task3);

        List<TemporalRange> expectedFreeRange1 = new LinkedList<>(), expectedFreeRange2 = new LinkedList<>();

        expectedFreeRange1.add(range2);

        assertEquals(this._scheduler.getFreeSlots(time1, time4), expectedFreeRange1);
        assertEquals(this._scheduler.getFreeSlots(time2, time4), expectedFreeRange1);
        assertEquals(this._scheduler.getFreeSlots(time1, time3), expectedFreeRange1);
        assertEquals(this._scheduler.getFreeSlots(time2, time3), expectedFreeRange1);
        assertEquals(this._scheduler.getFreeSlots(time1, time2), expectedFreeRange2);
        assertEquals(this._scheduler.getFreeSlots(time3, time4), expectedFreeRange2);
    }
}
