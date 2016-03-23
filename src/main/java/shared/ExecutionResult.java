package shared;

import exception.ExceptionHandler;

/**
 * Created by maianhvu on 20/03/2016.
 */
public class ExecutionResult {
    /**
     * Properties
     */
    private ViewType _viewType;
    private Object _data;
    private boolean _isShutdownSignal;

    public ExecutionResult(ViewType viewType, Object data) {
        this._viewType = viewType;
        this._data = data;
        this._isShutdownSignal = false;
    }

    public ViewType getViewType() {
        return this._viewType;
    }

    /**
     * Constructs an empty execution result (normally used for shutdown signal)
     */
    private ExecutionResult(boolean isShutdown) {
        this._viewType = null;
        this._data = null;
        this._isShutdownSignal = isShutdown;
    }
    public static ExecutionResult shutdownSignal() {
        return new ExecutionResult(true);
    }

    public boolean isShutdownSignal() {
        return this._isShutdownSignal;
    }
    public <T> T getData() {
        try {
            return (T) this._data;
        } catch (ClassCastException e) {
            ExceptionHandler.handle(e);
            return null;
        }
    }
}