package logic;

import javafx.util.Pair;
import shared.Command;
import storage.Task;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class VisualIndexMapper {

    private static final VisualIndexMapper instance = new VisualIndexMapper();

    private TreeMap<Integer, Task> _itemsMap;

    public static VisualIndexMapper getInstance() {
        return instance;
    }

    private VisualIndexMapper() {
        this._itemsMap = new TreeMap<>();
    }

    public void updateList(List<Task> list) {
        // Clear old items
        this._itemsMap.clear();

        // Populate new map
        IntStream.range(0, list.size())
                .forEach(index -> {
                    this._itemsMap.put(
                            getVisualIndexFromArrayIndex(index),
                            list.get(index));
                });
    }

    public void translateVisualToRaw(Command command) {
        assert this._itemsMap.isEmpty() == false;
        int visualIndex = command.getInstruction().getIndex();
        Task item = this._itemsMap.get(visualIndex);
        int rawIndex = item.getId();
        // FIXME: Might be null
        command.getInstruction().setIndex(rawIndex);
    }

    public List<Pair<Integer, Task>> translateRawToVisual(List<Task> rawList) {
        return IntStream.range(0, rawList.size())
                .mapToObj(index -> new Pair<>(index + 1, rawList.get(index)))
                .collect(Collectors.toList());
    }

    private static int getArrayIndexFromVisualIndex(int visualIndex) {
        return visualIndex - 1;
    }

    private static int getVisualIndexFromArrayIndex(int arrayIndex) {
        return arrayIndex + 1;
    }
}