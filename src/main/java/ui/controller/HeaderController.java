package ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * Created by maianhvu on 20/03/2016.
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
