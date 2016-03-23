package skeleton;

import java.util.List;

import exception.PrimaryKeyNotFoundException;

public interface CollectionSpec<T> {

    int add(T item);

    boolean edit(int index, T item);

    T remove(int index);

    T get(int index);

    List<T> getAll();

    String getStoragePath();
}
