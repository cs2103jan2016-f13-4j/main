package component.back_end.storage;

/**
 * Created by maianhvu on 6/3/16.
 */
public interface PrimaryKeySpec<T> extends Comparable<PrimaryKeySpec<T>> {
    T getValue();
    void setValue(T newKeyValue);
}
