package component.front_end.ui;

import java.util.List;

import entity.Task;
/**
 * This view class generate the view data when list of tasks object are to be rendered
 * @author Tio
 *
 */
public class TaskListView extends View<List<Task>>{
    
    private List<Task> viewData_;
    
    public TaskListView(List<Task> list){
        super(list);
        
    }

    @Override
    public String getContent() {
        List<Task> tasks = (List<Task>)this.getViewData(); 
        int size = this.viewData_.size();
        String result = super.STRING_EMPTY; 
        for( int i = 0; i < size ; i++){
            Task current = tasks.get(i);
            // supposed to take detail from the Tasks 
            this.createNewLine(result);
        }
        return result;
    }
    // helper function
    private String createNewLine(String result){
        return (result + super.STRING_NEW_LINE); 
    }
    
    private String constructTimeString(Task t){
        String time = super.STRING_EMPTY;
        return time; 
    }
}