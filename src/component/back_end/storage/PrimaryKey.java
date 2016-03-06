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

}
