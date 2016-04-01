package skeleton;

import java.util.List;

import exception.PrimaryKeyNotFoundException;

/**
 * @@author Mai Anh Vu
 */
public interface CollectionSpec<T> {

    int save(T item);

    T remove(int index);

    T get(int index);

    void undelete(int index);

    List<T> getAll();
}
