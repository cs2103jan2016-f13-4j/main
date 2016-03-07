package component.front_end;

import component.back_end.storage.Task;
import component.front_end.ui.core.VisualTuple;

import java.util.List;

public interface VisualIndexMapperSpec {
    int translateRawToVisual(Integer rawPrimaryKey);
    Integer translateVisualToRaw(int visualId);
    List<VisualTuple<Task>> getVisualTupleList();
}
