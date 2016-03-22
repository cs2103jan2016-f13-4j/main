package ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import org.fxmisc.richtext.StyleClassedTextArea;

/**
 * Created by maianhvu on 19/03/2016.
 */
public class CommandInputController {
    private static final double PADDING_HORZ_COMMAND_INPUT = 12.0;
    private static final double PADDING_VERT_COMMAND_INPUT = 13.0;

    @FXML
    private AnchorPane _commandInputContainer;
    private StyleClassedTextArea _inputField;

    @FXML public void initialize() {
        this._inputField = new StyleClassedTextArea();

        this._inputField.getStyleClass().add("command-input__field");
        this._inputField.setWrapText(false);

        // Arrange the input field correctly
        AnchorPane.setLeftAnchor(this._inputField, PADDING_HORZ_COMMAND_INPUT);
        AnchorPane.setRightAnchor(this._inputField, PADDING_HORZ_COMMAND_INPUT);
        AnchorPane.setTopAnchor(this._inputField, PADDING_VERT_COMMAND_INPUT);
        AnchorPane.setBottomAnchor(this._inputField, PADDING_VERT_COMMAND_INPUT);

        this._commandInputContainer.getChildren().add(this._inputField);
    }

    /**
     * TODO: Write JavaDoc
     *
     * @return
     */
    public StyleClassedTextArea getInputField() {
        return this._inputField;
    }
}
