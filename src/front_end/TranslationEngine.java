package front_end;

import front_end.ui.MessageDisplayUI;
import front_end.ui.base.UserInterface;
import objects.Command;
import objects.ExecutionResult;
import front_end.ui.base.VisualIdTranslator;
import front_end.ui.base.VisualIndexUI;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Scanner;

/**
 * Created by maianhvu on 5/3/16.
 */
public class TranslationEngine {

    private boolean skipInput_;
    private Scanner inputReader_;
    private CommandParser commandParser_;

    private ExecutionResult currentExecutionResult_;
    private UserInterface currentUI_;
    private VisualIdTranslator currentIdTranslator_;

    public TranslationEngine(boolean skipInput) {
        this.commandParser_ = new CommandParser();

        // Initialize input reader to read from System.in
        this.skipInput_ = skipInput;
        if (!skipInput) {
           this.inputReader_ = new Scanner(System.in) ;
        }
    }

    public TranslationEngine() {
        this(true);
    }

    private static <T> UserInterface<T> constructUserInterface(ExecutionResult executionResult) {
        // Initialize the UserInterface instance
        UserInterface ui = null;

        // Get all available constructors for the UserInterface
        Constructor<?> constructors[] = executionResult.getUiClass().getConstructors();

        // Find appropriate constructor for the user interface
        // and attempt to initialize the user interface with it
        for (Constructor<?> c : constructors) {
            try {
                Constructor<? extends UserInterface<T>> constructor =
                        (Constructor<? extends UserInterface<T>>) c;
                ui = constructor.newInstance(executionResult.getData());
                break;
            } catch (Exception e) {
                continue;
            }
        }

        return ui;
    }

    public Command display(ExecutionResult result) {
        this.displayResultMessages(result);
        this.initializeUI(result);
        this.currentUI_.render();

        // Debug/testing mode, skip input
        if (this.skipInput_) {
            return null;
        }

        return this.waitAndParseInput();
    }

    private void displayResultMessages(ExecutionResult result) {
        // If there are no messages to display, just skip
        if (!result.hasMessage()) {
            return;
        }

        // Create message display UI and attach messages to it
        MessageDisplayUI messageUI = new MessageDisplayUI(result.getMessages());
        messageUI.render();
    }

    /**
     * TODO: Write Java-doc for this
     * Expose as package-level for testing
     *
     * @param executionResult
     */
    void initializeUI(ExecutionResult executionResult) {
        this.currentExecutionResult_ = executionResult;

        // Instantiate new User Interface from executionResult data
        currentUI_ = constructUserInterface(this.currentExecutionResult_);
        assert (currentUI_ != null);

        // If the executionResult class is classified under visual index UI
        // we provide the translation engine with a Visual ID Mapping
        if (VisualIndexUI.class.isAssignableFrom(this.currentExecutionResult_.getUiClass())) {
            assert (this.currentExecutionResult_.getData() instanceof List);

            this.currentIdTranslator_ = new VisualIdTranslator((List) executionResult.getData());

            // Assign the visual tuple to executionResult
            ((VisualIndexUI) currentUI_).setVisualTupleList(this.currentIdTranslator_.getVisualTupleList());
        }
    }

    private Command waitAndParseInput() {
        assert(this.skipInput_ == false);
        String rawCommandString = this.inputReader_.nextLine();
        Command command = this.commandParser_.parseCommand(rawCommandString);
        return command;
    }

    /**
     * Package-level methods exposed for testing purposes
     */
    ExecutionResult getCurrentExecutionResult() {
        return this.currentExecutionResult_;
    }

    UserInterface getCurrentUI() {
        return this.currentUI_;
    }

    VisualIdTranslator getCurrentIdTranslator() {
        return this.currentIdTranslator_;
    }
}
