package ui.view;

import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
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
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import shared.Command;
import shared.Resources;
import shared.Task;
import ui.controller.DateFormatterHelper;

import javafx.util.Duration;
import java.time.LocalDateTime;
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
    private List<VisualTask> _displayList;
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
        // find viewIndex for new task if the last command is add
        if(this.getLastCommand().getInstruction() == Command.Instruction.ADD){
            Pair<Integer, Integer> indexPair =  obtainNewTaskIndex();
            this._viewIndex = indexPair.getValue();
            this._newTaskIndex = indexPair.getKey();
        } else {
            this._viewIndex = 0;
            this._newTaskIndex = -1;
        }

        this._displayList = constructDisplayList();
        _observableList = FXCollections.observableArrayList(_displayList);

        ListView listView = Resources.getInstance().getComponent("TaskList");
        listView.setItems(this._observableList);

        final int highlightIndex = this._newTaskIndex;

        listView.setCellFactory(list -> new Item(this.getLastCommand(),highlightIndex));

        this.setComponent(listView);
    }

    private Pair<Integer,Integer> obtainNewTaskIndex(){
        List<VisualTask> taskList = this.getData();
        int index = 0;
        Task temp ;
        Task current= null;
        for(int i = 0; i < taskList.size();  i++){
            if(current == null){
                current = taskList.get(i).getTask();
            } else {
                temp = taskList.get(i).getTask();
                LocalDateTime curCreationTime = current.getCreationTime();
                LocalDateTime tempCreationTime = temp.getCreationTime();
                if(curCreationTime.compareTo(tempCreationTime) < 0){
                    current = temp;
                    index = i;
                }
            }
        }
        return new Pair<Integer, Integer>(index,index/MAXIMUM_DISPLAY_SIZE);
    }

    private class Item extends ListCell<VisualTask> {
        private static final int STRING_FIRST_ITEM = 1;
        public static final double STRING_HIGHLIGHT_OPACITY = .31;
        private static final String STRING_NAME_TEMPLATE_EVENT = "TaskListItemDouble";
        private static final String STRING_NAME_TEMPLATE_SINGLE = "TaskListItemSingle";
        private static final String STRING_HIGHLIGHT_COLOR = "#FBFF74";
        private final Color COLOR_TRANSPARENT_FULL = new Color(1,1,1,0);
        private final Color COLOR_TRANSPARENT_NONE = new Color(1,1,1,0.8);
        @FXML private AnchorPane _container;
        @FXML private Label _indexLabel;
        @FXML private Label _nameLabel;
        @FXML private Label _startLabelPrefix;
        @FXML private Label _startLabelTime;
        @FXML private Label _endLabelPrefix;
        @FXML private Label _endLabelTime;
        @FXML private Rectangle _highlight;
        @FXML private Rectangle _canScrollUp;
        private DateFormatterHelper _df = new DateFormatterHelper();
        private Command _lastCommand;
        private int _newTaskIndex;

        public Item(Command lastCommand){
            this(lastCommand,-1);
        }

        public Item(Command lastCommand, int newTaskIndex) {
            super();


                this._container = Resources.getInstance().getComponent(STRING_NAME_TEMPLATE_EVENT);


            this._indexLabel = (Label) this._container.lookup("#_indexLabel");
            this._nameLabel = (Label) this._container.lookup("#_taskNameLabel");
            this._highlight = (Rectangle) this._container.lookup("#_highlightEffect");
            this._canScrollUp = (Rectangle) this._container.lookup("#_scrollUp");
            assert this._indexLabel != null;
            assert this._nameLabel != null;
            assert this._highlight != null;
            assert this._canScrollUp != null;

            this._lastCommand = lastCommand ;
            this._newTaskIndex = newTaskIndex;

        }

        @Override protected void updateItem(VisualTask item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                this.setGraphic(null);
            } else {
                System.out.println("update item");
                //TODO: Check the time stored in the task. If it is an event, use TaskListItemEvent. Else, use TaskListItemSingle
                int index = item.getVisualIndex();
                Task task = item.getTask();

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
                if (this._lastCommand.getInstruction() == Command.Instruction.ADD &&
                        item.getVisualIndex() == (this._newTaskIndex +1)) {
                    setHighlightAnimation();
                }

                // check for any reused cell, the behaviour of the ListCell is that sometimes it might reproduce an existing cell,
                // so there is a need to reset the effect applied, else it might interfere with the interface
                if(this._canScrollUp.getOpacity() == 1){
                    this._canScrollUp.setOpacity(0);
                }

                // set indicator for scrolling up
                if (this.getItem().getVisualIndex()%MAXIMUM_DISPLAY_SIZE == STRING_FIRST_ITEM && canScrollUp()) {
                    setScrollUpIndicator();
                }

                setUpTime(task);

                this.setGraphic(this._container);
            }
        }

        private void setHighlightAnimation(){
            FillTransition highlight = new FillTransition(
                    Duration.millis(750),
                    this._highlight,
                    Color.WHITE,
                    Color.web(STRING_HIGHLIGHT_COLOR, STRING_HIGHLIGHT_OPACITY)
            );
            highlight.setCycleCount(2);
            highlight.setAutoReverse(true);
            highlight.setInterpolator(Interpolator.EASE_BOTH);
            highlight.play();

        }

        private void setScrollUpIndicator() {
            this._canScrollUp.setOpacity(1);
            Stop[] pattern = new Stop[] {new Stop(0.9,COLOR_TRANSPARENT_NONE), new Stop(1,COLOR_TRANSPARENT_FULL)};
            LinearGradient gradientFlow = new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE,pattern);
            this._canScrollUp.setFill(gradientFlow);
        }

        @Override public boolean equals( Object obj){
            if(obj == null){
                return false;
            } else if ( this == obj){
                return true;
            }

            if(obj instanceof Item){
                Item another = (Item) obj;
                VisualTask itemStored = another.getItem();
                return this.equals(itemStored);
            }

            if(obj instanceof VisualTask){
                VisualTask comparator = (VisualTask) obj;
                return obj.equals(this.getItem());
            }

            return false;
        }

        private AnchorPane setContainer(Task task){
            if (task.getStartTime() != null && task.getEndTime() != null) {
                return (AnchorPane) Resources.getInstance().getComponent(STRING_NAME_TEMPLATE_EVENT);
            } else {
                return (AnchorPane) Resources.getInstance().getComponent(STRING_NAME_TEMPLATE_SINGLE);
            }
        }

        private void setUpTime(Task task) {

            if (isEvent(task)) { // task is an event

                this._startLabelPrefix = (Label) this._container.lookup("#_startPrefix") ;
                this._startLabelTime = (Label) this._container.lookup("#_startTime");
                this._endLabelPrefix = (Label) this._container.lookup("#_endPrefix");
                this._endLabelTime = (Label) this._container.lookup("#_endTime");

                assert this._startLabelPrefix != null;
                assert this._startLabelTime != null;
                assert this._endLabelPrefix != null;
                assert this._endLabelTime != null;

                // set up time
                this._startLabelPrefix.setText("From");
                this._startLabelTime.setText(_df.getDateTimeDisplay(task.getStartTime()));
                this._endLabelPrefix.setText("To");
                this._endLabelTime.setText(_df.getDateTimeDisplay(task.getEndTime()));

            } else { // Task is floating, or has only one time recorded

                this._startLabelTime = (Label) this._container.lookup("#_timeLabel");

                assert this._startLabelPrefix != null;
                assert this._startLabelTime != null;

               // this._startLabelTime.setText("");

            }
        }

        private boolean isEvent(Task task) {
            return task.getStartTime() != null && task.getEndTime() != null;
        }
    }

    private List<VisualTask> constructDisplayList() {
        List<VisualTask> temp = new ArrayList<>();
        List<VisualTask> viewData = this.getData();

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
        List<VisualTask> viewData = this.getData();
        int size = viewData.size() - (this._viewIndex + 1) * MAXIMUM_DISPLAY_SIZE;
        return size > 0;
    }
}
