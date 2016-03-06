package component.front_end.ui;


abstract class View<T> implements ViewSpec {
    private final static String STRING_NEW_LINE = "\n";
    private final static String STRING_TIME_CONNECTOR = "-";
    private final static String STRING_EMPTY = "";

    private T viewData_;
    private StringBuilder contentBuilder_;
    
    public View(T data){
        this.viewData_ = data;
        this.contentBuilder_ = new StringBuilder();
    }

    @Override
    public abstract void buildContent();
    
    @Override
    public Object getViewData() {
        return this.viewData_;
    }

    protected void addLine(String string) {
        this.contentBuilder_.append(string);
        this.contentBuilder_.append(STRING_NEW_LINE);
    }

    @Override
    public String getContent() {
        return this.contentBuilder_.toString();
    }
}