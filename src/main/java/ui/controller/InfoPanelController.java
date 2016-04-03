package ui.controller;

import javafx.animation.*;
import javafx.beans.value.WritableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 * @@author Mai Anh Vu
 */
public class InfoPanelController {
    @FXML private AnchorPane _mainContainer;

    @FXML public void initialize() {}

    public void hide() {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(true);
        final WritableValue<Double> height = new WritableHeight(this._mainContainer);
        final KeyValue keyValue = new KeyValue(height, 0.0, Interpolator.EASE_OUT);
        final KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private class WritableHeight implements WritableValue<Double> {
        private final Region _region;

        WritableHeight(Region region) {
            this._region = region;
        }

        @Override
        public Double getValue() {
            return this._region.getHeight();
        }

        @Override
        public void setValue(Double value) {
            this._region.setPrefHeight(value);
        }
    }
}

