package component.front_end.ui.core;

import component.back_end.storage.RelationInterface;

import java.util.List;

/**
 * Created by maianhvu on 6/3/16.
 */
public abstract class VisualIndexView<T extends RelationInterface> extends View<List<VisualTuple<T>>> {

    public VisualIndexView(List<VisualTuple<T>> data) {
        super(data);
    }

    protected List<VisualTuple<T>> getVisualTupleList() {
        return (List<VisualTuple<T>>) this.getViewData();
    }

}
