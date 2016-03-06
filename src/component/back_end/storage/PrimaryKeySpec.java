package component.back_end.storage;

/**
 * Created by maianhvu on 6/3/16.
 */
public interface PrimaryKeySpec extends Comparable<PrimaryKeySpec> {
    Comparable getValue();
    void setValue(Comparable newKeyValue);
}
