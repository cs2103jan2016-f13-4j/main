package objects;

/**
 * Created by maianhvu on 5/3/16.
 */
public abstract class SerialIdRelation extends Relation {

    private Index id_;

    @Override
    public Comparable getPrimaryKey() {
        return this.id_;
    }

}
