package back_end.storage.base;

/**
 * Created by maianhvu on 5/3/16.
 */
public abstract class SerialIdRelation extends Relation {

    private Index id_;

    @Override
    public Index getPrimaryKey() {
        return this.id_;
    }

    @Override
    public void setPrimaryKey(PrimaryKey key) {
        this.id_ = (Index) key;
    }

}
