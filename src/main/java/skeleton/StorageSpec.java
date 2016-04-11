package skeleton;

import java.util.List;
import java.util.Set;

/**
 * @@author A0127046L
 */
public interface StorageSpec<T> {

    void initialise();

    int save(T item);

    T remove(int index);

    T get(int index);

    void undelete(int index);

    Set<Integer> getNonDeletedTasks();

    List<T> getAll();

    void shutdown();
}
