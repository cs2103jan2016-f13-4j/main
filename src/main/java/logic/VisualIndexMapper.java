package logic;

import javafx.util.Pair;
import shared.Command;
import shared.Range;
import shared.Task;
import ui.view.VisualTask;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * @@author Mai Anh Vu
 */
public class VisualIndexMapper {

    /**
     * Singleton instance
     */
    private static VisualIndexMapper instance = new VisualIndexMapper();

    /**
     * Properties
     */
    private List<Task> _itemsList;

    public static VisualIndexMapper getInstance() {
        return instance;
    }

    private VisualIndexMapper() {
        this._itemsList = new ArrayList<>();
    }

    public void translateVisualToRaw(Command command) {
        assert !this._itemsList.isEmpty();

        // Parse a single task index
        if (command.hasParameter(Command.ParamName.TASK_INDEX)) {
            int visualIndex = command.getParameter(Command.ParamName.TASK_INDEX);
            Task item = this._itemsList.get(getArrayIndexFromVisualIndex(visualIndex));
            int rawIndex = item.getId();
            // FIXME: Might be null when the task is out of range
            command.setParameter(Command.ParamName.TASK_INDEX, rawIndex);
        }

        // Parse a list of visual ranges
        if (command.hasParameter(Command.ParamName.TASK_INDEX_RANGES)) {

            // TODO: Limit all the ranges to the bounds of the current task list
            List<Range> ranges = command.getParameter(Command.ParamName.TASK_INDEX_RANGES);

            // Enumerate all indices
            int[] indices = Range.enumerateRanges(ranges);

            // We are sure that all the visual indices are now valid
            // Proceed with translation
            ranges = Arrays.stream(indices).map(VisualIndexMapper::getArrayIndexFromVisualIndex)
                    .mapToObj(this._itemsList::get)
                    .map(Task::getId)
                    .map(Range::new)
                    .collect(Collectors.toList());
            Range.straightenRanges(ranges);
            command.setParameter(Command.ParamName.TASK_INDEX_RANGES, ranges);
        }
    }

    public List<VisualTask> translateRawToVisual(List<Task> rawList) {
        this._itemsList = rawList;
        return IntStream.range(0, rawList.size())
                .mapToObj(index -> new VisualTask(
                        getVisualIndexFromArrayIndex(index),
                        rawList.get(index)))
                .collect(Collectors.toList());
    }

    private static int getVisualIndexFromArrayIndex(int arrayIndex) {
        return arrayIndex + 1;
    }

    private static int getArrayIndexFromVisualIndex(int visualIndex) {
        return visualIndex - 1;
    }
}
