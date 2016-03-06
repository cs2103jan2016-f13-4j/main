package component.back_end.storage;

import entity.RelationDescriptor;

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
public interface DataStoreSpec {
    
    /**
     * Adds a tuple into the data store
     * 
     * Note that the tuple object must be fully defined
     * (for example for Task, by the Task Scheduler if necessary)
     * before adding it. This will be checked by assertions.
     *
     * @param tuple The tuple to be added
     */
    void add(RelationSpec tuple);

    /**
     * Removes a tuple from the data store
     * @param tuple
     * @return
     */
    RelationSpec remove(RelationSpec tuple);

    /**
     * Removes a tuple knowing its relation class and its primary key
     * @param relationClass a class that extends the Relation
     * @param primaryKey a primary key object
     * @return
     */
    RelationSpec remove(Class<? extends RelationSpec> relationClass, PrimaryKeySpec primaryKey);
    
    /**
     * Get a list of tasks that match the task descriptor specified (see {@link RelationDescriptor})
     * @param relationClass the Class of the relation
     * @return
     */
    List<RelationSpec> getAll(Class<? extends RelationSpec> relationClass);
    
    /**
     * Get a list of tasks that match the task descriptor specified (see {@link RelationDescriptor})
     * and then applies the modifier function to the list before returning it
     * 
     * This may be used to, for instance, sort the Task list by some metric before returning it.
     * 
     * @param descriptor
     * @param modifierFunction
     * @return
     */
    List<RelationSpec> map(RelationDescriptor descriptor, Function<List<RelationSpec>, Void> modifierFunction);
}
