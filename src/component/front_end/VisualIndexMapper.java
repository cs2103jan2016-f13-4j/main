package component.front_end;

import entity.Index;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by maianhvu on 6/3/16.
 */
public class VisualIndexMapper implements VisualIndexMapperSpec {

    /**
     * Properties
     */
    private ArrayList<Index> forwardMap_;
    private HashMap<Index, Integer> backwardMap_;

    /**
     * Constructs a visual index mapper based on a list of index
     */
    public VisualIndexMapper() {
        this.forwardMap_ = new ArrayList<>();
        this.backwardMap_ = new HashMap<>();
    }

    @Override
    public Integer translateRawToVisual(Index rawId) {
        return null;
    }

    @Override
    public Index translateVisualToRaw(Integer visualId) {
        return null;
    }

    private static Integer getVisualIdFromArrayListId(Integer arrayListId) {
        assert(arrayListId != null);

        // Visual IDs start from 1, so to get the visual index we just
        // need to get the array list index plus one
        return arrayListId + 1;
    }

    private static Integer getArrayListIdFromVisualId(Integer visualId) {
        assert(visualId != null);

        // Array list IDs start from 0, so to get the array list index we just
        // need to get the visual index minus one
        return visualId - 1;
    }
}
