package entity;


import component.back_end.storage.RelationInterface;
import component.front_end.ui.core.ViewSpec;
import component.front_end.ui.core.VisualTuple;

import java.util.ArrayList;
import java.util.List;

public class ExecutionResult<T> {

    /**
     * Properties
     */
    private Class<? extends ViewSpec> viewClass_;
    private List<Message> messages_;
    private T data_;

    public ExecutionResult(Class<? extends ViewSpec> viewClass, T data) {
        this.viewClass_ = viewClass;
        this.data_ = data;
    }

    public void addMessage(Message message) {
        // Initialize message list only if needed
        if (this.messages_ == null) {
            this.messages_ = new ArrayList<>();
        }

        this.messages_.add(message);
    }

    public List<Message> getMessages() {
        return this.messages_;
    }

    public Class<? extends ViewSpec> getViewClass() {
        return this.viewClass_;
    }
    
    public T getData() {
        return this.data_;
    }
    
    public ExecutionResult<?> transformToVisual(List<VisualTuple<? extends RelationInterface>> visualTupleList) {
        ExecutionResult<?> visualResult = new ExecutionResult<>(this.viewClass_, visualTupleList);
        visualResult.messages_ = this.messages_;
        return visualResult;
    }
}
