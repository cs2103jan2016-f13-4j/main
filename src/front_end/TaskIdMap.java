package front_end;

import back_end.storage.base.Index;
import back_end.storage.relations.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maianhvu on 5/3/16.
 */
public class TaskIdMap {

    private ArrayList<Index> fromVisualToRawMap_;
    private HashMap<Index, Integer> fromRawToVisualMap_;

    public TaskIdMap(List<Task> taskList) {
        this.fromVisualToRawMap_ = new ArrayList<>();
        this.fromRawToVisualMap_ = new HashMap<>();

        // Populate index map
        for (Task task : taskList) {
            Index rawTaskId = (Index) task.getPrimaryKey();
            // Enumerate the task IDs in ascending order
            this.fromVisualToRawMap_.add(rawTaskId);
            // Map the raw index to the most recently added index inside the array list
            this.fromRawToVisualMap_.put(rawTaskId, this.fromVisualToRawMap_.size() - 1);
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
        } finally {
            return null;
        }
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
}

