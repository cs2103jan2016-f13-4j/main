package shared;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @@author A0127046L
 */
public class Range implements Comparable<Range> {

    /**
     * Properties
     */
    private Integer _start;
    private Integer _end;

    /**
     * Constructs a range with the specified start and end (both inclusive).
     * @param start an integer value
     * @param end an integer value
     */
    public Range(int start, int end) {
        assert start <= end;
        this._start = start;
        this._end = end;
    }

    /**
     * Constructs a range that contains only one value.
     * @param singleIndex the value to contain
     */
    public Range(int singleIndex) {
        this._start = singleIndex;
    }

    public boolean hasEnd() {
        return this._end != null;
    }

    public boolean contains(int value) {
        if (!hasEnd()) { // Single index
            return this._start == value;
        }
        assert hasEnd();
        return value >= this._start && value <= this._end;
    }

    public boolean contains(Range another) {
        // If the other value is a single value, fall back
        // to the single value contains method
        if (!another.hasEnd()) {
            return this.contains(another.getStart());
        }

        // If the second value starts smaller than this then
        // it is definitely not contained inside
        if (another.getStart() < this.getStart()) {
            return false;
        }

        // When this doesn't have an end, the only case where
        // the second one is contained inside this, is where
        // the end value is exactly equal to the start value
        if (!this.hasEnd()) {
            return another.getEnd().equals(another.getStart());
        }

        // Compare the end points
        return another.getEnd() <= this.getEnd();
    }

    public Integer getStart() {
        return this._start;
    }

    public Integer getEnd() {
        return this._end;
    }

    public void setStart(int start) {
        this._start = start;
    }

    public void setEnd(int end) {
        this._end = end;
    }

    /**
     * For debugging.
     * @return
     */
    @Override public String toString() {
        if (!this.hasEnd()) {
            return this._start.toString();
        }
        return String.format("[%d,%d]", this._start, this._end);
    }

    @Override public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof Range)) return false;

        Range otherRange = (Range) o;
        // Unequal start
        if (!this.getStart().equals(otherRange.getStart())) {
            return false;
        }
        // Either one doesn't have (exclusive or)
        if (this.hasEnd() ^ otherRange.hasEnd()) {
            return false;
        }

        // Both doesn't have end
        if (!this.hasEnd()) {
            return true;
        }

        return this.getEnd().equals(otherRange.getEnd());
    }

    @Override public int compareTo(Range another) {
        return this.getStart().compareTo(another.getStart());
    }

    public static void straightenRanges(List<Range> rangeList) {
        Collections.sort(rangeList);
        Range previousRange = null;
        for (Iterator<Range> it = rangeList.iterator(); it.hasNext(); ) {
            Range thisRange = it.next();
            if (previousRange == null) {
                previousRange = thisRange;
                continue;
            }
            boolean didMerge = false;
            // Check for containment or overlap
            if (previousRange.contains(thisRange) || previousRange.contains(thisRange.getStart())) {
                // Does contain, merge the two!
                if (thisRange.hasEnd() && (!previousRange.hasEnd() || previousRange.hasEnd() &&
                        previousRange.getEnd() < thisRange.getEnd())) {
                    previousRange.setEnd(thisRange.getEnd());
                }
                it.remove();
                didMerge = true;
            }
            // Check for continuation of end [5, 6] + [6, 8]
            if (previousRange.hasEnd() && thisRange.getStart().equals(previousRange.getEnd() + 1)) {
                if (thisRange.hasEnd()) {
                    previousRange.setEnd(thisRange.getEnd());
                } else {
                    previousRange.setEnd(thisRange.getStart());
                }
                it.remove();
                didMerge = true;
            }
            // Check for continuation of start [5] [6] [7]
            if (!(previousRange.hasEnd() && thisRange.hasEnd()) &&
                    thisRange.getStart().equals(previousRange.getStart() + 1)) {
                previousRange.setEnd(thisRange.getStart());
                it.remove();
                didMerge = true;
            }

            if (!didMerge) {
                previousRange = thisRange;
            }
        }
    }

    public static int[] enumerateRanges(List<Range> ranges) {
        return ranges.stream().flatMapToInt(range -> {
            if (!range.hasEnd()) {
                return IntStream.of(range.getStart());
            } else {
                return IntStream.range(range.getStart(), range.getEnd() + 1);
            }
        }).sorted().toArray();
    }
}
