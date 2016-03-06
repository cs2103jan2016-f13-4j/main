package component.back_end.storage;

import java.util.List;
import java.util.function.Function;

/**
 * The Data Store is the gatekeeper for the actual task data stored on disk.
 * It exposes functionality to create, read, update and delete the task data.
 * 
 * @author Huiyie
 *
 */

public class DataStore extends DataStoreSpec {


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
    public List<RelationInterface> getAll(Class<? extends RelationInterface> relationClass, RelationConstraint descriptor) {
        return null;
    }

    @Override
    public List<RelationInterface> map(RelationConstraint descriptor, Function<List<RelationInterface>, Void> modifierFunction) {
        return null;
    }
}
