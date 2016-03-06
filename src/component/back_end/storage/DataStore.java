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
    public void add(RelationSpec tuple) {

    }

    @Override
    public RelationSpec remove(RelationSpec tuple) {
        return null;
    }

    @Override
    public RelationSpec remove(Class<? extends RelationSpec> relationClass, PrimaryKeySpec primaryKey) {
        return null;
    }

    @Override
    public List<RelationSpec> getAll(Class<? extends RelationSpec> relationClass) {
        return null;
    }

    @Override
    public List<RelationSpec> map(RelationDescriptor descriptor, Function<List<RelationSpec>, Void> modifierFunction) {
        return null;
    }
}
