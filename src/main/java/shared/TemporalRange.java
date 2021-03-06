package shared;

/**
 * Represents a range spanning two CustomTime bounds
 *
 * @@author A0124772E
 */
public class TemporalRange {
    private CustomTime _start;
    private CustomTime _end;

    /**
     * standard constructor
     * @param start
     * @param end
     */
    public TemporalRange(CustomTime start, CustomTime end) {
        this.setStart(start);
        this.setEnd(end);
    }

    /**
     * special constructor used only to create the universal range
     */
    private TemporalRange() {
        this._start = null;
        this._end = null;
    }


    /**
     * a special temporal range that overlaps and is contained in every other temporal range
     * it is universally quantified over its start and end times, so those values are completely ignored
     * @return
     */
    public static TemporalRange getUniversalRange() {
        return new TemporalRange() {

            @Override
            public boolean overlaps(TemporalRange rangeToCheck) {
                return true;
            }

            @Override
            public boolean isAdjacentTo(TemporalRange rangeToCheck) {
                return false;
            }

            @Override
            public TemporalRange merge(TemporalRange rangeToMerge) {
                return rangeToMerge;
            }

            @Override
            public boolean isUniversal() {
                return true;
            }
        };
    }


    public boolean isUniversal() {
        return false;
    }


    public CustomTime getStart() {
        return this._start;
    }

    public CustomTime getEnd() {
        return this._end;
    }

    public void setStart(CustomTime start) {
        this._start = start;
    }

    public void setEnd(CustomTime end) {
        this._end = end;
    }


    public boolean overlaps(TemporalRange rangeToCheck) {
        if (rangeToCheck.equals(getUniversalRange())) {
            return true;
        }

        boolean endsBefore = this.getEnd().compareTo(rangeToCheck.getStart()) <= 0;
        boolean startsAfter = this.getStart().compareTo(rangeToCheck.getEnd()) >= 0;

        return !(endsBefore || startsAfter);
    }

    public boolean isAdjacentTo(TemporalRange rangeToCheck) {
        if (rangeToCheck.equals(getUniversalRange())) {
            return false;
        }

        boolean endsAtStart = this.getEnd().compareTo(rangeToCheck.getStart()) == 0;
        boolean startsAtEnd = this.getStart().compareTo(rangeToCheck.getEnd()) == 0;

        return endsAtStart || startsAtEnd;
    }

    public TemporalRange merge(TemporalRange rangeToMerge) {
        if (rangeToMerge.equals(getUniversalRange())) {
            return this;
        }

        CustomTime start;
        CustomTime end;

        if (!(this.overlaps(rangeToMerge) || this.isAdjacentTo(rangeToMerge))) {
            return null; // we cannot merge two completely separate ranges with no common points
        }

        if (this.getStart().compareTo(rangeToMerge.getStart()) < 0) {
            start = this.getStart();
        } else {
            start = rangeToMerge.getStart();
        }

        if (this.getEnd().compareTo(rangeToMerge.getEnd()) > 0) {
            end = this.getEnd();
        } else {
            end = rangeToMerge.getEnd();
        }

        return new TemporalRange(start, end);
    }


    /*
     * inherited methods from Object
     */

    @Override
    public boolean equals(Object o) {
        assert o instanceof TemporalRange;

        // handle the universal range, in which _start and _end are not defined
        if (this.isUniversal()) {
            return ((TemporalRange) o).isUniversal();
        }

        TemporalRange range = (TemporalRange) o;
        return this._start.equals(range._start) && this._end.equals(range._end);
    }
}
