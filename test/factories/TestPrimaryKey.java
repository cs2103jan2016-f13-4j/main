package factories;

import component.back_end.storage.PrimaryKeySpec;
import junit.framework.Test;

/**
 * Created by maianhvu on 6/3/16.
 */
public class TestPrimaryKey implements PrimaryKeySpec<String> {

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
    public int compareTo(PrimaryKeySpec<String> o) {
        return this.getValue().compareTo(o.getValue());
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof TestPrimaryKey)) return false;

        TestPrimaryKey key = (TestPrimaryKey) o;
        return this.getValue().equals(key.getValue());
    }
}