package logic;

import javafx.util.Pair;
import shared.Command;
import storage.Task;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class VisualIndexMapper {

    private static final VisualIndexMapper instance = new VisualIndexMapper();

    private List<Task> _itemsList;

    public static VisualIndexMapper getInstance() {
        return instance;
    }

    private VisualIndexMapper() {}

    public void updateList(List<Task> list) {
        this._itemsList = list;
    }

    public void translateVisualToRaw(Command command) {
        int visualIndex = command.getInstruction().getIndex();
        Task item = this._itemsList.get(getArrayIndexFromVisualIndex(visualIndex));
        int rawIndex = item.getId();
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

}
