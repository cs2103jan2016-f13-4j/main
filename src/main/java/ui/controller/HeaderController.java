package ui.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * @@author Mai Anh Vu
 */
public class HeaderController {
    @FXML public ImageView _iconView;
    @FXML public Label _headerLabel;

    @FXML public void initialize() {

    }

    public void setHeader(String header) {
        this._headerLabel.setText(header);
    }
}

