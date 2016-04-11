package ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * @@author A0127046L
 */
public class HeaderBarController {

    @FXML private Label _titleLabel;

    @FXML public void initialize() {

    }

    public void setTitle(String title) {
        this._titleLabel.setText(title);
    }

}

