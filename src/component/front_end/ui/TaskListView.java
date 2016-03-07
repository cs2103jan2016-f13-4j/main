package component.front_end.ui;

import component.back_end.storage.Task;

import java.util.List;

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
    public void buildContent() {
        List<Task> tasks = (List<Task>) this.getViewData();
        for (Task task : tasks) {
            this.addLine(task.getTaskName());
        }
    }
    
//    private String constructTimeString(Task t){
//        String time = super.STRING_EMPTY;
//        return time;
//    }
}