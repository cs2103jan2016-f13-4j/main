package logic;

import static org.junit.Assert.*;

import org.junit.Test;
import shared.CustomTime;
import shared.TemporalRange;

import java.time.Month;
import java.util.LinkedList;
import java.util.List;

/**
 * @@author Thenaesh Elango
 */
public class SchedulerTest {

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


    @Test
    public void Collapsing_disjoint_ranges_works() {
        List<TemporalRange> originalRanges = new LinkedList<>();
        List<TemporalRange> expectedRanges = new LinkedList<>();

        originalRanges.add(range1);
        originalRanges.add(range3);

        expectedRanges.add(range1);
        expectedRanges.add(range3);

        assertEquals(Scheduler.getInstance().collapseOverlappingRanges(originalRanges), expectedRanges);
    }

    @Test
    public void Collapsing_adjacent_ranges_works() {
        List<TemporalRange> originalRanges = new LinkedList<>();
        List<TemporalRange> expectedRanges = new LinkedList<>();

        originalRanges.add(range1);
        originalRanges.add(range2);
        originalRanges.add(range3);

        expectedRanges.add(range6);

        assertEquals(Scheduler.getInstance().collapseOverlappingRanges(originalRanges), expectedRanges);
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

        assertEquals(Scheduler.getInstance().collapseOverlappingRanges(originalRanges1), expectedRanges1);
        assertEquals(Scheduler.getInstance().collapseOverlappingRanges(originalRanges2), expectedRanges2);
    }

    @Test
    public void Collapsing_general_ranges_works() {
        List<TemporalRange> originalRanges = new LinkedList<>();
        List<TemporalRange> expectedRanges = new LinkedList<>();

        originalRanges.add(range4);
        originalRanges.add(range5);

        expectedRanges.add(range6);

        assertEquals(Scheduler.getInstance().collapseOverlappingRanges(originalRanges), expectedRanges);
    }
}
