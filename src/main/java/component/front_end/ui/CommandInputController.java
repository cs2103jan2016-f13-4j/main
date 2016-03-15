package component.front_end.ui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.wellbehaved.event.EventHandlerHelper;


/**
 * Created by maianhvu on 15/03/2016.
 */
public class CommandInputController {
    @FXML
    private BorderPane _inputContainer;

    @FXML
    public void initialize() {
        StyleClassedTextArea textArea = new StyleClassedTextArea();

        final EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    System.out.println("Enter pressed!");
                    // TODO: Stub, change this

                    event.consume();
                }
            }
        };

        textArea.setOnKeyPressed(handler);

        this._inputContainer.setCenter(textArea);
    }
}
