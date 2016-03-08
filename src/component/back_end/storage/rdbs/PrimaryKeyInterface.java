package component.back_end.storage.rdbs;

/**
 * Created by maianhvu on 6/3/16.
 */
public interface PrimaryKeyInterface<T> extends Comparable<PrimaryKeyInterface<T>> {
    T getValue();
    void setValue(T newKeyValue);

    int hashCode();
    boolean equals(Object o);
}