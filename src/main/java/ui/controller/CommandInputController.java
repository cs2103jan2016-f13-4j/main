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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by maianhvu on 19/03/2016.
 */
public class CommandInputController {
    private static final double PADDING_HORZ_COMMAND_INPUT = 12.0;
    private static final double PADDING_VERT_COMMAND_INPUT = 18.0;

    /**
     * Highlighting patterns
     */
    private static final String[] INSTRUCTIONS = new String[] {
            "add", "display", "delete", "edit", "exit"
            };
    private static final String PATTERN_INSTRUCTION = "^\\b(" + String.join("|", INSTRUCTIONS) + ")\\b";
    private static final Pattern PATTERN_INPUT = Pattern.compile(
            "(?<INST>" + PATTERN_INSTRUCTION + ")"
    );

    @FXML
    private AnchorPane _commandInputContainer;
    private StyleClassedTextArea _inputField;
    private Function<String, Void> _inputSubmissionHandler;

    private ExecutorService _executor;

    @FXML public void initialize() {
        this.initializeComponents();
        this.initializeLayout();
        this.initializeHandlers();
    }

    private void initializeHandlers() {
        // Set handlers
        this._inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                assert _inputSubmissionHandler != null;
                // Throw event handler up to UserInterface
                _inputSubmissionHandler.apply(_inputField.getText());
                // Clear the field
                _inputField.clear();
                event.consume();
            }
        });

        // Set highlighting
        EventStream<?> richChanges = this._inputField.richChanges();
        richChanges
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(richChanges)
                .filterMap(t -> {
                    if (t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);
    }

    private void initializeLayout() {
        String stylesheet = Resources.getInstance().getStylesheet("CommandInput");
        this._inputField.getStylesheets().add(stylesheet);

        this._inputField.getStyleClass().add("command-input__field");
        this._inputField.setWrapText(false);

        // Arrange the input field correctly
        AnchorPane.setLeftAnchor(this._inputField, PADDING_HORZ_COMMAND_INPUT);
        AnchorPane.setRightAnchor(this._inputField, PADDING_HORZ_COMMAND_INPUT);
        AnchorPane.setTopAnchor(this._inputField, PADDING_VERT_COMMAND_INPUT);
        AnchorPane.setBottomAnchor(this._inputField, 0.0);
    }

    private void initializeComponents() {
        this._executor = Executors.newSingleThreadExecutor();
        this._inputField = new StyleClassedTextArea();
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
        this._executor.execute(task);
        return task;
    }

    /**
     * TODO: Write JavaDoc
     * @param text
     * @return
     */
    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN_INPUT.matcher(text);
        int lastKeywordEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                    matcher.group("INST") != null ? "keyword" : null;
            assert styleClass != null; /* never happens */
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKeywordEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKeywordEnd = matcher.end();
        }

        spansBuilder.add(Collections.emptyList(), text.length() - lastKeywordEnd);
        return spansBuilder.create();
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        this._inputField.setStyleSpans(0, highlighting);
    }

    public void cleanUp() {
        this._executor.shutdown();
    }
}
