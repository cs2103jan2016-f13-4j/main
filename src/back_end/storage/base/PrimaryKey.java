package back_end.storage.base;

/**
 * Created by maianhvu on 5/3/16.
 */
public abstract class PrimaryKey implements Comparable<PrimaryKey> {

    protected Comparable key_;

    public PrimaryKey(Comparable key) {
        this.key_ = key;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof PrimaryKey)) return false;

        PrimaryKey k = (PrimaryKey) o;
        return this.key_.equals(k.key_);
    }

    @Override
    public int compareTo(PrimaryKey another) {
        return this.key_.compareTo(another.key_);
    }

    @Override
    public int hashCode() {
        return this.key_.hashCode();
    }

    public Comparable getValue() {
        return this.key_;
    }

    public void setKey(Comparable value) {
        this.key_ = value;
    }

}
