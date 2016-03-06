package component.front_end.ui.core;

import component.back_end.storage.RelationInterface;

import java.util.List;

/**
 * Created by maianhvu on 6/3/16.
 */
public abstract class VisualIndexView<T extends RelationInterface> extends View<List<T>> {

    private List<VisualTuple<T>> visualTupleList_;

    public VisualIndexView(List<T> data) {
        super(data);
    }

    public void setVisualTupleList(List<VisualTuple<T>> visualTupleList) {
        this.visualTupleList_ = visualTupleList;
    }

    protected List<VisualTuple<T>> getVisualTuples() {
        return this.visualTupleList_;
    }
}
