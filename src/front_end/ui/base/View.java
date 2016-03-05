package front_end.ui.base;

/**
 * Created by maianhvu on 5/3/16.
 */
public class View<T> {

    private Class<? extends UserInterface<T>> uiClass_;
    private T data_;
    private String title_;

    public View(Class<? extends UserInterface<T>> uiClass, T data, String title) {
        this.uiClass_ = uiClass;
        this.data_ = data;
    }

    public View(Class<? extends UserInterface<T>> uiClass, T data) {
        this(uiClass, data, null);
    }

    public Class<? extends UserInterface<T>> getUiClass() {
        return this.uiClass_;
    }

    public T getData() {
        return this.data_;
    }

    public String getTitle() {
        return this.title_;
    }
}
