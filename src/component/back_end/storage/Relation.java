package component.back_end.storage;

/**
 * 
 * @author Huiyie
 *
 */

public abstract class Relation implements RelationSpec {
    
    PrimaryKey pKey_;

    @Override
    public abstract <T> PrimaryKeySpec<T> getPrimaryKey();

    @Override
    public abstract <T> void setPrimaryKey(PrimaryKeySpec<T> newPrimaryKey);

}
