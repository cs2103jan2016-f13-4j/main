package front_end.ui.base;

/**
 * Created by maianhvu on 5/3/16.
 */
public class View<T> {

    private Class<? extends UserInterface<T>> uiClass_;
    private T data_;

    public View(Class<? extends UserInterface<T>> uiClass, T data) {
        this.uiClass_ = uiClass;
        this.data_ = data;
    }

    public Class<? extends UserInterface<T>> getUiClass() {
        return this.uiClass_;
    }

    public T getData() {
        return this.data_;
    }

}
