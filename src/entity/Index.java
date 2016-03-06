package entity;


import component.back_end.storage.PrimaryKeySpec;
import component.front_end.VisualIndexMapperSpec;

/**
 * Index is the type of IDs used in both ID systems (see {@link VisualIndexMapperSpec}).
 * We do this to ensure we can change the actual type of IDs without any repercussions.
 * 
 * created by thenaesh on Mar 6, 2016
 *
 */
public class Index implements PrimaryKeySpec {
    private Long rawIndex_;
   
    /**
     * Constructs an Index object with a rawIndex
     * @param rawIndex a long value
     */
    public Index(Long rawIndex) {
        this.rawIndex_ = rawIndex;
    }
    
    @Override
    public boolean equals(Object o) {
        Long rawIndexOther = ((Index) o).rawIndex_;
        return rawIndex_.equals(rawIndexOther);
    }

    @Override
    public int hashCode() {
        return this.rawIndex_.hashCode();
    }

    @Override
    public Comparable getValue() {
        return null;
    }

    @Override
    public void setValue(Comparable newKeyValue) {

    }

    @Override
    public int compareTo(PrimaryKeySpec o) {
        return 0;
    }
}
