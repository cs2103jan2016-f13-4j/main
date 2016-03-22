package logic;

import shared.ViewType;

public class ExecutionResult {

    private ViewType _viewType;
    private Object _data;

    public ExecutionResult(ViewType viewType, Object data) {
        this._viewType = viewType;
        this._data = data;
    }

    public ViewType getViewType() {
        return this._viewType;
    }

    public Object getData() {
        return this._data;
    }
}
