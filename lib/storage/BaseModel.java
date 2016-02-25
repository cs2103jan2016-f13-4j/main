package storage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by maianhvu on 25/2/16.
 */
public abstract class BaseModel implements YamlSerializable {

    private LinkedHashMap<String, Field> fieldsMap_;
    private ArrayList<BaseInstance> dataList_;
    private Constructor<?> instanceConstructor_;

    public BaseModel(Class<? extends BaseInstance> instanceClass) {
        this.fieldsMap_ = new LinkedHashMap<>();
        this.dataList_ = new ArrayList<>();

        try {
            this.instanceConstructor_ = instanceClass.getConstructor(BaseModel.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void defineField(String name, Field.Type fieldType) throws Error {
        if (this.fieldsMap_.containsKey(name)) {
            throw new Error("A field with the same name already existed");
        }

        Field field = new Field(name, fieldType);
        this.fieldsMap_.put(name, field);
    }

    public Field getFieldByName(String name) {
        if (name == null) return null;
        return this.fieldsMap_.get(name);
    }

    public List<Field> getFields() {
        return new ArrayList<Field>(this.fieldsMap_.values());
    }

    public <T extends BaseInstance> T create() {
        try {
            return (T) this.instanceConstructor_.newInstance(this);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
