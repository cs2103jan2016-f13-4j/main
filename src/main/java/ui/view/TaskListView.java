package ui.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import shared.Resources;
import shared.Task;
import ui.controller.TaskListController;

/**
 * @@author Antonius Satrio Triatmoko
 */
public class TaskListView extends View {
    /**
     * Constants
     */
    private final int MAXIMUM_DISPLAY_SIZE = 6;

    /**
     * Properties
     */
    private ObservableList _observableList;
    private TaskListController _listControl;
    private List<Pair<Integer, Task>> _displayList;
    private int _viewIndex;

    /**
     * Constructs a new view containing the provided data
     *
     * @param data
     */
    public TaskListView(List<Pair<Integer, Task>> data) {
        super(data);
    }

    @Override protected void buildContent() {
        _displayList = constructDisplayList();
        _observableList = FXCollections.observableArrayList(_displayList);

        ListView listView = Resources.getInstance().getComponent("TaskList");
        listView.setItems(this._observableList);
        listView.setCellFactory(list -> new Item());

        this.setComponent(listView);
    }

    public static class Item extends ListCell<Pair<Integer, Task>> {
        private static final String STRING_NAME_TEMPLATE = "TaskListItem";
        private static final String STRING_DATE_PATTERN = "EE";

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
                LocalDateTime startTime = task.getStartTime();
                this._indexLabel.setText(Integer.toString(index));
                this._nameLabel.setText(task.getTaskName());

                // Optional date time to support floating tasks
                this._dateLabel.setText(startTime != null ? _df.format(startTime) : ""); // TODO:
                                                                                         // stub

                this.setGraphic(this._container);
            }
        }
    }

    private List<Pair<Integer, Task>> constructDisplayList() {
        List<Pair<Integer, Task>> temp = new ArrayList<>();
        List<Pair<Integer, Task>> viewData = this.getData();

        if (viewData.size() > MAXIMUM_DISPLAY_SIZE) {
            for (int i = 0; i < MAXIMUM_DISPLAY_SIZE; i++) {
                temp.add(viewData.get(i));
            }
            return temp;
        } else {
            return viewData;
        }

    }

    @Override public Function<KeyEvent, Boolean> getKeyInputInterceptor() {
        return (event -> {
            if (!event.getCode().isArrowKey()) {
                return false;
            }

            // TODO: Handle event here
            System.out.println(event.getCode());
            event.consume();
            return true;
        });
    }
}
