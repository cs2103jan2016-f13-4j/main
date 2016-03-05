package front_end.ui.core;

import back_end.storage.base.SerialIdRelation;
import front_end.ui.base.VisualTuple;

import java.util.List;

/**
 * Created by maianhvu on 5/3/16.
 */
public abstract class VisualIndexUI<T extends SerialIdRelation> extends UserInterface<List<T>> {

    private List<VisualTuple<T>> visualTupleList_;

    public VisualIndexUI(List<T> data) {
        super(data);
    }

    public void setVisualTupleList(List<VisualTuple<T>> visualTupleList) {
        this.visualTupleList_ = visualTupleList;
    }

    protected List<VisualTuple<T>> getVisualTuples() {
        return this.visualTupleList_;
    }


}
