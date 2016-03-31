package shared;

import exception.ExceptionHandler;

/**
 * @@author Mai Anh Vu
 */
public class ExecutionResult {
    /**
     * Properties
     */
    private ViewType _viewType;
    private Object _data;
    private boolean _isShutdownSignal;
    private String _errorMessage;

    public ExecutionResult(ViewType viewType, Object data, String error) {
        this._viewType = viewType;
        this._data = data;
        this._isShutdownSignal = false;
        this._errorMessage = error;
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

    public String getErrorMessage(){
        return this._errorMessage;
    }
}
