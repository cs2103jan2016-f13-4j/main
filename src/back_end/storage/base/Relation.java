package back_end.storage.base;

/**
 * Created by maianhvu on 5/3/16.
 */
public abstract class Relation {

    public abstract PrimaryKey getPrimaryKey();
    public abstract void setPrimaryKey(PrimaryKey key);
}
