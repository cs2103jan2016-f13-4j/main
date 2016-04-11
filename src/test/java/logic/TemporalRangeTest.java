package logic;

import static org.junit.Assert.*;

import org.junit.Test;
import shared.CustomTime;
import shared.TemporalRange;

import java.time.Month;

/**
 * @@author A0124772E
 */
public class TemporalRangeTest {
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
    public void Overlap_detection_works_for_non_overlapping_adjacent_ranges() {
        assertFalse(range1.overlaps(range2));
        assertFalse(range2.overlaps(range3));
        assertFalse(range2.overlaps(range1));
        assertFalse(range3.overlaps(range2));
    }

    @Test
    public void Overlap_detection_works_for_non_overlapping_non_adjacent_ranges() {
        assertFalse(range1.overlaps(range3));
        assertFalse(range3.overlaps(range1));
    }

    @Test
    public void Overlap_detection_works_for_overlapping_contained_ranges() {
        assertTrue(range4.overlaps(range1));
        assertTrue(range4.overlaps(range2));
        assertTrue(range5.overlaps(range2));
        assertTrue(range5.overlaps(range3));
        assertTrue(range1.overlaps(range4));
        assertTrue(range2.overlaps(range4));
        assertTrue(range2.overlaps(range5));
        assertTrue(range3.overlaps(range5));
    }

    @Test
    public void Overlap_detection_works_for_overlapping_non_contained_ranges() {
        assertTrue(range4.overlaps(range5));
        assertTrue(range5.overlaps(range4));
    }

    @Test
    public void Adjacency_detection_works_for_adjacent_ranges() {
        assertTrue(range1.isAdjacentTo(range2));
        assertTrue(range2.isAdjacentTo(range1));
        assertTrue(range2.isAdjacentTo(range3));
        assertTrue(range3.isAdjacentTo(range2));
    }

    @Test
    public void Merge_works_for_non_overlapping_adjacent_ranges() {
        assertEquals(range1.merge(range2), range4);
        assertEquals(range2.merge(range1), range4);
        assertEquals(range2.merge(range3), range5);
        assertEquals(range3.merge(range2), range5);
    }

    @Test
    public void Merge_works_for_overlapping_ranges() {
        assertEquals(range4.merge(range5), range6);
    }

    @Test
    public void Merge_gives_null_on_ranges_with_no_points_in_common() {
        assertNull(range1.merge(range3));
        assertNull(range3.merge(range1));
    }

    @Test
    public void Universal_range_works_as_intended() {
        assertEquals(TemporalRange.getUniversalRange().merge(range1), range1);
        assertEquals(range2.merge(TemporalRange.getUniversalRange()), range2);

        assertEquals(TemporalRange.getUniversalRange().merge(TemporalRange.getUniversalRange()), TemporalRange.getUniversalRange());
    }
}
