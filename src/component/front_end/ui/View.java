package component.front_end.ui;


public class View<T> implements ViewSpec {
    
    private T viewData_;
    
    public View(T data){
        this.viewData_ = data; 

    }
    @Override
    public String getContent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getViewData() {
        // TODO Auto-generated method stub
        return this.viewData_;
    }

}
