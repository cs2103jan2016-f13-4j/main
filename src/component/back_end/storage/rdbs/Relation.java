package component.back_end.storage.rdbs;

/**
 *
 * @author Huiyie
 *
 */

public abstract class Relation implements RelationInterface {

    PrimaryKey pKey_;

    @Override
    public abstract PrimaryKeyInterface<?> getPrimaryKey();

    @Override
    public abstract void updatePrimaryKey();
}
