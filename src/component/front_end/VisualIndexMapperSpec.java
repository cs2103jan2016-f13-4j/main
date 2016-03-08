package component.front_end;

import component.back_end.storage.Task;
import component.front_end.ui.core.VisualTuple;

import java.util.List;

public abstract class VisualIndexMapperSpec {
    public abstract int translateRawToVisual(Integer rawPrimaryKey);
    public abstract Integer translateVisualToRaw(int visualId);
    public abstract List<VisualTuple<Task>> getVisualTupleList();
}
