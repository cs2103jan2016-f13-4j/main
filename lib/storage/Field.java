package storage;

import java.util.Date;

/**
 * Created by maianhvu on 25/2/16.
 */
public class Field {

    public enum Type {
        INTEGER(Integer.class),
        REAL(Double.class),
        STRING(String.class),
        DATETIME(Date.class);

        final Class<?> className;
        Type(Class<?> cls) {
            className = cls;
        }
    }

    private Type type_;
    private String name_;

    public Field(String name, Type type) {
        this.name_ = name;
        this.type_ = type;
    }

    public Class<?> getInstanceClass() {
        return this.type_.className;
    }

    public Type getType() {
        return this.type_;
    }

    public String getName() {
        return this.name_;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;

        if (o instanceof Field) {
            Field f = (Field) o;
            return this.name_.equals(f.name_);
        } else if (o instanceof String) {
            return this.name_.equals((String) o);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.name_.hashCode();
    }
}
