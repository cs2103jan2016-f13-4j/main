package skeleton;

import java.util.List;

/**
 * @@author Mai Anh Vu
 */
public interface StorageSpec<T> {

    void initialise();

    int save(T item);

    T remove(int index);

    T get(int index);

    void undelete(int index);

    List<T> getAll();

    void shutdown();
}
