package back_end.storage.base;

/**
 * Created by maianhvu on 5/3/16.
 */
public abstract class Relation implements Comparable<Relation> {

    public abstract PrimaryKey getPrimaryKey();

    public abstract void setPrimaryKey(PrimaryKey key);

    @Override
    public int compareTo(Relation another) {
        return this.getPrimaryKey().compareTo(another.getPrimaryKey());
    }
}
