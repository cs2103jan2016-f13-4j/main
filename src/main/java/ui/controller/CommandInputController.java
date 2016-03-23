package ui.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.reactfx.EventStream;
import shared.Resources;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @@author Mai Anh Vu
 */
public class CommandInputController {
    private static final double PADDING_HORZ_COMMAND_INPUT = 12.0;
    private static final double PADDING_VERT_COMMAND_INPUT = 18.0;
    private static final int DELAY_HIGHLIGHT = 250;

    /**
     * Highlighting patterns
     */
    private static final String[] INSTRUCTIONS = new String[] {
            "add", "display", "delete", "edit", "mark", "exit"
            };
    private static final String PATTERN_INSTRUCTION = "^\\b(" + String.join("|", INSTRUCTIONS) + ")\\b";
    private static final String PATTERN_PARAMETER = "\\b\\w+:";
    private static final Pattern PATTERN_INPUT = Pattern.compile(
            "(?<INST>" + PATTERN_INSTRUCTION + ")"
            + "|(?<PARAM>" + PATTERN_PARAMETER + ")"
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
                String rawCommand = _inputField.getText();
                _inputSubmissionHandler.apply(rawCommand);

                // Clear the field
                _inputField.clear();
                event.consume();
            }
        });

        // Set highlighting
        EventStream<?> richChanges = this._inputField.richChanges();
        richChanges
                .successionEnds(Duration.ofMillis(DELAY_HIGHLIGHT))
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

        String instructionClass = null;
        while (matcher.find()) {
            // Fill in previous non-highlighted part
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKeywordEnd);

            // Highlight instruction
            if (instructionClass == null && matcher.group("INST") != null) {
                instructionClass = matcher.group("INST");
                spansBuilder.add(Collections.singleton(instructionClass), matcher.end() - matcher.start());
            }
            // Highlight parameters
            else if (matcher.group("PARAM") != null) {
                Collection<String> classes = new ArrayList<>();
                classes.add("param");
                if (instructionClass != null) {
                    classes.add("param__" + instructionClass);
                }
                spansBuilder.add(classes, matcher.end() - matcher.start());
            }
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
