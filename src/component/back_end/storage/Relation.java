package component.back_end.storage;

/**
 * 
 * @author Huiyie
 *
 */

public abstract class Relation implements RelationSpec {
    
    PrimaryKey pKey_;

    @Override
    public abstract PrimaryKeySpec<?> getPrimaryKey();
    
    @Override
    public abstract void updatePrimaryKey();
}
