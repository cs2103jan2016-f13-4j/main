package shared;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @@author Mai Anh Vu
 */
public class RangeTest {

    @Test
    public void Range_of_single_index_has_only_start_value() {
        Range range = new Range(6);
        assertFalse(range.hasEnd());
    }

    @Test
    public void Range_is_inclusive() {
        Range range = new Range(5, 10);
        assertTrue(range.contains(5));
        assertTrue(range.contains(10));
    }

    @Test
    public void Range_of_only_start_does_not_contain_anything_after() {
        Range range = new Range(8);
        assertFalse(range.contains(9));
    }

    @Test
    public void Range_contains_a_value_inbetween() {
        Range range = new Range(-1, 9);
        assertTrue(range.contains(5));
    }

    @Test
    public void Range_does_not_contain_outside_values() {
        Range range = new Range(-5, 9);
        assertFalse(range.contains(-6));
        assertFalse(range.contains(10));
    }

    @Test
    public void Ranges_with_same_bounds_are_equal() {
        Range range1 = new Range(1, 6);
        Range range2 = new Range(1, 6);
        assertThat(range1, is(equalTo(range2)));
    }

    @Test
    public void Ranges_with_same_start_but_one_null_end_are_not_equal() {
        Range range1 = new Range(1, 6);
        Range range2 = new Range(1);
        assertThat(range1, is(not(equalTo(range2))));
    }

    @Test
    public void Ranges_with_same_start_but_different_ends_are_not_equal() {
        Range range1 = new Range(1, 5);
        Range range2 = new Range(1, 6);
        assertThat(range1, is(not(equalTo(range2))));
    }

    @Test
    public void Overlapping_ranges_are_not_equal() {
        Range range1 = new Range(1, 5);
        Range range2 = new Range(2, 5);
        assertThat(range1, is(not(equalTo(range2))));
    }

    @Test
    public void A_larger_range_contains_a_smaller_range() {
        Range larger = new Range(1, 7);
        Range smaller = new Range(3, 5);
        assertTrue(larger.contains(smaller));
    }

    @Test
    public void A_single_index_range_contains_a_range_with_same_start_end_values() {
        Range index = new Range(5);
        Range range = new Range(5, 5);
        assertTrue(index.contains(range));
    }

    @Test
    public void A_smaller_range_does_not_contain_a_larger_range() {
        Range smaller = new Range(1, 4);
        Range larger = new Range(2, 5);
        assertFalse(smaller.contains(larger));
    }

    @Test
    public void Two_non_overlapping_ranges_do_not_contain_each_other() {
        Range range1 = new Range(1, 4);
        Range range2 = new Range(5, 6);
        assertFalse(range1.contains(range2));
        assertFalse(range2.contains(range1));
    }

    @Test
    public void A_range_should_contain_itself() {
        Range range = new Range(2, 6);
        assertTrue(range.contains(range));
    }

    @Test(expected = AssertionError.class)
    public void A_range_cannot_end_earlier_than_it_starts() {
        Range range = new Range(10, 1);
    }

    @Test
    public void Range_with_single_values_merge_when_continuous() {
        List<Range> ranges = new ArrayList<>(Arrays.asList(
                new Range(5),
                new Range(6),
                new Range(7),
                new Range(8)
        ));
        Range.straightenRanges(ranges);
        assertThat(ranges, hasSize(1));
        assertThat(ranges, hasItem(new Range(5, 8)));
    }
}
