package back_end.storage.base;

/**
 * Created by maianhvu on 5/3/16.
 */
public class Index extends PrimaryKey {

    public Index(Long value) {
        super(value);
    }

    public Index nextIndex() {
        return new Index((Long) this.getValue() + 1);
    }

}
