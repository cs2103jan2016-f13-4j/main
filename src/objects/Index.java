package objects;

/**
 * Created by maianhvu on 5/3/16.
 */
public class Index implements Comparable<Index> {

    private final long value_;

    public Index(long value) {
        this.value_ = value;
    }

    public long getValue() {
        return this.value_;
    }

    @Override
    public int compareTo(Index another) {
        long difference = this.value_ - another.value_;

        // Equal
        if (difference == 0) {
            return 0;
        }

        // This is smaller than another
        if (difference < 0) {
            return -1;
        }

        // This is greater than another
        return 1;
    }
}
