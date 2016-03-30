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
public class InfoPanelController {

    private static final Function<Void, LocalDateTime> DATE_CALENDAR_INITIAL = aVoid -> LocalDateTime.now();

    @FXML private Canvas _calendarCanvas;

    @FXML public void initialize() {
        this.drawCalendar(DATE_CALENDAR_INITIAL.apply(null));
    }

    private void drawCalendar(LocalDateTime date) {

    }
}
