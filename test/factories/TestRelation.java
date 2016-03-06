package factories;

import component.back_end.storage.PrimaryKeyInterface;
import component.back_end.storage.RelationInterface;

/**
 * Created by maianhvu on 6/3/16.
 */
public class TestRelation implements RelationInterface {

    private PrimaryKeyInterface<String> primaryKey_;
    private final String name_;

    public TestRelation(String name) {
        this.name_ = name;
        this.updatePrimaryKey();
    }

    @Override
    public PrimaryKeyInterface<String> getPrimaryKey() {
        return this.primaryKey_;
    }

    public <T> void setPrimaryKey(PrimaryKeyInterface<T> newPrimaryKey) {
        this.primaryKey_ = (PrimaryKeyInterface<String>) newPrimaryKey;
    }

    @Override
    public void updatePrimaryKey() {
        this.primaryKey_ = new TestPrimaryKey(this.name_);
    }

}
