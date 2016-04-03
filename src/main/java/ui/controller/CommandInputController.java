package ui.controller;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.FlexiCommandParser;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.reactfx.EventStream;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import shared.Resources;

/**
 * @@author Mai Anh Vu
 */
public class CommandInputController {
    private static final String ID_COMMAND_INPUT = "command-input";
    private static final double PADDING_HORZ_COMMAND_INPUT = 0.0;
    private static final double PADDING_VERT_COMMAND_INPUT = 0.0;
    private static final int DELAY_HIGHLIGHT = 250;
    private static final String STYLE_CLASS_INSTRUCTION = "command__instruction";
    private static final String STYLE_CLASS_PARAM = "command__param";
    private static final String STYLE_CLASS_NORMAL = "command__normal-text";

    @FXML private AnchorPane _commandInputContainer;
    private StyleClassedTextArea _inputField;
    private Function<String, Void> _inputSubmissionHandler;
    private Function<KeyEvent, Boolean> _interceptor;

    private ExecutorService _executor;

    private HashMap<String, String> _instructionStyleClassMap;
    private Pattern _highlightPattern;

    @FXML public void initialize() {
        this.initializeHighlighters();

        this.initializeComponents();

        this.initializeLayout();

        this.initializeHandlers();
    }

    /**
     * Sets up all the different highlighting patterns to be shown by the
     * command input field.
     */
    private void initializeHighlighters() {
        // Prepare the hash map for dealing with finding a command reset
        // instruction
        this._instructionStyleClassMap = new LinkedHashMap<>();

        // Create the instruction highlight pattern
        FlexiCommandParser parser = FlexiCommandParser.getInstance();
        String highlightPatternString = String.join("|", new String[] {
                parser.getInstructionPattern(),
                parser.getTimePattern(),
                parser.getPriorityPattern()
        });

        this._highlightPattern = Pattern.compile(highlightPatternString, Pattern.CASE_INSENSITIVE);
    }

    /**
     * Sets up all the event handlers that will be called upon when the command
     * input field is being interacted with.
     */
    private void initializeHandlers() {
        // Set handlers
        this._inputField.setOnKeyPressed(event -> {

            if (this._interceptor.apply(event)) {
                event.consume();
                return;
            }

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
        richChanges.successionEnds(Duration.ofMillis(DELAY_HIGHLIGHT)).supplyTask(this::computeHighlightingAsync)
                .awaitLatest(richChanges).filterMap(t -> {
                    if (t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        return Optional.empty();
                    }
                }).subscribe(this::applyHighlighting);
    }

    /**
     * Put the appropriate elements in place, visually.
     */
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

    /**
     * Starts up or instantiates all the required components
     */
    private void initializeComponents() {
        this._executor = Executors.newSingleThreadExecutor();
        this._inputField = new StyleClassedTextArea();

        // Set an ID to the input field for easy reference
        this._inputField.setId(ID_COMMAND_INPUT);

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

    public void setKeyInputInterceptor(Function<KeyEvent, Boolean> interceptor) {
        this._interceptor = interceptor;
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = this._inputField.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        try {
            this._executor.execute(task);
        } catch (RejectedExecutionException e) {
            // Do nothing
        }
        return task;
    }

    /**
     * TODO: Write JavaDoc
     * 
     * @param text
     * @return
     */
    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = this._highlightPattern.matcher(text);
        int lastKeywordEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while (matcher.find()) {
            // Fill in previous non-highlighted part
            spansBuilder.add(Collections.singleton(STYLE_CLASS_NORMAL), matcher.start() - lastKeywordEnd);

            // Highlight instruction
            if (matcher.group("INST") != null) {
                // Prepare the list of classes to be added
                spansBuilder.add(
                        Collections.singleton(STYLE_CLASS_INSTRUCTION),
                        matcher.end() - matcher.start()
                );
            } else if (matcher.group("DATE") != null || matcher.group("TIME") != null) {
                spansBuilder.add(
                        Collections.singleton(STYLE_CLASS_PARAM),
                        matcher.end() - matcher.start()
                );
            }

            // Increase last keyword end to the end of the current word
            lastKeywordEnd = matcher.end();
        }

        spansBuilder.add(Collections.singleton(STYLE_CLASS_NORMAL), text.length() - lastKeywordEnd);
        return spansBuilder.create();
    }

    /**
     * TODO: Write JavaDoc
     * 
     * @param highlighting
     */
    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        this._inputField.setStyleSpans(0, highlighting);
    }

    /**
     * Performs the concluding actions before the application concludes.
     */
    public void cleanUp() {
        this._executor.shutdown();
    }
}
