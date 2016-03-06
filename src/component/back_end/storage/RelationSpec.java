package component.back_end.storage;

/**
 * Created by maianhvu on 6/3/16.
 */
public interface RelationSpec {
    PrimaryKeySpec getPrimaryKey();
    void setPrimaryKey(PrimaryKeySpec newPrimaryKey);
}
