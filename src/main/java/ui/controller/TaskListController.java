package ui.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.util.Pair;
import storage.Task;

/**
 * Created by Tio on 3/23/2016.
 */
public class TaskListController {
    @FXML
    private ListView<Pair<Integer, Task>> _displayList;

    private ObservableList<Pair<Integer, Task>> _taskList;

    @FXML private void ininitialize() {

    }
}
