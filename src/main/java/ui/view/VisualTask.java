package ui.view;

import shared.Task;

/**
 * Created by maianhvu on 05/04/2016.
 */
public class VisualTask {

    private int _visualIndex;
    private Task _task;
    private boolean _isHighlighted;

    public VisualTask(int visualIndex, Task task) {
        this._visualIndex = visualIndex;
        this._task = task;

        this._isHighlighted = false;
    }

    public int getVisualIndex() {
        return this._visualIndex;
    }

    public Task getTask() {
        return this._task;
    }

    public boolean isHighlighted() {
        return this._isHighlighted;
    }

    public void highlight() {
        this._isHighlighted = true;
    }
}
