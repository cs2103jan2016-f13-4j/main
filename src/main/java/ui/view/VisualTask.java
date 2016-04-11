package ui.view;

import shared.Task;

/**
 * @@author A0127036M
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

    @Override public boolean equals(Object obj){
        if(obj == null){
            return false;
        } else if ( this == obj){
            return true;
        } else if(!(obj instanceof VisualTask)) {
         return false;
        }


        VisualTask compared = (VisualTask) obj;
        return this.getTask().equals(compared.getTask()) &&
                    this.getVisualIndex() == compared.getVisualIndex();

    }

}
