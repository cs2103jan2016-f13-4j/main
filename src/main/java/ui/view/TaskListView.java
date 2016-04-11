package ui.view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import shared.Command;
import shared.Resources;
import shared.Task;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

/**
 * This class manage the process of displaying the list of Task that is called by the user to be displayed.
 * The View contains List of VisualTask which is a pair of Task and its display index.
 *
 * @@author Antonius Satrio Triatmoko
 */
public class TaskListView extends View {
    /***
     * constant
     */
    private final int SCROLL_RANGE = 5;
    /**
     * Properties
     */
    private ObservableList _observableList;
    private int _viewIndex;
    private int _newTaskIndex;

    /**
     * Constructs a new view containing the provided data
     *
     * @param data
     */
    public TaskListView(List<VisualTask> data, Command lastCommand) {
        super(data, lastCommand);
    }

    @Override protected void buildContent() {

        List<VisualTask> viewData = (List<VisualTask>) this.getData();
        _observableList = FXCollections.observableArrayList(viewData);

        ListView listView = Resources.getInstance().getComponent("TaskList");
        listView.setItems(this._observableList);

        if (this.getLastCommand().getInstruction() == Command.Instruction.ADD) {
            this._newTaskIndex = obtainNewTaskIndex();
        } else {
            this._newTaskIndex = 0;
        }

        final int highlightIndex = this._newTaskIndex;

        listView.setCellFactory(list -> new TaskListItem(this.getLastCommand(),highlightIndex));

        listView.scrollTo(this._newTaskIndex);
        this._viewIndex += this._newTaskIndex;


        this.setComponent(listView);
    }


    private int obtainNewTaskIndex() {
        List<VisualTask> taskList = this.getData();

        int index = 0;

        Task temp ;
        Task current= null;

        for (int i = 0; i < taskList.size();  i++) {
            if (current == null) {
                current = taskList.get(i).getTask();
            } else {

                temp = taskList.get(i).getTask();
                LocalDateTime curCreationTime = current.getCreationTime();
                LocalDateTime tempCreationTime = temp.getCreationTime();

                if (curCreationTime.compareTo(tempCreationTime) < 0) {
                    current = temp;
                    index = i;
                }

            }
        }

        return  index;
    }


    @Override public Function<KeyEvent, Boolean> getKeyInputInterceptor() {
        return (event -> {

            if (event.getCode().equals(KeyCode.UP) && canScrollUp()) {
                this._viewIndex -= SCROLL_RANGE;
            } else if (event.getCode().equals(KeyCode.DOWN) && canScrollDown()) {
                this._viewIndex += SCROLL_RANGE;
            } else {
                return false;
            }

            ListView temp = (ListView) this.getComponent();
            temp.scrollTo(this._viewIndex);
            event.consume();
            return true;
        });
    }

    private boolean canScrollUp() {
        return (this._viewIndex - SCROLL_RANGE) >= 0;
    }

    private boolean canScrollDown() {
        List<VisualTask> viewData = this.getData();
        int size = viewData.size() - (this._viewIndex + SCROLL_RANGE);
        return size > 0;
    }



}
