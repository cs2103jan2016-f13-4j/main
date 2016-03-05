package front_end.ui.base;

import back_end.storage.base.Index;
import back_end.storage.base.SerialIdRelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maianhvu on 5/3/16.
 */
public class VisualIdTranslator<T extends SerialIdRelation> {

    private ArrayList<Index> fromVisualToRawMap_;
    private HashMap<Index, Integer> fromRawToVisualMap_;
    private List<VisualTuple<T>> visualTupleList_;

    public VisualIdTranslator(List<T> taskList) {
        this.fromVisualToRawMap_ = new ArrayList<>();
        this.fromRawToVisualMap_ = new HashMap<>();
        this.visualTupleList_ = new ArrayList<>();

        // Populate index map
        for (T tuple : taskList) {
            Index rawTaskId = (Index) tuple.getPrimaryKey();
            // Enumerate the task IDs in ascending order
            this.fromVisualToRawMap_.add(rawTaskId);

            // Get the index of the latest inserted tuple inside the visualToRaw list
            Integer lastInsertedIndex = this.fromVisualToRawMap_.size() - 1;

            // Map the raw index to the most recently added index inside the array list
            this.fromRawToVisualMap_.put(rawTaskId, lastInsertedIndex);

            // Add visual tuple
            this.visualTupleList_.add(new VisualTuple<T>(
                    translateArrayListIndexToVisual(lastInsertedIndex),
                    tuple
            ));

        }
    }

    public Integer translateRawToVisual(Index rawIndex) {
        assert(rawIndex != null);
        Integer arrayListIndex = this.fromRawToVisualMap_.get(rawIndex);

        // Index not found
        if (arrayListIndex == null) {
            return null;
        }

        return translateArrayListIndexToVisual(this.fromRawToVisualMap_.get(rawIndex));
    }

    public Index translateVisualToRaw(Integer visualIndex) {
        assert(visualIndex != null);
        Integer arrayListIndex = translateVisualIndexToArrayList(visualIndex);

        // Handle case where index not found
        try {
            return this.fromVisualToRawMap_.get(arrayListIndex);
        } catch (IndexOutOfBoundsException e) {
            // FIXME: Handle error
            return null;
        }
    }

    public List<VisualTuple<T>> getVisualTupleList() {
        return this.visualTupleList_;
    }

    private static Integer translateArrayListIndexToVisual(Integer arrayListIndex) {
        assert(arrayListIndex != null);
        // Array list index starts from 0 but actual visual index starts from 1
        // So to get the visual index from array list index, just add 1
        return arrayListIndex + 1;
    }

    private static Integer translateVisualIndexToArrayList(Integer visualIndex) {
        assert(visualIndex != null);
        // Visual index starts from 1 but array list index starts from 0
        // So to get the array list index from visual index, just minus 1
        return visualIndex - 1;
    }

    // Package methods exposed for testing
    ArrayList<Index> getIndexList() {
        return this.fromVisualToRawMap_;
    }
}

