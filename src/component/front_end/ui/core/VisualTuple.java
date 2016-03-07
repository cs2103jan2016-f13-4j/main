package component.front_end.ui.core;

import component.back_end.storage.RelationInterface;

/**
 * Created by maianhvu on 6/3/16.
 */
public class VisualTuple<T extends RelationInterface> {

    private final Integer index_;
    private final T tuple_;

    public VisualTuple(Integer index_, T tuple) {
        this.index_ = index_;
        this.tuple_ = tuple;
    }

    public Integer getIndex() {
        return this.index_;
    }

    public T getOriginal() {
        return this.tuple_;
    }
}
