package ui.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import shared.Resources;
import shared.ViewType;
import storage.Task;
import ui.controller.TaskListController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @@author Antonius Satrio Triatmoko
 */
public class TaskListView extends View {
    private final int MAXIMUM_DISPLAY_SIZE = 10;

    private ObservableList _observableList;
    private TaskListController _listControl ;
    private List<Pair<Integer,Task>> _displayList;
    private int _viewIndex;
    /**
     * Constructs a new view containing the provided data
     *
     * @param data
     */
    public TaskListView(List<Pair<Integer,Task>> data) {
        super(data);
        this._viewIndex = 0;
    }

    @Override protected void buildContent() {
        _displayList =  constructDisplayList();
        _observableList  = FXCollections.observableArrayList(_displayList);
        ListView listView = Resources.getInstance().getComponent("TaskList");
        listView.setItems(this._observableList);
        listView.setCellFactory(list -> new Item());

        this.setComponent(listView);
    }


    public  class Item extends ListCell<Pair<Integer, Task>> {
        private static final String STRING_NAME_TEMPLATE = "TaskListItem";
        private static final String STRING_DATE_PATTERN = "EE";
        private static final String STRING_DATE_NULL = "";
        @FXML private AnchorPane _container;
        @FXML private Label _indexLabel;
        @FXML private Label _nameLabel;
        @FXML private Label _dateLabel;
        private DateTimeFormatter _df = DateTimeFormatter.ofPattern(STRING_DATE_PATTERN);

        public Item() {
            super();
            this._container = Resources.getInstance().getComponent(STRING_NAME_TEMPLATE);
            this._indexLabel = (Label) this._container.lookup("#_indexLabel");
            this._nameLabel = (Label) this._container.lookup("#_taskNameLabel");
            this._dateLabel = (Label) this._container.lookup("#_timeLabel");
            assert this._indexLabel != null;
            assert this._nameLabel != null;
            assert this._dateLabel != null;
        }

        @Override protected void updateItem(Pair<Integer, Task> item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                this.setGraphic(null);
            } else {
                int index = item.getKey();
                Task task = item.getValue();

                this._indexLabel.setText(Integer.toString(index));
                this._nameLabel.setText(task.getTaskName());
                constructTime(task);

                this.setGraphic(this._container);
            }
        }

        private void constructTime(Task tsk) {
            LocalDateTime st = tsk.getStartTime();
            if(hasNoTime(tsk)){
                this._dateLabel.setText(STRING_DATE_NULL);
            } else {
                this._dateLabel.setText(st.format(_df));
            }

        }

        private boolean hasNoTime(Task tsk){
            return tsk.getStartTime() == null && tsk.getEndTime() == null;

        }
    }

    //helper Function
    public List<Pair<Integer,Task>> constructDisplayList(){
        List<Pair<Integer,Task>> temp = new ArrayList<Pair<Integer, Task>>();
        List<Pair<Integer,Task>> viewData = (List<Pair<Integer,Task>>)this.getData();
        int dataSize = viewData.size();
        int windowStart = this._viewIndex * MAXIMUM_DISPLAY_SIZE;
        if(viewData.size() - windowStart > MAXIMUM_DISPLAY_SIZE){
            for(int i = windowStart; i < MAXIMUM_DISPLAY_SIZE + windowStart ; i++ ){
                temp.add(viewData.get(i));
            }
            return temp;
        } else {

                return viewData;
        }

    }

    public void setObservableValue(List<Pair<Integer,Task>> list){
        this._observableList = FXCollections.observableArrayList(list);
    }

    public void incrementViewIndex(){
        this._viewIndex++;
    }

    public void decrementViewIndex(){
        this._viewIndex--;
    }
}
