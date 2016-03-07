package factories;

import component.back_end.storage.PrimaryKeyInterface;

/**
 * Created by maianhvu on 6/3/16.
 */
public class TestPrimaryKey implements PrimaryKeyInterface<String> {

    private String name_;

    public TestPrimaryKey(String name) {
        this.name_ = name;
    }

    @Override
    public String getValue() {
        return this.name_;
    }

    @Override
    public void setValue(String newKeyValue) {
        this.name_ = newKeyValue;
    }

    @Override
    public int compareTo(PrimaryKeyInterface<String> o) {
        return this.getValue().compareTo(o.getValue());
    }
}
