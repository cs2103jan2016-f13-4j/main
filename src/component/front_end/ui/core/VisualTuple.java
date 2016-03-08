package component.front_end.ui.core;

/**
 * Created by maianhvu on 6/3/16.
 */
public class VisualTuple<Task> {

    private final Integer index_;
    private final Task task_;

    public VisualTuple(Integer index_, Task task) {
        this.index_ = index_;
        this.task_ = task;
    }

    public Integer getIndex() {
        return this.index_;
    }

    public Task getOriginal() {
        return this.task_;
    }
}
