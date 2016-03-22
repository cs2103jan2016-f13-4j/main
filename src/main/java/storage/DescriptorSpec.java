package storage;

import skeleton.CollectionSpec;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DescriptorSpec<T> {

    public abstract boolean matches(T object);

    public List<T> apply(CollectionSpec<T> collection) {
        List<T> allItems = collection.getAll();
        return allItems.stream().filter(this::matches).collect(Collectors.toList());
    }
}
