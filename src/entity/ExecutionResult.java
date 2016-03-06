package entity;


public class ExecutionResult<T> {
    
    private boolean success_;
    private String message_;
    private T data_;
    
    
    public boolean getSuccess() {
        return success_;
    }
    
    public String getMessage() {
        return message_;
    }
    
    public T getData() {
        return data_;
    }
    
    
}
