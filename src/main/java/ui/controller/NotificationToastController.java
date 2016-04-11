package ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * this class control the information stored in the notification bar
 *
 * @@author A0127046L
 */
public class NotificationToastController {

    @FXML
    private AnchorPane _container;
    @FXML
    private Label _label;

    @FXML public void initialize() {

    }

    public void setNotification(String notification) {
        this._label.setText(notification);
    }
}
