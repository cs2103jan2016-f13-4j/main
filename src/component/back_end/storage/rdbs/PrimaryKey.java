package component.back_end.storage.rdbs;

/**
 * 
 * @author Huiyie
 *
 */

public class PrimaryKey<T extends Comparable<T>> implements PrimaryKeyInterface<T> {

    private T value_;
    
    public PrimaryKey(T value) {
        this.value_ = value;
    }

    @Override
    public int compareTo(PrimaryKeyInterface<T> o) {
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

        // Compare two primary keys
        if (o instanceof PrimaryKey) {
            try {
                PrimaryKey<T> key = (PrimaryKey<T>) o;
                return this.getValue().equals(key.getValue());
            } catch (ClassCastException e) {
                return false;
            }
        }

        // Compare a key with a value that it holds
        if (this.getValue().getClass().equals(o.getClass())) {
            T key = (T) o;
            return this.getValue().equals(key);
        }

        return false;
    }
}
