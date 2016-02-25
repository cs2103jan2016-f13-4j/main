package storage;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by maianhvu on 25/2/16.
 */
public abstract class BaseInstance {
    private BaseModel parentModel_;
    private LinkedHashMap<Field, Object> valuesMap_;

    public BaseInstance(BaseModel parent) {
        this.valuesMap_ = new LinkedHashMap<>();
        this.parentModel_ = parent;
    }

    public void setValue(String fieldName, Object value) {
        if (fieldName == null) return;
        Field field = this.parentModel_.getFieldByName(fieldName);
        setValue(field, value);
    }

    public void setValue(Field field, Object value) {
        if (field == null) return;
        this.valuesMap_.put(field, value);
    }

    public <T> T getValue(String fieldName) {
        if (fieldName == null) return null;
        Field field = this.parentModel_.getFieldByName(fieldName);
        return getValue(field);
    }

    public <T> T getValue(Field field) {
        if (field == null) return null;
        return (T) this.valuesMap_.get(field);
    }
}
