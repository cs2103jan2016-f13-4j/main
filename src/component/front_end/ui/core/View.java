package component.front_end.ui.core;

public abstract class View<T> implements ViewSpec {
    private final static String STRING_NEW_LINE = "\n";

    private final T viewData_;
    private final StringBuilder contentBuilder_;

    public View(T data){
        this.viewData_ = data;
        this.contentBuilder_ = new StringBuilder();

        this.buildContent();
    }

    @Override
    public abstract void buildContent();
    
    @Override
    public Object getViewData() {
        return this.viewData_;
    }

    protected void addLine() {
        this.contentBuilder_.append(STRING_NEW_LINE);
    }

    protected void addLine(String string, Object... args) {
        this.addText(string, args);
        this.contentBuilder_.append(STRING_NEW_LINE);
    }
    
    protected void addText(String string, Object... args){
        this.contentBuilder_.append(String.format(string, args));
    }

    @Override
    public String getContent() {
        return this.contentBuilder_.toString();
    }
}
