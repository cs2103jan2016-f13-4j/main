package component.front_end;

import component.back_end.storage.PrimaryKeySpec;
import component.back_end.storage.RelationSpec;
import entity.Index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maianhvu on 6/3/16.
 */
public class VisualIndexMapper implements VisualIndexMapperSpec {

    /**
     * Properties
     */
    private ArrayList<PrimaryKeySpec<?>> forwardMap_;
    private HashMap<PrimaryKeySpec<?>, Integer> backwardMap_;

    /**
     * Constructs a visual index mapper based on a list of index
     */
    public VisualIndexMapper(List<RelationSpec> tupleList) {
        this.forwardMap_ = new ArrayList<>();
        this.backwardMap_ = new HashMap<>();

        // Populate keys
        for (RelationSpec tuple : tupleList)  {
            PrimaryKeySpec<?> primaryKey = tuple.getPrimaryKey();
            this.forwardMap_.add(primaryKey);

            // Get the latest added primary key
            int latestIndex = this.forwardMap_.size() - 1;

            // Translate the latestIndex into visual index
            int visualIndex = getVisualIdFromArrayListId(latestIndex);

            // Map the visual index back to the primary key
            this.backwardMap_.put(primaryKey, visualIndex);
        }
    }

    @Override
    public int translateRawToVisual(PrimaryKeySpec<?> rawPrimaryKey) {
        assert(rawPrimaryKey != null);
        return this.backwardMap_.get(rawPrimaryKey);
    }

    @Override
    public PrimaryKeySpec<?> translateVisualToRaw(int visualId) {
        int arrayListId = getArrayListIdFromVisualId(visualId);
        try {
            return this.forwardMap_.get(arrayListId);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private static int getVisualIdFromArrayListId(int arrayListId) {
        // Visual IDs start from 1, so to get the visual index we just
        // need to get the array list index plus one
        return arrayListId + 1;
    }

    private static int getArrayListIdFromVisualId(int visualId) {
        // Array list IDs start from 0, so to get the array list index we just
        // need to get the visual index minus one
        return visualId - 1;
    }
}
