package entity;

import component.back_end.storage.PrimaryKeyInterface;
import component.front_end.VisualIndexMapperSpec;

/**
 * Index is the type of IDs used in both ID systems (see {@link VisualIndexMapperSpec}).
 * We do this to ensure we can change the actual type of IDs without any repercussions.
 * 
 * created by thenaesh on Mar 6, 2016
 *
 */
public class Index implements PrimaryKeyInterface<Index> {
    private Long rawIndex_;

    @Override
    public Index getValue() {
        return null;
    }

    @Override
    public void setValue(Index newKeyValue) {

    }

    @Override
    public int compareTo(PrimaryKeyInterface<Index> o) {
        return 0;
    }
}
