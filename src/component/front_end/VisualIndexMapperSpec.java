package component.front_end;

import component.back_end.storage.PrimaryKeyInterface;
import component.back_end.storage.RelationInterface;
import component.front_end.ui.core.VisualTuple;

import java.util.List;

public interface VisualIndexMapperSpec {
    int translateRawToVisual(Integer rawPrimaryKey);
    Integer translateVisualToRaw(int visualId);
}
