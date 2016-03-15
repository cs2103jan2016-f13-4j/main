package component.front_end.ui.core;

import java.util.List;

/**
 * Created by maianhvu on 6/3/16.
 */
public abstract class VisualIndexView<Task> extends View<List<VisualTuple<Task>>> {

    public VisualIndexView(List<VisualTuple<Task>> data) {
        super(data);
    }

    protected List<VisualTuple<Task>> getVisualTupleList() {
        return (List<VisualTuple<Task>>) this.getViewData();
    }

}
