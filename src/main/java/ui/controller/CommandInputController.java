package ui.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.reactfx.EventStream;
import shared.Instruction;
import shared.Resources;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * Created by maianhvu on 19/03/2016.
 */
public class CommandInputController {
    private static final double PADDING_HORZ_COMMAND_INPUT = 12.0;
    private static final double PADDING_VERT_COMMAND_INPUT = 18.0;

    @FXML
    private AnchorPane _commandInputContainer;
    private StyleClassedTextArea _inputField;
    private Function<String, Void> _inputSubmissionHandler;

//    private final String[] _keywordsInstruction;
//    private ExecutorService _executor;

    public CommandInputController() {
        super();

//        this._executor = Executors.newSingleThreadExecutor();
//        this._keywordsInstruction = (String[]) Arrays.stream(Instruction.Type.values())
//                .filter(type -> type.keyword != null)
//                .map(type -> type.keyword)
//                .toArray();
    }


    @FXML public void initialize() {
        this._inputField = new StyleClassedTextArea();

        String stylesheet = Resources.getInstance().getStylesheet("CommandInput");
        this._inputField.getStylesheets().add(stylesheet);

        this._inputField.getStyleClass().add("command-input__field");
        this._inputField.setWrapText(false);

        // Arrange the input field correctly
        AnchorPane.setLeftAnchor(this._inputField, PADDING_HORZ_COMMAND_INPUT);
        AnchorPane.setRightAnchor(this._inputField, PADDING_HORZ_COMMAND_INPUT);
        AnchorPane.setTopAnchor(this._inputField, PADDING_VERT_COMMAND_INPUT);
        AnchorPane.setBottomAnchor(this._inputField, 0.0);

        // Set handlers
        this._inputField.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {
                assert _inputSubmissionHandler != null;
                // Throw event handler up to UserInterface
                _inputSubmissionHandler.apply(_inputField.getText());
                // Clear the field
                _inputField.clear();
                event.consume();
            } else {

            }
        });

        // Set highlighting
//        EventStream<?> richChanges = this._inputField.richChanges();
//        richChanges
//                .successionEnds(Duration.ofMillis(500))
//                .supplyTask(this::computeHighlightingAsync)
//                .awaitLatest(richChanges)
//                .filterMap(t -> {
//                    if (t.isSuccess()) {
//                        return Optional.of(t.get());
//                    } else {
//                        return Optional.empty();
//                    }
//                })
//                .subscribe(this::applyHighlighting);

        this._commandInputContainer.getChildren().add(this._inputField);
    }

    /**
     * TODO: Write JavaDoc
     *
     * @return
     */
    public void setInputSubmissionHandler(Function<String, Void> handler) {
        this._inputSubmissionHandler = handler;
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = this._inputField.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
//        this._executor.execute(task);
        return task;
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        StyleSpansBuilder<Collection<String>> spansBuilder =
                new StyleSpansBuilder<>();

        int firstWordStart = 0;
        int firstWordEnd = text.indexOf(' ');
        String instruction = text.substring(firstWordStart, firstWordEnd);

        boolean match = false;
        for (String keyword : Collections.singleton("Hello")) {
            if (instruction.equals(keyword)) {
                match = true;
                break;
            }
        }

        if (match) {
            spansBuilder.add(Collections.singleton("red"), firstWordEnd - firstWordStart);
        }

        spansBuilder.add(Collections.emptyList(), text.length() - firstWordEnd);
        return spansBuilder.create();
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        this._inputField.setStyleSpans(0, highlighting);
    }

    public void cleanUp() {
//        this._executor.shutdown();
    }
}
