package component.back_end.storage;

import java.util.List;
import java.util.function.Function;

/**
 * The Data Store is the gatekeeper for the actual task data stored on disk.
 * It exposes functionality to create, read, update and delete the task data.
 * 
 * Rank: Staff Sergeant (reports to CPT Decision Engine)
 *
 * created by thenaesh on Mar 5, 2016
 *
 */
public abstract class DataStoreSpec {
    
    /**
     * Adds a tuple into the data store
     * 
     * Note that the tuple object must be fully defined
     * (for example for Task, by the Task Scheduler if necessary)
     * before adding it. This will be checked by assertions.
     *
     * @param tuple The tuple to be added
     */
    public abstract void add(RelationInterface tuple);

    /**
     * Removes a tuple from the data store
     * @param tuple
     * @return
     */
    public abstract RelationInterface remove(RelationInterface tuple);

    /**
     * Removes a tuple knowing its relation class and its primary key
     * @param relationClass a class that extends the Relation
     * @param primaryKey a primary key object
     * @return
     */
    public abstract RelationInterface remove(Class<? extends RelationInterface> relationClass, PrimaryKeyInterface primaryKey);

    /**
     * Get a list of tasks that match the task descriptor specified (see {@link RelationConstraint})
     * @param relationClass the Class of the relation
     * @param descriptor the task descriptor
     * @return
     */
    public abstract List<RelationInterface> getAll(Class<? extends RelationInterface> relationClass, RelationConstraint descriptor);

    /**
     * Get a list of tasks that match the task descriptor specified (see {@link RelationConstraint})
     * and then applies the modifier function to the list before returning it
     * 
     * This may be used to, for instance, sort the Task list by some metric before returning it.
     * 
     * @param descriptor
     * @param modifierFunction
     * @return
     */
    public abstract List<RelationInterface> map(RelationConstraint descriptor, Function<List<RelationInterface>, Void> modifierFunction);
}
