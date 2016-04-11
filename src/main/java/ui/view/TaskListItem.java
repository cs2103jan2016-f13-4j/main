package ui.view;

import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import shared.Command;
import shared.CustomTime;
import shared.Resources;
import shared.Task;
import ui.controller.DateFormatterHelper;

/**
 * The TaskListItem class is the extension of ListCell class which allows us to customize the display content of the list.
 * There are two possible .xml files that can be loaded depending on Date and Time information stored by the task.Event Task
 * (Task with both start time and end time)will load TaskListItemDated.fxml while other type of task will
 * use TaskListItemNormal.fxml .
 *
 * DateFormatterHelper is used to help determining the date and time presentation.
 */
public class TaskListItem extends ListCell<VisualTask> {
    private static final double STRING_HIGHLIGHT_OPACITY = .31;
    private static final String STRING_NAME_TEMPLATE_WITH_DATE = "TaskListItemDated";
    private static final String STRING_NAME_TEMPLATE_NO_DATE = "TaskListItemNormal";
    private static final String STRING_HIGHLIGHT_COLOR = "#FBFF74";

    @FXML
    private AnchorPane _container;
    @FXML private Label _indexLabel;
    @FXML private Label _nameLabel;
    @FXML private Label _priorityLabel;
    @FXML private Label _timeLabel;
    @FXML private Label _dateLabel;
    @FXML private Rectangle _highlight;

    private DateFormatterHelper _df = new DateFormatterHelper();
    private Command _lastCommand;
    private int _newTaskIndex;
    private boolean _canHighlight = true;

    public TaskListItem(Command lastCommand, int newTaskIndex) {
        super();

        this._lastCommand = lastCommand ;
        this._newTaskIndex = newTaskIndex;

    }

    /***
     * This method link the TaskListItem Class to the corresponding  .fxml file used to display the list content.
     * Having reference to the .fxml file component give us the ability to edit its component attribute to set up
     * our intended behaviour
     *
     * @param task the task that is going to be checked. Event Task will call TasklistItemDouble.fxml  whereas
     *             other type of task will call TaskListItemNormal.fxml.
     */
    private void updateGraphicPointer(Task task) {
        if (isSameDate(task)) {
            this._container = Resources.sharedResources().getComponent(STRING_NAME_TEMPLATE_NO_DATE);
            assert this._container != null;

        } else {
            this._container = Resources.sharedResources().getComponent(STRING_NAME_TEMPLATE_WITH_DATE);
            assert this._container != null;

            this._dateLabel = (Label) this._container.lookup("#_dateLabel");
            assert this._dateLabel != null;
        }

        this._indexLabel = (Label) this._container.lookup("#_indexLabel");
        this._nameLabel = (Label) this._container.lookup("#_taskNameLabel");
        this._highlight = (Rectangle) this._container.lookup("#_highlightEffect");
        this._timeLabel = (Label) this._container.lookup("#_timeLabel");
        this._priorityLabel = (Label) this._container.lookup("#_priorityIndicator");

        assert this._indexLabel != null;
        assert this._nameLabel != null;
        assert this._highlight != null;
        assert this._timeLabel != null;
    }

    /***
     * Update the corresponding ListView cell with the appropriate content.
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
            if (this._timeLabel.getText().trim().isEmpty()) {
                // Shift the name label off
                this._nameLabel.setTranslateY(8.0);
            }

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
            this._priorityLabel.setText("High");
        } else if (task.getPriority() == Task.Priority.MEDIUM) {
            this._priorityLabel.setText("Medium");
        } else if (task.getPriority() == Task.Priority.LOW) {
            this._priorityLabel.setText("Low");
        }
    }

    /**
     * The item sometimes is displayed using a reused ListCell Object, instead of a completely new ListCell Object instantiated.
     * So previous applied style and effect might still persist. Hence, there is a need to reset the StyleClass applied to the cell
     * before applying a new one when the list of item is updated.
     */
    private void resetEffect() {
        this.getStyleClass().clear();
        this.getStyleClass().add("cell");
        this.getStyleClass().add("indexed-cell");
        this.getStyleClass().add("list-cell");

    }


    /***
     * prepare and play the highlight animation to show new task
     * The highlight effect will only be played the first time the task is displayed.
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
     * This method prepare the time String to be displayed by utilising DateFormatHelper
     * For more detail on how the time stored within the task is procsessed, @see DateFormatterHelper.java
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

    /***
     * this helper method check if the specified task has the same date as its previous task.
     *
     * @param curTask
     * @return
     */
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

        if(obj instanceof TaskListItem){
            TaskListItem another = (TaskListItem) obj;
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