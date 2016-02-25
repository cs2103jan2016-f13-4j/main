package storage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by maianhvu on 25/2/16.
 */
public abstract class BaseModel implements YamlSerializable {

    private LinkedHashMap<String, Field> fieldsMap_;
    private ArrayList<BaseInstance> dataList_;

    public BaseModel() {
        this.fieldsMap_ = new LinkedHashMap<>();
        this.dataList_ = new ArrayList<>();
    }

    public void defineField(String name, Field )
}
