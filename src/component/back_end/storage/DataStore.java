package component.back_end.storage;

import java.util.List;
import java.util.function.Function;

import entity.RelationDescriptor;

/**
 * The Data Store is the gatekeeper for the actual task data stored on disk.
 * It exposes functionality to create, read, update and delete the task data.
 * 
 * @author Huiyie
 *
 */

public class DataStore implements DataStoreSpec {


    @Override
    public void add(RelationInterface tuple) {

    }

    @Override
    public RelationInterface remove(RelationInterface tuple) {
        return null;
    }

    @Override
    public RelationInterface remove(Class<? extends RelationInterface> relationClass, PrimaryKeyInterface primaryKey) {
        return null;
    }

    @Override
    public List<RelationInterface> getAll(Class<? extends RelationInterface> relationClass, RelationDescriptor descriptor) {
        return null;
    }

    @Override
    public List<RelationInterface> map(RelationDescriptor descriptor, Function<List<RelationInterface>, Void> modifierFunction) {
        return null;
    }
}
