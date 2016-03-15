package component.front_end;

import component.front_end.ui.ErrorDisplayView;
import component.front_end.ui.core.UserInterface;
import component.front_end.ui.core.UserInterfaceSpec;
import component.front_end.ui.core.View;
import component.front_end.ui.core.VisualIndexView;
import entity.ExecutionResult;
import entity.command.Command;
import entity.command.Instruction;
import skeleton.front_end.CommandParserSpec;
import skeleton.front_end.TranslationEngineSpec;
import skeleton.front_end.VisualIndexMapperSpec;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Created by maianhvu on 6/3/16.
 */
public class TranslationEngine extends TranslationEngineSpec {

    /**
     * Properties
     */
    private final CommandParser commandParser_;
    private final UserInterface userInterface_;

    private View<?> currentView_;
    private VisualIndexMapperSpec currentIndexMapper_;

    /**
     * Constructs a default translation engine.
     */
    public TranslationEngine() {
        this.commandParser_ = new CommandParser();
        this.userInterface_ = new UserInterface();
    }

    /**
     * Returns the main command parser used by this translation engine.
     * @return the main command parser
     */
    @Override protected CommandParserSpec getCommandParser() {
        return this.commandParser_;
    }

    /**
     * Returns the main user interface used by this translation engine.
     * @return the main user interface
     */
    @Override protected UserInterfaceSpec getUserInterface() {
        return this.userInterface_;
    }

    /**
     * Constructs a view to be rendered based on the information
     * gathered from the execution result passed in. Then notifies
     * the user interface to render the constructed view.
     *
     * @param result the execution result to display
     */
    @Override public void display(ExecutionResult<?> result) {
        // Null view, does not do anything
        if (result == null) {
            return;
        }

        this.initializeView(result);
        this.getUserInterface().render(this.currentView_);
    }

    /**
     * Initializes a View using the information gathered from the
     * execution result and set it to the current fields.
     * @param executionResult the result to gather info from
     */
    private void initializeView(ExecutionResult<?> executionResult) {
        ExecutionResult<?> currentExecutionResult_ = executionResult;

        // If the executionResult class is classified under visual index UI
        // we provide the translation engine with a Visual ID Mapping
        if (VisualIndexView.class.isAssignableFrom(executionResult.getViewClass())) {
            assert (executionResult.getData() instanceof List);

            //noinspection unchecked
            this.currentIndexMapper_ = new VisualIndexMapper((List) executionResult.getData());

            // Assign the visual tuple to executionResult
            currentExecutionResult_ = executionResult.transformToVisual(
                    this.currentIndexMapper_.getVisualTupleList()
            );
        }

        // Instantiate new User Interface from executionResult data
        this.currentView_ = constructView(currentExecutionResult_);

        assert (this.currentView_ != null);
    }

    /**
     * Constructs a View instance using the constructor provided from the execution result
     * and its data.
     *
     * @param executionResult the execution result passed in
     * @return the view constructed
     */
    private static View<?> constructView(ExecutionResult<?> executionResult) {
        // Initialize the View instance
        View<?> view = null;

        // Get all available constructors for the View
        Constructor<?> constructors[] = executionResult.getViewClass().getConstructors();

        // Find appropriate constructor for the user interface
        // and attempt to initialize the user interface with it
        for (Constructor<?> c : constructors) {
            try {
                Constructor<? extends View<?>> constructor =
                        (Constructor<? extends View<?>>) c;
                view = constructor.newInstance(executionResult.getData());
                break;
            } catch (Exception e) {
                // Do nothing
            }
        }

        return view;
    }


    /**
     * Prompts the user for the next command string and passes the command string
     * through a command parser to be given to the dispatcher. Will detect commands
     * that are of {@link Instruction} type INVALID or UNRECOGNISED, and intercepts
     * the passing to dispatcher and ask the user to provide input again.
     *
     * @return the command parsed from user input
     */
    @Override public Command getNextCommand() {
        String rawCommandString = this.userInterface_.queryInput();

        Command command = this.getCommandParser().parseCommand(rawCommandString);

        // If the command is either INVALID or UNDEFINED, we immediately capture this command
        // on the front end and let the user specify a new (potentially) valid command. This
        // function is recursive to ensure this process repeats until the user inputs a valid
        // command.
        if (mustInterceptCommand(command)) {
            this.displayFaultyCommandView();
            return this.getNextCommand();
        }

        // If the command instruction has an index that appears on the screen
        // but does not truly reflect the actual index in the back end, attempt
        // to convert it from the visual index (current) to the raw index
        if (shouldApplyVisualIndexMapping(command)) {
            int visualIndex = command.getInstruction().getIndex();
            command.getInstruction().setIndex(
                    this.currentIndexMapper_.translateVisualToRaw(
                            visualIndex
                    ));
        }

        return command;
    }

    private static boolean shouldApplyVisualIndexMapping(Command command) {
        return command.getInstruction().getIndex() != null;
    }

    /**
     * Displays a message to notify the user of invalid or unrecognised command.
     */
    private void displayFaultyCommandView() {
        this.currentView_ = new ErrorDisplayView(
                "Your command is either invalid or unrecognised"
        );
        this.getUserInterface().render(this.currentView_);
    }

    /**
     * Determines if the command is of the instruction type that has to be intercepted.
     * @param command the command to be examined
     * @return whether the command needs to be intercepted
     */
    private static boolean mustInterceptCommand(Command command) {
        Instruction.Type type = command.getInstruction().getType();
        return type == Instruction.Type.INVALID || type == Instruction.Type.UNRECOGNISED;
    }
}