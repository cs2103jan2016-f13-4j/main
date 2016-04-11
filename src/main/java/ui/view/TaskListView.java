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
import shared.CustomTime;
import shared.Resources;
import shared.Task;
import ui.controller.DateFormatterHelper;

import javafx.util.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This class manage the process of displaying the list of Task that is called by the user to be displayed.
 * Instead of Task, the View contains List of VisualTask which is a pair of Task and its display index.
 *
 * @@author Antonius Satrio Triatmoko
 */
public class TaskListView extends View {

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

        listView.setCellFactory(list -> new Item(this.getLastCommand(),highlightIndex));

        listView.scrollTo(this._newTaskIndex);
        this._viewIndex += this._newTaskIndex;


        this.setComponent(listView);
    }


    /**
     * The Item class is the extension of ListCell class which allow us to customize the display content of the list.
     * There are two possible .xml files that can be loaded depending on Date and Time information stored by the task.Event Task
     * (Task with both start time and end time)will load TaskListItemDouble.fxml while other type of task will
     * use TaskListItemSingle.fxml .
     *
     * DateFormatterHelper is used to help determining the date and time presentation.
     */
    private class Item extends ListCell<VisualTask> {
        public static final double STRING_HIGHLIGHT_OPACITY = .31;
        private static final String STRING_NAME_TEMPLATE_WITH_DATE = "TaskListItemDouble";
        private static final String STRING_NAME_TEMPLATE_NO_DATE = "TaskListItemSingle";
        private static final String STRING_HIGHLIGHT_COLOR = "#FBFF74";
        private final Color COLOR_TRANSPARENT_FULL = new Color(1,1,1,0);
        private final Color COLOR_TRANSPARENT_NONE = new Color(1,1,1,0.8);

        @FXML private AnchorPane _container;
        @FXML private Label _indexLabel;
        @FXML private Label _nameLabel;
        @FXML private Label _priorityLabel;
        @FXML private Label _timeLabel;
        @FXML private Label _dateLabel;
        @FXML private Rectangle _highlight;
        @FXML private Rectangle _canScrollUp;

        private DateFormatterHelper _df = new DateFormatterHelper();
        private Command _lastCommand;
        private int _newTaskIndex;
        private boolean _canHighlight = true;

        public Item(Command lastCommand, int newTaskIndex) {
            super();

            this._lastCommand = lastCommand ;
            this._newTaskIndex = newTaskIndex;

        }

        /***
         * This method link the Item Class to the corresponding  .fxml file used to display the list content.
         *
         * @param task the task that is going to be checked. Event Task will call TasklistItemDouble.fxml  whereas
         *             other type of task will call TaskListItemSingle.fxml.
         */
        private void updateGraphicPointer(Task task) {
            if (isSameDate(task)) {
                this._container = (AnchorPane) Resources.getInstance().getComponent(STRING_NAME_TEMPLATE_NO_DATE);
                assert this._container != null;

            } else {
                this._container = (AnchorPane) Resources.getInstance().getComponent(STRING_NAME_TEMPLATE_WITH_DATE);
                assert this._container != null;

                this._dateLabel = (Label) this._container.lookup("#_dateLabel");
                assert this._dateLabel != null;
            }

            this._indexLabel = (Label) this._container.lookup("#_indexLabel");
            this._nameLabel = (Label) this._container.lookup("#_taskNameLabel");
            this._highlight = (Rectangle) this._container.lookup("#_highlightEffect");
            this._canScrollUp = (Rectangle) this._container.lookup("#_scrollUp");
            this._timeLabel = (Label) this._container.lookup("#_timeLabel");
            this._priorityLabel = (Label) this._container.lookup("#_priorityIndicator");

            assert this._indexLabel != null;
            assert this._nameLabel != null;
            assert this._highlight != null;
            assert this._canScrollUp != null;
            assert this._timeLabel != null;
        }

        /***
         * Update the corresponding ListView cell with the appropriate content
         * @param item  Task (in form of Visual Task) that is going to be displayed
         * @param empty check if the item is empty
         */

        @Override protected void updateItem(VisualTask item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                this.setGraphic(null);
            } else {
                int index = item.getVisualIndex();
                Task task = item.getTask();

                // Update Cell Graphic Container and Link to Container Component
                this.updateGraphicPointer(task);

                // display the index and the task name
                this._indexLabel.setText(Integer.toString(index));
                this._nameLabel.setText(task.getTaskName());

                // set up the time to be displayed
                this.setUpTime(task);

                //The item sometimes is displayed using a reused ListCell Object, instead of a completely new ListCell Object instantiated.
                // So previous applied style and effect might still persist.
                // Hence, there is a need to reset the StyleClass applied to the cell.
                this.resetEffect();

                // Grey out completed tasks
                if (task.isCompleted()) {
                    this.getStyleClass().add("completed");
                }

                // apply highlight effect to the new task when first displayed;
                if (isAddCommand(this._lastCommand) && (this.getItem().getVisualIndex() - 1) == this._newTaskIndex ) {
                    System.out.println("highlighted " + this.getItem().getVisualIndex());
                    this.setHighlightAnimation();
                }

                // set priority indicator.
                if (task.getPriority() != null) {
                    this.setPriority(task);
                }


                this.setGraphic(this._container);
            }
        }

        private void setPriority(Task task) {
            this.getStyleClass().add("priority--" + task.getPriority().name().toLowerCase());

            if (task.getPriority() == Task.Priority.HIGH) {
                this._priorityLabel.setText("HIGH");
            } else if (task.getPriority() == Task.Priority.MEDIUM) {
                this._priorityLabel.setText("MEDIUM");
            } else if (task.getPriority() == Task.Priority.LOW) {
                this._priorityLabel.setText("LOW");
            }
        }

        private void resetEffect() {
            this.getStyleClass().clear();
            this.getStyleClass().add("cell");
            this.getStyleClass().add("indexed-cell");
            this.getStyleClass().add("list-cell");

            // check if any cell has scrollup indicator applied
            if(this._canScrollUp.getOpacity() == 1) {
                this._canScrollUp.setOpacity(0);
            }
        }


        /***
         * prepare and play the highlight animation to show new task
         * should only work the first time the task is created.
         */
        private void setHighlightAnimation(){
            if (_canHighlight) {

                _canHighlight = !_canHighlight;

                FillTransition highlight = new FillTransition(
                        Duration.millis(1500),
                        this._highlight,
                        Color.WHITE,
                        Color.web(STRING_HIGHLIGHT_COLOR, STRING_HIGHLIGHT_OPACITY)
                );
                highlight.setCycleCount(2);
                highlight.setAutoReverse(true);
                highlight.setInterpolator(Interpolator.EASE_BOTH);
                highlight.play();

            }

        }

        /***
         * apply scroll up indicator effect to the relevant listCell, usually the current first item on display
         */
        private void setScrollUpIndicator() {
            this._canScrollUp.setOpacity(1);
            Stop[] pattern = new Stop[] {new Stop(0.9,COLOR_TRANSPARENT_NONE), new Stop(1,COLOR_TRANSPARENT_FULL)};
            LinearGradient gradientFlow = new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE,pattern);
            this._canScrollUp.setFill(gradientFlow);
        }

        /***
         * This method prepare the time String to be displayed by utilising DateFormatHelper
         * For more detail on how the time stored within the task is procsessed, see DateFormatterHelper.java
         *
         * @param task task obtained from the list stored in the ListView Scene
         */
        private void setUpTime(Task task) {

            if (!isSameDate(task)) { // task is not same date as previous
                setDateHeading(task);

            }
            String result = _df.getCellTimeTaskDisplay(task);
            this._timeLabel.setText(result);
        }

        private boolean isSameDate(Task curTask){
            int curIndex = this.getIndex();

            if(curIndex == 0) {
                return false;
            } else {

                Task prevTask = this.getListView().getItems().get(curIndex - 1).getTask();

                CustomTime curStartTime = curTask.getStartTime();
                CustomTime curEndTime = curTask.getEndTime();
                CustomTime prevStartTime = prevTask.getStartTime();
                CustomTime prevEndTime = curTask.getEndTime();

                if (curStartTime != null) {
                    if (prevStartTime != null) {
                        return curStartTime.hasSameDate(prevStartTime);
                    } else if (prevEndTime != null) {
                        return curStartTime.hasSameDate(prevEndTime);
                    } else {
                        return false;
                    }
                } else if (curEndTime != null) {
                    if (prevStartTime != null) {
                        return curEndTime.hasSameDate(prevStartTime);
                    } else if (prevEndTime != null) {
                        return curEndTime.hasSameDate(prevEndTime);
                    } else {
                        return false;
                    }
                } else {
                    if(prevStartTime != null || curStartTime != null) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }

        }

        private void setDateHeading(Task task){
            CustomTime startTime = task.getStartTime();
            if (startTime != null) {
                if (startTime.hasDate()) {
                    this._dateLabel.setText(this._df.getDateDisplay(startTime));
                }
            } else {
              CustomTime endTime = task.getEndTime();
                if (endTime != null) {
                    if (endTime.hasDate()) {
                        this._dateLabel.setText(this._df.getDateDisplay(endTime));
                    }
                } else {
                    this._dateLabel.setText("Floating");
                }
            }
        }

        private boolean isAddCommand(Command cmd) {
            return cmd.getInstruction() == Command.Instruction.ADD;
        }

        @Override public boolean equals(Object obj) {
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
                return comparator.equals(this.getItem());
            }

            return false;
        }


    }



    private int obtainNewTaskIndex(){
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
        System.out.println("new task created: " + current.toString() + "index is" + index);
        return  index;
    }


    @Override public Function<KeyEvent, Boolean> getKeyInputInterceptor() {
        return (event -> {

            if (event.getCode().equals(KeyCode.UP) && canScrollUp()) {
                this._viewIndex -= 5;
            } else if (event.getCode().equals(KeyCode.DOWN) && canScrollDown()) {
                this._viewIndex += 5;
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
        return (this._viewIndex - 1) >= 0;
    }

    private boolean canScrollDown() {
        List<VisualTask> viewData = this.getData();
        int size = viewData.size() - (this._viewIndex + 1);
        return size > 0;
    }



}
