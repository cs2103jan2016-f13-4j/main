package objects;

/**
 * Created by maianhvu on 5/3/16.
 */
public class Index implements Comparable<Index> {

    private final Long value_;

    public Index(Long value) {
        this.value_ = value;
    }

    public Long getValue() {
        return this.value_;
    }

    @Override
    public int compareTo(Index another) {
        return this.value_.compareTo(another.value_);
    }

    @Override
    public int hashCode() {
        return this.value_.hashCode();
    }
}
