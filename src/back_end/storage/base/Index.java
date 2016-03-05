package back_end.storage.base;

/**
 * Created by maianhvu on 5/3/16.
 */
public class Index extends PrimaryKey {

    public Index(Long value) {
        super(value);
    }

    public Index nextIndex() {
        return new Index((Long) this.getValue() + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;

        if (o instanceof Index) {
            Index id = (Index) o;
            return this.getValue().equals(id.getValue());
        } else if (o instanceof Long) {
            Long longId = (Long) o;
            return this.getValue().equals(longId);
        } else if (o instanceof Integer) {
            Long longId = new Long((Integer) o);
            return this.getValue().equals(longId);
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getValue().toString();
    }
}
