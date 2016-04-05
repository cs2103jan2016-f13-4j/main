package shared;

/**
 * Created by maianhvu on 05/04/2016.
 */
public class Range {

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
}
