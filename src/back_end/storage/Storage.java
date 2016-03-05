package back_end.storage;

import back_end.storage.base.Index;
import back_end.storage.base.PrimaryKey;
import back_end.storage.base.Relation;
import back_end.storage.base.SerialIdRelation;

import java.util.*;

/**
 * Created by maianhvu on 5/3/16.
 */
public class Storage {
    private HashMap<Class<? extends Relation>, TreeMap<PrimaryKey, Relation>> storageMap_;

    public Storage() {
        this.storageMap_ = new HashMap<>();
    }

    public void initializeStorageFor(Class<? extends Relation> relation) {
        // Ignore if a storage for this relation already existed
        if (this.storageMap_.containsKey(relation)) {
            return;
        }

        TreeMap<PrimaryKey, Relation> storageTree = new TreeMap<>();
        this.storageMap_.put(relation, storageTree);
    }

    public PrimaryKey save(Relation record) throws RelationNotFoundException, PrimaryKeyMissingException {
        // Find the storage tree from record class first
        Class<? extends Relation> relationClass = record.getClass();
        TreeMap<PrimaryKey, Relation> storageTree = this.storageMap_.get(relationClass);

        // Storage tree does not exist, throw exception
        if (storageTree == null) {
            throw new RelationNotFoundException();
        }

        // Define whether the action is to add or to save
        // If the primary key is null, then we are trying to create a new record
        // with an automatically generated primary key
        if (record.getPrimaryKey() == null) {
            // However, this automatically created primary key is only valid for
            // SerialIdRelation, which the next primary key is predictable.
            // Throw PrimaryKeyMissingException if record is not under a SerialIdRelation
            if (!(record instanceof SerialIdRelation)) {
                throw new PrimaryKeyMissingException();
            }

            // Otherwise, edit the record. Find the
            // newIndex index to insert the record.
            Index newIndex = null;

            if (storageTree.isEmpty()) {
                // Case 1: Tree empty, first index is 0
                newIndex = new Index((long) 0);
            } else {
                // Case 2: Tree not empty, get element with largest index
                // and get the next index
                SerialIdRelation largestIndexRecord =
                        (SerialIdRelation) storageTree.lastEntry().getValue();
                newIndex = largestIndexRecord.getPrimaryKey().nextIndex();
            }

            // Set primary key for the record and let it gracefully downgrades
            // to a `edit` call instead
            record.setPrimaryKey(newIndex);
        }

        // Put the record inside the tree
        storageTree.put(record.getPrimaryKey(), record);

        // Return the primary key of the saved record
        return record.getPrimaryKey();
    }

    public <T extends Relation> T get(Class<T> relation, PrimaryKey primaryKey) throws RelationNotFoundException {
        // Search for the storage tree reference from the relation class first
        // If it does not exist, throw the exception
        TreeMap<PrimaryKey, Relation> storageTree = this.storageMap_.get(relation);

        if (storageTree == null) {
           throw new RelationNotFoundException();
        }

        // Search the storage tree for the primary key and return it
        // Default behaviour for TreeMap is that if key not found, return null
        return (T) storageTree.get(primaryKey);
    }

    public <T extends Relation> List<T> getAll(Class<T> relation) throws RelationNotFoundException {
        // Search for storage tree reference from the relation class first
        // If it does not exist, throw the exception
        TreeMap<PrimaryKey, Relation> storageTree = this.storageMap_.get(relation);

        if (storageTree == null) {
            throw new RelationNotFoundException();
        }

        // Add all elements of the storage tree into an array list and returns it
        ArrayList<T> allRecords = new ArrayList<T>((Collection<T>) storageTree.values());
        return allRecords;
    }


    public class RelationNotFoundException extends Exception {}
    public class PrimaryKeyMissingException extends Exception {}
}
