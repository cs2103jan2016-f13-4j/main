package objects;

import front_end.ui.core.UserInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maianhvu on 5/3/16.
 */
public class ExecutionResult<T> {

    private List<Message> messages_;
    private Class<? extends UserInterface<T>> uiClass_;
    private T data_;

    public ExecutionResult(Class<? extends UserInterface<T>> uiClass, T data) {
        this.uiClass_ = uiClass;
        this.data_ = data;
    }

    public static ExecutionResult nullResult() {
        return new ExecutionResult(null, null);
    }

    public boolean hasMessage() {
        return this.messages_ != null && !this.messages_.isEmpty();
    }

    public List<Message> getMessages() {
        return this.messages_;
    }

    public void setMessages(List<Message> messages) {
        this.messages_ = messages;
    }

    public void addMessage(Message message) {
        // Initialize messages array if there isn't any
        if (this.messages_ == null) {
            this.messages_ = new ArrayList<>();
        }

        this.messages_.add(message);
    }

    public Class<? extends UserInterface<T>> getUiClass() {
        return this.uiClass_;
    }

    public T getData() {
        return this.data_;
    }

}
