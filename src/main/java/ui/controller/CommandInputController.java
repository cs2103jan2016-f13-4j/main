package ui.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import logic.CommandParser;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.reactfx.EventStream;
import shared.Command;
import shared.Resources;

import java.time.Duration;
import java.util.*;
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

    @FXML
    private AnchorPane _commandInputContainer;
    private StyleClassedTextArea _inputField;
    private Function<String, Void> _inputSubmissionHandler;

    private ExecutorService _executor;

    private HashMap<String, String> _instructionStyleClassMap;
    private Pattern _highlightPattern;

    @FXML public void initialize() {
        this.initializeHighlighters();

        this.initializeComponents();

        this.initializeLayout();

        this.initializeHandlers();
    }

    private void initializeHighlighters() {
        // Prepare the hash map for dealing with finding a command reset instruction
        this._instructionStyleClassMap = new LinkedHashMap<>();

        // Get keyword data from Command Parser
        LinkedHashMap<String, Command.Instruction> instructionMap = CommandParser.constructInstructionMap();

        // Prepare a list of highlighted instruction words
        List<String> highlightList = new ArrayList<>();

        instructionMap.entrySet().stream()
                // Turn into a pair of instruction keyword and the style class applied to it
                // based on the instruction's original keyword. Refer to the CommandParser
                // for the keywords
                .map(entry -> {
                    String instruction = entry.getKey();
                    String styleClass = entry.getValue().toString().toLowerCase();

                    // Also add this instruction to highlight list
                    highlightList.add(instruction);

                    return new Pair<>(instruction, styleClass);
                })
                // Point each of this keyword to the correct class and store them
                // inside a Hash Map for easy referral later
                .forEach(pair -> this._instructionStyleClassMap.put(
                        pair.getKey(),
                        pair.getValue())
                );

        // Create the instruction highlight pattern
        String instructionPattern = buildInstructionHighlightPattern(highlightList);

        this._highlightPattern = Pattern.compile(
                "(?<INST>" + instructionPattern + ")"
        );
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
    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = this._highlightPattern.matcher(text);
        int lastKeywordEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        String instructionClass = null;
        while (matcher.find()) {
            // Fill in previous non-highlighted part
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKeywordEnd);

            // Highlight instruction
            if (instructionClass == null && matcher.group("INST") != null) {
                // Prepare the list of classes to be added
                List<String> classes = new ArrayList<>();
                classes.add("instruction");

                // For individual classes
                instructionClass = this._instructionStyleClassMap.get(matcher.group("INST"));
                if (instructionClass != null) {
                    classes.add("instruction--" + instructionClass);
                }

                // Fill in the classes
                spansBuilder.add(
                        classes,
                        matcher.end() - matcher.start()
                );
            }
            // Highlight parameters
//            else if (matcher.group("PARAM") != null) {
//                Collection<String> classes = new ArrayList<>();
//                classes.add("param");
//                if (instructionClass != null) {
//                    classes.add("param__" + instructionClass);
//                }
//                spansBuilder.add(classes, matcher.end() - matcher.start());
//            }

            // Increase last keyword end to the end of the current word
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

    private static String buildInstructionHighlightPattern(List<String> instructions) {
        StringBuilder patternBuilder = new StringBuilder();
        patternBuilder.append("^\\b(");
        for (int i = 0; i < instructions.size(); i++) {
            if (i != 0) patternBuilder.append("|");
            patternBuilder.append(instructions.get(i));
        }
        patternBuilder.append(")\\b");
        return patternBuilder.toString();
    }

}
