package ui;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import shared.ApplicationContext;
import shared.Resources;
import skeleton.UserInterfaceSpec;
import ui.controller.CommandInputController;
import ui.controller.HeaderController;
import ui.view.View;

import java.util.function.Function;

/**
 * @@author Mai Anh Vu
 */
public class UserInterface implements UserInterfaceSpec {

    /**
     * Constants
     */
    private static final String[] SOURCES_FONT = {
            "Lato-Bold",
            "Lato-Italic",
            "Lato-Regular"
    };
    private static final double SIZE_FONT_DEFAULT = 16.0;
    private static final String STYLE_CLASS_CONTAINER_MAIN = "sub-container";

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
    private CommandInputController _commandInputController;

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
    @Override
    public void initialize() {
        assert (ApplicationContext.mainContext().getPrimaryStage() != null);

        // Set primary stage
        this._primaryStage = ApplicationContext.mainContext().getPrimaryStage();
        this._primaryStage.getIcons().add(Resources.getInstance().getImage("mom.png"));

        this.initializeFonts();

        this.setRootView();
        this.registerHeader();
        this.registerViewContainer();
    }

    private void initializeFonts() {
        for (String font : SOURCES_FONT) {
            Font.loadFont(Resources.getInstance().getFontUrl(font), SIZE_FONT_DEFAULT);
        }
    }

    private void setRootView() {
        this._rootView = Resources.getInstance().getComponent("Window");

        this._primaryStage.setScene(new Scene(this._rootView));
        this._primaryStage.setTitle("Your MOM");
        this._primaryStage.setResizable(false);
    }

    /**
     * TODO: Write JavaDoc
     */
    @Override
    public void show() {
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
    @Override
    public void setOnCommandInputHandler(Function<String, Void> onCommandInput) {
        this._commandInputHandler = onCommandInput;
        this.registerCommandInput();
    }

    private void registerHeader() {
        Pair<AnchorPane, HeaderController> headerMetadata =
                Resources.getInstance().getComponentAndController("HeaderWrapper");

        AnchorPane headerWrapper = headerMetadata.getKey();
        this._headerController = headerMetadata.getValue();

        this._rootView.setTop(headerWrapper);
    }

    private void registerCommandInput() {
        assert (this._commandInputHandler != null);

        Pair<AnchorPane, CommandInputController> inputMetadata =
                Resources.getInstance().getComponentAndController("CommandInputWrapper");

        assert inputMetadata != null;

        AnchorPane commandInputWrapper = inputMetadata.getKey();
        this._rootView.setBottom(commandInputWrapper);

        this._commandInputController = inputMetadata.getValue();
        this._commandInputController.setInputSubmissionHandler(
                rawCommand -> this._commandInputHandler.apply(rawCommand)
        );
    }

    private void registerViewContainer() {
        this._mainContainer = new AnchorPane();
        this._mainContainer.getStyleClass().add(STYLE_CLASS_CONTAINER_MAIN);
        this._rootView.setCenter(this._mainContainer);
    }

    @Override
    public void render(View view) {
        assert this._mainContainer != null;
        assert view != null;
        // TODO: Account for similar controller, only update data
        this._mainContainer.getChildren().clear();
        this._mainContainer.getChildren().add(view.getComponent());
        this._commandInputController.setKeyInputInterceptor(view.getKeyInputInterceptor());
    }

    @Override
    public void cleanUp() {
        // Trickle down to controller
        this._commandInputController.cleanUp();
    }

    @Override
    public void setHeader(String title) {
        this._headerController.setHeader(title);
    }
}
