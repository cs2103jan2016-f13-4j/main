package component.back_end.storage;

/**
 * 
 * @author Huiyie
 *
 */

public class PrimaryKey<T extends Comparable<T>> implements PrimaryKeySpec<T> {

    private T value_;
    
    public PrimaryKey(T value) {
        this.value_ = value;
    }

    @Override
    public int compareTo(PrimaryKeySpec<T> o) {
        return this.getValue().compareTo(o.getValue());
    }

    @Override
    public T getValue() {
        return value_;
    }

    @Override
    public void setValue(T newKeyValue) {
        this.value_ = newKeyValue;
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof PrimaryKey)) return false;

        PrimaryKey<T> key = (PrimaryKey<T>) o;
        return this.getValue().equals(key.getValue());
    }
}
