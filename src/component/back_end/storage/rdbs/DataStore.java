package component.back_end.storage.rdbs;

import exception.back_end.storage.PrimaryKeyAlreadyExistsException;

import java.util.*;
import java.util.function.Function;

/**
 * The Data Store is the gatekeeper for the actual task data stored on disk.
 * It exposes functionality to create, read, update and delete the task data.
 * 
 * @author Huiyie
 *
 */

public class DataStore extends DataStoreSpec {

    /**
     * Properties
     */
    private final HashMap<Class<? extends RelationInterface>, TreeMap<PrimaryKeyInterface<?>, RelationInterface>> storageMap_;

    /**
     * Constructs an empty data storage
     */
    public DataStore() {
        // Initialize HashMap
        this.storageMap_ = new HashMap<>();
    }

    @Override
    public void add(RelationInterface tuple) throws PrimaryKeyAlreadyExistsException {
        assert(this.hasCollectionFor(tuple.getClass()));
        assert(tuple.getPrimaryKey() != null);

        // Get collection of values from the tuple's class
        TreeMap<PrimaryKeyInterface<?>, RelationInterface> collection =
                this.getCollectionFor(tuple.getClass());

        // Try to find the primary key inside database
        PrimaryKeyInterface primaryKey = tuple.getPrimaryKey();
        if (collection.containsKey(primaryKey)) {
            throw new PrimaryKeyAlreadyExistsException(primaryKey, tuple.getClass());
        }

        collection.put(primaryKey, tuple);
    }

    private TreeMap<PrimaryKeyInterface<?>,RelationInterface> getCollectionFor(
            Class<? extends RelationInterface> aClass) {
        return this.storageMap_.get(aClass);
    }

    /**
     * Creates and adds new collection
     * 
     * @param relationClass
     */
    public void createCollectionFor(Class<? extends RelationInterface> relationClass) {
        // Create new collection
        TreeMap<PrimaryKeyInterface<?>, RelationInterface> collection = new TreeMap<>();
        
        // Add storage tree for this relation to HashMap
        this.storageMap_.put(relationClass, collection);
    }

    private boolean hasCollectionFor(Class<? extends RelationInterface> relationClass) {
        return this.getCollectionFor(relationClass) != null;
    }

    @Override
    public <T extends RelationInterface> T remove(T tuple) {
        assert(tuple != null);
        return this.remove((Class<T>) tuple.getClass(), tuple.getPrimaryKey());
    }

    @Override
    public <T extends RelationInterface> T remove(Class<T> relationClass, PrimaryKeyInterface primaryKey) {
        assert(relationClass != null);
        assert(primaryKey != null);
        assert(this.hasCollectionFor(relationClass));

        TreeMap<PrimaryKeyInterface<?>, RelationInterface> collection =
                this.getCollectionFor(relationClass);

        try {
            // Primary key does not exists in database, return null
            if (!collection.containsKey(primaryKey)) {
                return null;
            }

            return (T) collection.remove(primaryKey);
        } catch (ClassCastException e) {
            // Normally this is returned when there is a primary key mismatch
            return null;
        }
    }

    @Override
    public <T extends RelationInterface> List<T> getAll(Class<T> relationClass,
            RelationConstraint descriptor) {
        assert(relationClass != null);
        assert(this.hasCollectionFor(relationClass));

        TreeMap<PrimaryKeyInterface<?>, RelationInterface> collection =
                this.getCollectionFor(relationClass);

        List<T> tuples = new ArrayList<>((Collection<T>) collection.values());

        if (descriptor != null) {
            for (Iterator<T> it = tuples.iterator(); it.hasNext(); ) {
                RelationInterface tuple = it.next();

                // Pass it through the function
                if (!descriptor.matches(tuple)) {
                    it.remove();
                }
            }
        }

        return tuples;
    }

    @Override
    public <T extends RelationInterface> T get(Class<T> relationClass, PrimaryKeyInterface primaryKey) {
        assert(relationClass != null);
        assert(primaryKey != null);
        assert(this.hasCollectionFor(relationClass));

        TreeMap<PrimaryKeyInterface<?>, RelationInterface> collection =
                this.getCollectionFor(relationClass);

        try {
            return (T) collection.get(primaryKey);
        } catch (ClassCastException e) {
            // Normally this is returned when there is a primary key mismatch
            return null;
        }
    }

    @Override
    public List<RelationInterface> map(RelationConstraint descriptor,
            Function<List<RelationInterface>, Void> modifierFunction) {
        // TODO: Auto-generated method stub
        return null;
    }

}
