package skeleton;

import exception.PrimaryKeyNotFoundException;
import storage.DescriptorSpec;

import java.util.List;

public interface CollectionSpec<T> {

    int add(T item);

    boolean edit(int index, T item);

    T remove(int index);

    T get(int index) throws PrimaryKeyNotFoundException;

    List<T> getAll();

    List<T> getAll(DescriptorSpec<T> descriptor);

    String getStoragePath();
}
