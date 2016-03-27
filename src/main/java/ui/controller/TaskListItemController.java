package ui.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Pair;
import shared.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Tio on 3/23/2016.
 */
public class TaskListItemController {
    @FXML private Label _indexLabel;
    @FXML private Label _taskNameLabel;
    @FXML private Label _timeLabel;

    private Pair<Integer, Task> _data;
    private final static String DATE_FORMAT = "EE";
    private DateTimeFormatter _timeFormat = DateTimeFormatter.ofPattern(DATE_FORMAT);

    // method is called after the fxml file is loadded
    @FXML private void Initialize() {
    }

    public void setData(Pair<Integer,Task> pTask){
        assert (pTask != null);
        _data = pTask;
    }

    public void constructDisplay(){
        assert (_data != null);



        Task t = _data.getValue();
        LocalDateTime startTime = t.getStartTime();
        LocalDateTime endTime = t.getEndTime();
        _timeFormat = DateTimeFormatter.ofPattern(DATE_FORMAT);

        String day = startTime.format(_timeFormat);
        String id = _data.getKey().toString();
        String tName = t.getTaskName();

        _indexLabel.setText(id);
        _taskNameLabel.setText(tName);
        _timeLabel.setText(day);

    }
}
