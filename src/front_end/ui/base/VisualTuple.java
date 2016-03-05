package front_end.ui.base;

import back_end.storage.base.SerialIdRelation;

/**
 * Created by maianhvu on 5/3/16.
 */
public class VisualTuple<T extends SerialIdRelation> {
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
