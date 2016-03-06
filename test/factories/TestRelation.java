package factories;

import component.back_end.storage.PrimaryKeySpec;
import component.back_end.storage.RelationSpec;

/**
 * Created by maianhvu on 6/3/16.
 */
public class TestRelation implements RelationSpec {

    private PrimaryKeySpec<String> primaryKey_;
    private final String name_;

    public TestRelation(String name) {
        this.name_ = name;
        this.primaryKey_ = new TestPrimaryKey(name);
    }

    @Override
    public PrimaryKeySpec<String> getPrimaryKey() {
        return this.primaryKey_;
    }

    @Override
    public <T> void setPrimaryKey(PrimaryKeySpec<T> newPrimaryKey) {
        this.primaryKey_ = (PrimaryKeySpec<String>) newPrimaryKey;
    }
}
