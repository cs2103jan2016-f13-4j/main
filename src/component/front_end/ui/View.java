package component.front_end.ui;


abstract class View<T> implements ViewSpec {
    protected final static String STRING_NEW_LINE = "\n";
    protected final static String STRING_TIME_CONNECTOR = "-";
    protected final static String STRING_EMPTY = "";
    private T viewData_;
    
    public View(T data){
        this.viewData_ = data;
    }
    
    @Override
    public Object getViewData() {
        // TODO Auto-generated method stub
        return this.viewData_;
    }
    
}
