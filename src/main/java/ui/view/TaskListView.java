package ui.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import shared.Resources;
import shared.ViewType;
import storage.Task;

import java.util.List;

/**
 * @@author Antonius Satrio Triatmoko
 */
public class TaskListView extends View {

    private ObservableList _observableList = FXCollections.observableArrayList();

    /**
     * Constructs a new view containing the provided data
     *
     * @param data
     */
    public TaskListView(List<Task> data) {
        super(data);
    }

    @Override protected void buildContent() {
        ListView listView = new ListView();
        listView.setItems(this._observableList);
        listView.setCellFactory(list -> new Item());

        this.setComponent(listView);
    }

    public static class Item extends ListCell<Pair<Integer, Task>> {
        private static final String STRING_NAME_TEMPLATE = "TaskListItem";

        @FXML private AnchorPane _container;
        @FXML private Label _indexLabel;
        @FXML private Label _nameLabel;
        @FXML private Label _dateLabel;

        public Item() {
            super();
            this._container = Resources.getInstance().getComponent(STRING_NAME_TEMPLATE);
            this._indexLabel = (Label) this._container.lookup("#indexLabel");
            this._nameLabel = (Label) this._container.lookup("#nameLabel");
            this._dateLabel = (Label) this._container.lookup("#dateLabel");
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
                this._dateLabel.setText("today"); // TODO: stub

                this.setGraphic(this._container);
            }
        }
    }
}
