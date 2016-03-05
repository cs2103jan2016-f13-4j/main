package front_end;

import front_end.ui.base.UserInterface;
import front_end.ui.base.View;
import front_end.ui.base.VisualIdTranslator;
import front_end.ui.base.VisualIndexUI;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Created by maianhvu on 5/3/16.
 */
public class TranslationEngine {

    private CommandParser commandParser_;
    private View currentView_;
    private UserInterface currentUI_;
    private VisualIdTranslator currentIdTranslator_;

    public TranslationEngine() {
        this.commandParser_ = new CommandParser();
    }

    private static <T> UserInterface<T> constructUserInterface(View view) {
        // Initialize the UserInterface instance
        UserInterface ui = null;

        // Get all available constructors for the UserInterface
        Constructor<?> constructors[] = view.getUiClass().getConstructors();

        // Find appropriate constructor for the user interface
        // and attempt to initialize the user interface with it
        for (Constructor<?> c : constructors) {
            try {
                Constructor<? extends UserInterface<T>> constructor =
                        (Constructor<? extends UserInterface<T>>) c;
                ui = constructor.newInstance(view.getData());
                break;
            } catch (Exception e) {
                continue;
            }
        }

        return ui;
    }

    public void display(View view) {
        this.initializeUI(view);
        this.currentUI_.render();
    }

    /**
     * TODO: Write Java-doc for this
     * Expose as package-level for testing
     *
     * @param view
     */
    void initializeUI(View view) {
        this.currentView_ = view;

        // Instantiate new User Interface from view data
        currentUI_ = constructUserInterface(this.currentView_);
        assert (currentUI_ != null);

        // If the view class is classified under visual index UI
        // we provide the translation engine with a Visual ID Mapping
        if (VisualIndexUI.class.isAssignableFrom(this.currentView_.getUiClass())) {
            assert (this.currentView_.getData() instanceof List);

            this.currentIdTranslator_ = new VisualIdTranslator((List) view.getData());

            // Assign the visual tuple to view
            ((VisualIndexUI) currentUI_).setVisualTupleList(this.currentIdTranslator_.getVisualTupleList());
        }
    }

    /**
     * Package-level methods exposed for testing purposes
     */
    View getCurrentView() {
        return this.currentView_;
    }

    UserInterface getCurrentUI() {
        return this.currentUI_;
    }

    VisualIdTranslator getCurrentIdTranslator() {
        return this.currentIdTranslator_;
    }
}
