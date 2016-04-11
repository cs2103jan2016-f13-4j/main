package ui.controller;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Function;
import java.util.regex.Matcher;

import logic.CommandParser;
import logic.parser.RegexUtils;
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
 * @@author A0127046L
 */
public class CommandBarController {
    private static final String ID_COMMAND_INPUT = "command-input";
    private static final double PADDING_HORZ_COMMAND_INPUT = 12.0;
    private static final double PADDING_VERT_COMMAND_INPUT = 14.0;
    private static final int DELAY_HIGHLIGHT = 100;
    private static final String STYLE_CLASS_INSTRUCTION = "command__instruction";
    private static final String STYLE_CLASS_START_DATE = "command__start-date";
    private static final String STYLE_CLASS_END_DATE = "command__end-date";
    private static final String STYLE_CLASS_START_TIME = "command__start-time";
    private static final String STYLE_CLASS_END_TIME = "command__end-time";
    private static final String STYLE_CLASS_NORMAL = "command__normal-text";
    private static final String STYLE_CLASS_PRIORITY = "command__priority";

    @FXML private AnchorPane _commandBarContainer;

    private StyleClassedTextArea _inputField;
    private Function<String, Void> _inputSubmissionHandler;
    private Function<KeyEvent, Boolean> _interceptor;

    private ExecutorService _executor;

    // Regular expressions
    private String _instructionPattern;
    private String _startTimePattern;
    private String _endTimePattern;
    private String _priorityPattern;

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
        // Create the instruction highlight pattern
        CommandParser parser = CommandParser.getInstance();
        this._instructionPattern = parser.getInstructionPattern();
        this._startTimePattern   = parser.getStartTimePattern();
        this._endTimePattern     = parser.getEndTimePattern();
        this._priorityPattern    = parser.getPriorityPattern();
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
        String stylesheet = Resources.sharedResources().getStylesheet("CommandBar");
        this._inputField.getStylesheets().add(stylesheet);

        this._inputField.getStyleClass().add("command-input__field");
        this._inputField.setWrapText(false);

        // Arrange the input field correctly
        AnchorPane.setLeftAnchor(this._inputField, PADDING_HORZ_COMMAND_INPUT);
        AnchorPane.setRightAnchor(this._inputField, PADDING_HORZ_COMMAND_INPUT);
        AnchorPane.setTopAnchor(this._inputField, PADDING_VERT_COMMAND_INPUT);
        AnchorPane.setBottomAnchor(this._inputField, -5.0);
    }

    /**
     * Starts up or instantiates all the required components
     */
    private void initializeComponents() {
        this._executor = Executors.newSingleThreadExecutor();
        this._inputField = new StyleClassedTextArea();

        // Set an ID to the input field for easy reference
        this._inputField.setId(ID_COMMAND_INPUT);

        this._commandBarContainer.getChildren().add(this._inputField);
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
        int lastKeywordEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        //-------------------------------------------------------------------
        // I. Instruction
        //-------------------------------------------------------------------
        Matcher instructionMatcher = RegexUtils.caseInsensitiveMatch(
                this._instructionPattern,
                text
        );
        // Must have a valid instruction before the rest gets highlighted
        if (instructionMatcher.find()) {
            // Fill in previous non-highlighted part
            spansBuilder.add(Collections.singleton(STYLE_CLASS_NORMAL),
                    instructionMatcher.start() - lastKeywordEnd);
            // Highlight instruction
            if (instructionMatcher.group(CommandParser.MATCHER_GROUP_INSTRUCTION) != null) {
                spansBuilder.add(
                        Collections.singleton(STYLE_CLASS_INSTRUCTION),
                        instructionMatcher.end() - instructionMatcher.start()
                );
            }
            // Update last keyword end
            lastKeywordEnd = instructionMatcher.end();
        } else {
            // Instruction not found, abort highlighting
            // Find in the rest
            spansBuilder.add(Collections.singleton(STYLE_CLASS_NORMAL), text.length() - lastKeywordEnd);
            return spansBuilder.create();
        }

        //-------------------------------------------------------------------
        // II. Start Time
        //-------------------------------------------------------------------
        Matcher startTimeMatcher = RegexUtils.caseInsensitiveMatch(
                this._startTimePattern,
                text
        );
        lastKeywordEnd = this.highlightTime(lastKeywordEnd, spansBuilder, startTimeMatcher, true);

        //-------------------------------------------------------------------
        // III. End Time
        //-------------------------------------------------------------------
        Matcher endTimeMatcher = RegexUtils.caseInsensitiveMatch(
                this._endTimePattern,
                text
        );
        lastKeywordEnd = this.highlightTime(lastKeywordEnd, spansBuilder, endTimeMatcher, false);

        //-------------------------------------------------------------------
        // IV. Priority
        //-------------------------------------------------------------------
        Matcher priorityMatcher = RegexUtils.caseInsensitiveMatch(
                this._priorityPattern,
                text
        );
        if (priorityMatcher.find()) {
            // Get group first
            int matcherStart = priorityMatcher.start(CommandParser.MATCHER_GROUP_PRIORITY);
            int matcherEnd = priorityMatcher.end(CommandParser.MATCHER_GROUP_PRIORITY);
            // Fill in previous non-highlighted part
            spansBuilder.add(Collections.singleton(STYLE_CLASS_NORMAL),
                    matcherStart - lastKeywordEnd);

            // Highlight priority
            spansBuilder.add(
                    Collections.singleton(STYLE_CLASS_PRIORITY),
                    matcherEnd - matcherStart
            );
            // Update last keyword end
            lastKeywordEnd = matcherEnd;
        }

        spansBuilder.add(Collections.singleton(STYLE_CLASS_NORMAL), text.length() - lastKeywordEnd);
        return spansBuilder.create();
    }

    private int highlightTime(int lastKeywordEnd, StyleSpansBuilder<Collection<String>> spansBuilder,
                              Matcher timeMatcher, boolean isStart) {
        String dateStyle = isStart ? STYLE_CLASS_START_DATE : STYLE_CLASS_END_DATE;
        String timeStyle = isStart ? STYLE_CLASS_START_TIME : STYLE_CLASS_END_TIME;

        if (timeMatcher.find(lastKeywordEnd)) {
            String matcherGroup = Arrays.asList(
                    CommandParser.MATCHER_GROUP_RELATIVE_TIME,
                    CommandParser.MATCHER_GROUP_DAY_OF_WEEK,
                    CommandParser.MATCHER_GROUP_DATE
            ).stream().filter(groupName -> timeMatcher.group(groupName) != null)
                    .findFirst().orElse(null);

            if (matcherGroup != null) {
                int matcherStart = timeMatcher.start(matcherGroup);
                int matcherEnd = timeMatcher.end(matcherGroup);

                // Fill in previous non-highlighted part
                spansBuilder.add(Collections.singleton(STYLE_CLASS_NORMAL),
                        matcherStart - lastKeywordEnd);

                // Highlight start date
                spansBuilder.add(
                        Collections.singleton(dateStyle),
                        matcherEnd - matcherStart
                );
                // Update last keyword end
                lastKeywordEnd = matcherEnd;

                if (timeMatcher.group(CommandParser.MATCHER_GROUP_TIME_OF_DAY) != null) {
                    int timeStart = timeMatcher.start(CommandParser.MATCHER_GROUP_TIME_OF_DAY);
                    int timeEnd = timeMatcher.end(CommandParser.MATCHER_GROUP_TIME_OF_DAY);
                    // Fill in previous non-highlighted part
                    spansBuilder.add(Collections.singleton(STYLE_CLASS_NORMAL),
                            timeStart - matcherEnd);
                    // Highlight start time
                    spansBuilder.add(
                            Collections.singleton(timeStyle),
                            timeEnd - timeStart
                    );

                    // Update last keyword end
                    lastKeywordEnd = timeEnd;
                }
            }
        }
        return lastKeywordEnd;
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

    public void requestFocus() {
        this._inputField.requestFocus();
    }
}
