package ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.fxmisc.richtext.StyleClassedTextArea;
import shared.ApplicationContext;
import shared.Resources;
import skeleton.UserInterfaceSpec;
import ui.controller.CommandInputController;
import ui.controller.HeaderController;
import ui.view.View;

import java.util.function.Function;

/**
 * @author maianhvu
 */
public class UserInterface implements UserInterfaceSpec {

    /**
     * Constants
     */
    private static final String[] SOURCES_FONT = {
            "Lato-Bold.ttf",
            "Lato-Italic.ttf",
            "Lato-Regular.ttf"
    };
    private static final double SIZE_FONT_DEFAULT = 16.0;
    private static final String STYLE_CLASS_CONTAINER_MAIN = "container--main";

    /**
     * Singleton instance
     */
    private static UserInterface instance;

    /**
     * Properties
     */
    private Function<String, Void> _commandInputHandler;
    private Stage _primaryStage;
    private BorderPane _rootView;
    private HeaderController _headerController;

    private AnchorPane _mainContainer;

    private UserInterface() {
    }

    public static UserInterface getInstance() {
        if (instance == null) {
            instance = new UserInterface();
        }
        return instance;
    }

    /**
     * TODO: Write JavaDoc
     */
    @Override public void initialize() {
        // Set primary stage
        this._primaryStage = ApplicationContext.getPrimaryStage();
        assert (this._primaryStage == null);

        //this.initializeFonts();

        this.setRootView();
        this.registerHeader();
        this.registerCommandInput();
        this.registerViewContainer();
    }

    private void initializeFonts() {
        for (String font : SOURCES_FONT) {
            Font.loadFont(Resources.getFontUrl(font), SIZE_FONT_DEFAULT);
        }
    }

    private void setRootView() {
        this._rootView = Resources.getComponent("Window");

        this._primaryStage.setScene(new Scene(this._rootView));
        this._primaryStage.setTitle("Your MOM");
        this._primaryStage.setResizable(false);
    }

    /**
     * TODO: Write JavaDoc
     */
    @Override public void show() {
        // Make sure stage and command input handler are both set
        assert (this._primaryStage != null);
        assert (this._commandInputHandler != null);

        // Initialize first view

        this._primaryStage.show();
    }

    /**
     * TODO: Write JavaDoc
     *
     * @param onCommandInput
     */
    @Override public void setOnCommandInputHandler(Function<String, Void> onCommandInput) {
        this._commandInputHandler = onCommandInput;
    }

    private void registerHeader() {
        Pair<AnchorPane, HeaderController> headerMetadata =
                Resources.getComponentAndController("HeaderWrapper");

        AnchorPane headerWrapper = headerMetadata.getKey();
        this._headerController = headerMetadata.getValue();

        this._rootView.setTop(headerWrapper);
    }

    private void registerCommandInput() {
        assert (this._commandInputHandler != null);

        Pair<AnchorPane, CommandInputController> inputMetadata =
                Resources.getComponentAndController("CommandInputWrapper");

        assert inputMetadata != null;

        AnchorPane commandInputWrapper = inputMetadata.getKey();
        this._rootView.setBottom(commandInputWrapper);

        CommandInputController controller = inputMetadata.getValue();
        final StyleClassedTextArea inputField = controller.getInputField();
        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Push command
                String rawCommand = inputField.getText();
                _commandInputHandler.apply(rawCommand);

                // Clear text and prevent event from trickling down
                inputField.clear();
                event.consume();
            }
        });
    }

    private void registerViewContainer() {
        this._mainContainer = new AnchorPane();
        this._mainContainer.getStyleClass().add(STYLE_CLASS_CONTAINER_MAIN);
        this._rootView.setCenter(this._mainContainer);
    }

    @Override public void render(View view) {
        assert this._mainContainer != null;
        assert view != null;
        // TODO: Account for similar controller, only update data
        this._mainContainer.getChildren().clear();
        this._mainContainer.getChildren().add(view.getComponent());
    }

    private void setTitle(String title) {
        this._headerController.setHeader(title);
    }
}
