package component.back_end.storage;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * The Data Store is the gatekeeper for the actual task data stored on disk.
 * It exposes functionality to create, read, update and delete the task data.
 * 
 * @author Huiyie
 *
 */

public class DataStore extends DataStoreSpec {
    
    private HashMap<Class<? extends RelationInterface>, TreeMap<PrimaryKeyInterface<?>, RelationInterface>> storageMap_;

    // Constructor
    public DataStore() {
        // initialize HashMap
        this.storageMap_ = new HashMap<>();
    }

    @Override
    public void add(RelationInterface tuple) {
        Class<? extends RelationInterface> relationClass = (Class<? extends RelationInterface>) tuple.getClass();
        
        // check if the function is an add or edit
        if (this.isAddNewEntry(relationClass)) {
            this.addStorageTree(relationClass);
        }
        
        // find the storage tree with the particular class as key
        TreeMap<PrimaryKeyInterface<?>, RelationInterface> storageTree = getStorageFor(relationClass);

        // add to TreeMap, or overwrites when this is an edit function
        storageTree.put(tuple.getPrimaryKey(), tuple);
        
    }

    /**
     * Checks if the function is an add or edit.
     * If the storage tree for this particular relation already exists, then this is an edit.
     * Else, the function is an add.
     * 
     * @param relationClass
     * @return
     */
    private boolean isAddNewEntry(Class<? extends RelationInterface> relationClass) {
        // if storage tree for this relation already exists
        if (this.storageMap_.containsKey(relationClass)) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Creates and adds new storage tree.
     * 
     * @param relationClass
     */
    private void addStorageTree(Class<? extends RelationInterface> relationClass) {
        // create new storage tree
        TreeMap<PrimaryKeyInterface<?>, RelationInterface> storageTree = new TreeMap<>();
        
        // add storage tree for this relation to HashMap
        this.storageMap_.put(relationClass, storageTree);       
    }

    private TreeMap<PrimaryKeyInterface<?>, RelationInterface> getStorageFor(
            Class<? extends RelationInterface> relationClass) {
        
        return this.storageMap_.get(relationClass);
        
    }

    @Override
    public RelationInterface remove(RelationInterface tuple) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RelationInterface remove(Class<? extends RelationInterface> relationClass, PrimaryKeyInterface primaryKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<RelationInterface> getAll(Class<? extends RelationInterface> relationClass,
            RelationConstraint descriptor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<RelationInterface> map(RelationConstraint descriptor,
            Function<List<RelationInterface>, Void> modifierFunction) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public HashMap<Class<? extends RelationInterface>, TreeMap<PrimaryKeyInterface<?>, RelationInterface>> getStorageMap() {
        return storageMap_;
    }

    
    public void setStorageMap(
            HashMap<Class<? extends RelationInterface>, TreeMap<PrimaryKeyInterface<?>, RelationInterface>> storageMap) {
        storageMap_ = storageMap;
    }

}
