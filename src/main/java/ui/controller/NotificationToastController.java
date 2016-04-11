package ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * this class control the information stored in the notification bar
 *
 * Created by maianhvu on 03/04/2016.
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
