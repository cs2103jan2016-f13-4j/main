package component.front_end.ui;

import java.time.LocalDateTime;
import java.util.List;

import entity.Task;
/**
 * This view class generate the view data when list of tasks object are to be rendered
 * @author Tio
 *
 */
public class TaskListView extends View<List<Task>> {
    private List<Task> viewData_;
    
    public TaskListView(List<Task> list){
        super(list);
    }

    @Override
    public void buildContent() {
        List<Task> tasks = (List<Task>) this.getViewData();
        for (Task task : tasks) {
            this.addLine(task.getTask());
        }
    }
    
    private String constructTimeString(Task current){
        LocalDateTime Start = current.getStartingTime();
        LocalDateTime End = current.getEndingTime(); 
        return "";
    }
}