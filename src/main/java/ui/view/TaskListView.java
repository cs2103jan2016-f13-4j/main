package ui.view;

import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import shared.Command;
import shared.CustomTime;
import shared.Resources;
import shared.Task;
import ui.controller.DateFormatterHelper;
import ui.controller.TaskListController;

import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @@author Antonius Satrio Triatmoko
 */
public class TaskListView extends View {
    /**
     * Constants
     */
    private final int MAXIMUM_DISPLAY_SIZE = 8;

    /**
     * Properties
     */
    private ObservableList _observableList;
    private List<Pair<Integer, Task>> _displayList;
    private int _viewIndex;
    private int _newTaskIndex;

    /**
     * Constructs a new view containing the provided data
     *
     * @param data
     */
    public TaskListView(List<Pair<Integer, Task>> data, Command lastCommand) {
        super(data, lastCommand);
        _viewIndex = 0;
        _newTaskIndex = -1;
    }

    @Override protected void buildContent() {
        // find viewIndex for new task if the last command is add
        if(this.getLastCommand().getInstruction() == Command.Instruction.ADD){
            Pair<Integer, Integer> indexPair =  obtainNewTaskIndex();
            this._viewIndex = indexPair.getValue();
            this._newTaskIndex = indexPair.getKey();
        }

        _displayList = constructDisplayList();
        _observableList = FXCollections.observableArrayList(_displayList);

        ListView listView = Resources.getInstance().getComponent("TaskList");
        listView.setItems(this._observableList);

        final int highlightIndex = this._newTaskIndex;

        listView.setCellFactory(list -> new Item(this.getLastCommand(),highlightIndex));

        this.setComponent(listView);
    }

    private Pair<Integer,Integer> obtainNewTaskIndex(){
        List<Pair<Integer,Task>> taskList = this.getData();
        int index = 0;
        Task temp ;
        Task current= null;
        for(int i = 0; i < taskList.size();  i++){
            if(current == null){
                current = taskList.get(i).getValue();
            } else {
                temp = taskList.get(i).getValue();
                LocalDateTime curCreationTime = current.getCreationTime();
                LocalDateTime tempCreationTime = temp.getCreationTime();
                if(curCreationTime.compareTo(tempCreationTime) < 0){
                    current = temp;
                    index = i;
                }
            }
        }
        return new Pair<>(index,index/MAXIMUM_DISPLAY_SIZE);
    }

    public static class Item extends ListCell<Pair<Integer, Task>> {
        private static final String STRING_NAME_TEMPLATE = "TaskListItem";
        private static final String STRING_HIGHLIGHT_COLOR = "#FBFF74";
        @FXML private AnchorPane _container;
        @FXML private Label _indexLabel;
        @FXML private Label _nameLabel;
        @FXML private Label _dateLabel;
        @FXML private Rectangle _highlight;
        private DateFormatterHelper _df = new DateFormatterHelper();
        private Command _lastCommand;
        private int _newTaskIndex;

        public Item(Command lastCommand){
            this(lastCommand,-1);
        }

        public Item(Command lastCommand, int newTaskIndex) {
            super();
            this._container = Resources.getInstance().getComponent(STRING_NAME_TEMPLATE);
            this._indexLabel = (Label) this._container.lookup("#_indexLabel");
            this._nameLabel = (Label) this._container.lookup("#_taskNameLabel");
            this._dateLabel = (Label) this._container.lookup("#_timeLabel");
            this._highlight = (Rectangle) this._container.lookup("#_highlightEffect");
            assert this._indexLabel != null;
            assert this._nameLabel != null;
            assert this._dateLabel != null;
            assert this._highlight != null;

            this._lastCommand = lastCommand ;
            this._newTaskIndex = newTaskIndex;

        }

        @Override protected void updateItem(Pair<Integer, Task> item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                this.setGraphic(null);
            } else {
                int index = item.getKey();
                Task task = item.getValue();

                // Grey out completed tasks
                if (task.isCompleted()) {
                    this.getStyleClass().add("completed");
                }

                // Take care of priority
                if (task.getPriority() != null) {
                    this.getStyleClass().add("priority--" + task.getPriority().name().toLowerCase());
                }

                this._indexLabel.setText(Integer.toString(index));
                this._nameLabel.setText(task.getTaskName());

                //set animation for newly added task
                if(this._lastCommand.getInstruction() == Command.Instruction.ADD && item.getKey() == (this._newTaskIndex +1)) {
                    setHighlightAnimation();
                }
                // Optional date time to support floating tasks
                this._dateLabel.setText(_df.getPairDateDisplay(task.getStartTime(),task.getEndTime()));

                this.setGraphic(this._container);
            }
        }

        @Override public boolean equals(Object o) {
            if (o == null)
                return false;
            if (this == o)
                return true;

            if (o instanceof Pair) {
                Pair<Integer, Task> data = (Pair<Integer, Task>) o;
                if (!data.getKey().toString().equals(this._indexLabel.getText()))
                    return false;
                if (!data.getValue().getTaskName().equals(this._nameLabel.getText()))
                    return false;
                return true;
            } else if (o instanceof Item) {
                Item otherCell = (Item) o;
                if (!this._indexLabel.getText().equals(otherCell._indexLabel.getText()))
                    return false;
                if (!this._nameLabel.getText().equals(otherCell._nameLabel.getText()))
                    return false;
                if (!this._dateLabel.getText().equals(otherCell._dateLabel.getText()))
                    return false;
                return true;
            } else {
                return false;
            }
        }

        private void setHighlightAnimation(){
            //System.out.println("setting up highlight animation");

            FillTransition highlight = new FillTransition(Duration.millis(750),this._highlight,Color.WHITE,Color.web(STRING_HIGHLIGHT_COLOR));
            highlight.setCycleCount(2);
            highlight.setAutoReverse(true);
            highlight.setInterpolator(Interpolator.EASE_BOTH);
            highlight.play();

        }
    }

    private List<Pair<Integer, Task>> constructDisplayList() {
        List<Pair<Integer, Task>> temp = new ArrayList<>();
        List<Pair<Integer, Task>> viewData = this.getData();

        int startIndex = this._viewIndex * MAXIMUM_DISPLAY_SIZE;
        int difference = viewData.size() - startIndex;
        if (difference > MAXIMUM_DISPLAY_SIZE) {
            for (int i = startIndex; i < (startIndex + MAXIMUM_DISPLAY_SIZE); i++) {
                temp.add(viewData.get(i));
            }
        } else {
            for (int i = startIndex; i < viewData.size(); i++) {
                temp.add(viewData.get(i));
            }
        }

        return temp;

    }

    @Override public Function<KeyEvent, Boolean> getKeyInputInterceptor() {
        return (event -> {

            if (event.getCode().equals(KeyCode.UP) && canScrollUp()) {
                this._viewIndex--;
            } else if (event.getCode().equals(KeyCode.DOWN) && canScrollDown()) {
                this._viewIndex++;
            } else {
                return false;
            }

            this._displayList = this.constructDisplayList();
            this._observableList.setAll(this._displayList);
            event.consume();
            return true;
        });
    }

    private boolean canScrollUp() {
        return (this._viewIndex - 1) >= 0;
    }

    private boolean canScrollDown() {
        List<Pair<Integer, Task>> viewData = this.getData();
        int size = viewData.size() - (this._viewIndex + 1) * MAXIMUM_DISPLAY_SIZE;
        return size > 0;
    }
}
