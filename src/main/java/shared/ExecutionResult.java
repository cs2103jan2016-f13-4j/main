package shared;

import exception.ExceptionHandler;

/**
 * Created by maianhvu on 20/03/2016.
 */
public class ExecutionResult {
    /**
     * Properties
     */
    private final ViewType _viewType;
    private final Object _data;

    /**
     * TODO: Write JavaDoc
     * @param viewType
     * @param data
     */
    public ExecutionResult(ViewType viewType, Object data) {
        this._viewType = viewType;
        this._data = data;
    }

    /**
     * TODO: Write JavaDoc
     * @return
     */
    public ViewType getViewType() {
        return this._viewType;
    }

    /**
     * TODO: Write JavaDoc
     * @param <T>
     * @return
     */
    public <T> T getData() {
        try {
            return (T) this._data;
        } catch (ClassCastException e) {
            ExceptionHandler.handle(e);
            return null;
        }
    }
}
