package component.front_end;

import component.front_end.ui.core.UserInterface;
import component.front_end.ui.core.UserInterfaceSpec;
import component.front_end.ui.core.View;
import component.front_end.ui.core.VisualIndexView;
import entity.ExecutionResult;
import entity.command.Command;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Created by maianhvu on 6/3/16.
 */
public class TranslationEngine extends TranslationEngineSpec {
    private CommandParser commandParser_;
    private UserInterface userInterface_;

    private ExecutionResult<?> currentExecutionResult_;
    private View<?> currentView_;
    private VisualIndexMapperSpec currentIndexMapper_;

    public TranslationEngine() {
        this.commandParser_ = new CommandParser();
        this.userInterface_ = new UserInterface();
    }

    @Override
    protected CommandParserSpec getCommandParser() {
        return this.commandParser_;
    }

    @Override
    protected UserInterfaceSpec getUserInterface() {
        return this.userInterface_;
    }

    @Override
    public void display(ExecutionResult<?> result) {
        this.initializeView(result);
        this.getUserInterface().render(this.currentView_);
    }

    private void initializeView(ExecutionResult<?> executionResult) {
        this.currentExecutionResult_ = executionResult;

        // Instantiate new User Interface from executionResult data
        this.currentView_ = constructView(this.currentExecutionResult_);
        assert (this.currentView_ != null);

        // If the executionResult class is classified under visual index UI
        // we provide the translation engine with a Visual ID Mapping
        if (VisualIndexView.class.isAssignableFrom(this.currentExecutionResult_.getViewClass())) {
            assert (this.currentExecutionResult_.getData() instanceof List);

            this.currentIndexMapper_ = new VisualIndexMapper((List) executionResult.getData());

            // Assign the visual tuple to executionResult
            ((VisualIndexView) this.currentView_).setVisualTupleList(this.currentIndexMapper_.getVisualTupleList());
        }
    }

    private static <T> View<T> constructView(ExecutionResult<?> executionResult) {
        // Initialize the View instance
        View view = null;

        // Get all available constructors for the View
        Constructor<?> constructors[] = executionResult.getViewClass().getConstructors();

        // Find appropriate constructor for the user interface
        // and attempt to initialize the user interface with it
        for (Constructor<?> c : constructors) {
            try {
                Constructor<? extends View<T>> constructor =
                        (Constructor<? extends View<T>>) c;
                view = constructor.newInstance(executionResult.getData());
                break;
            } catch (Exception e) {
                continue;
            }
        }

        return view;
    }


    @Override
    public Command getCommand() {
        String rawCommandString = this.userInterface_.queryInput();
        return this.getCommandParser().parseCommand(rawCommandString);
    }
}
