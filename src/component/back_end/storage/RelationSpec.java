package component.back_end.storage;

/**
 * Created by maianhvu on 6/3/16.
 */
public interface RelationSpec {
    <T> PrimaryKeySpec<T> getPrimaryKey();
    <T> void setPrimaryKey(PrimaryKeySpec<T> newPrimaryKey);
}
