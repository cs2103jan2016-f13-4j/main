package component.front_end;

import component.back_end.storage.PrimaryKeyInterface;
import component.back_end.storage.RelationInterface;
import component.front_end.ui.core.VisualTuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maianhvu on 6/3/16.
 */
public class VisualIndexMapper extends VisualIndexMapperSpec {

    /**
     * Properties
     */
    private ArrayList<PrimaryKeyInterface<?>> forwardMap_;
    private HashMap<PrimaryKeyInterface<?>, Integer> backwardMap_;
    private ArrayList<VisualTuple<? extends RelationInterface>> visualTupleList_;

    /**
     * Constructs a visual index mapper based on a list of index
     */
    public VisualIndexMapper(List<RelationInterface> tupleList) {
        this.forwardMap_ = new ArrayList<>();
        this.backwardMap_ = new HashMap<>();

        // Populate keys
        for (RelationInterface tuple : tupleList)  {
            PrimaryKeyInterface<?> primaryKey = tuple.getPrimaryKey();
            this.forwardMap_.add(primaryKey);

            // Get the latest added primary key
            int latestIndex = this.forwardMap_.size() - 1;

            // Translate the latestIndex into visual index
            int visualIndex = getVisualIdFromArrayListId(latestIndex);

            // Map the visual index back to the primary key
            this.backwardMap_.put(primaryKey, visualIndex);

            // Also add the tuple to visual tuple list
            VisualTuple<? extends RelationInterface> visualTuple = new VisualTuple<>(
                    visualIndex,
                    tuple
            );
            this.visualTupleList_.add(visualTuple);
        }
    }

    @Override
    public int translateRawToVisual(PrimaryKeyInterface<?> rawPrimaryKey) {
        assert(rawPrimaryKey != null);
        return this.backwardMap_.get(rawPrimaryKey);
    }

    @Override
    public PrimaryKeyInterface<?> translateVisualToRaw(int visualId) {
        int arrayListId = getArrayListIdFromVisualId(visualId);
        try {
            return this.forwardMap_.get(arrayListId);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public List<VisualTuple<? extends RelationInterface>> getVisualTupleList() {
        return this.visualTupleList_;
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
