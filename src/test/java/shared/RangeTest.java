package shared;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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
}
