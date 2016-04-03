package ui;

import exception.ExceptionHandler;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;
import shared.ApplicationContext;
import shared.Resources;
import skeleton.UserInterfaceSpec;
import ui.controller.CommandInputController;
import ui.controller.InfoPanelController;
import ui.controller.NotificationToastController;
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
            "Lato-Black",
            "Lato-Bold",
            "Lato-Italic",
            "Lato-Regular",
            "Lato-Light"
    };
    private static final double SIZE_FONT_DEFAULT = 16.0;
    private static final String STYLE_CLASS_CONTAINER_MAIN = "sub-container";
    private static final double HEIGHT_MAIN_CONTAINER_MIN = 300.0;
    private static final double HEIGHT_MAIN_CONTAINER_MAX = 430.0;
    private static final double OFFSET_HIDE = 400.0;

    private static final int DURATION_ANIM_TRANSITION = 250;
    private static final int DURATION_ANIM_STOP = 3000;

    private static final Duration TIMELINE_INCOMING = Duration.millis(DURATION_ANIM_TRANSITION);
    private static final Duration TIMELINE_HANGING  = Duration.millis(DURATION_ANIM_TRANSITION + DURATION_ANIM_STOP);
    private static final Duration TIMELINE_OUTGOING = Duration.millis(
            DURATION_ANIM_TRANSITION + DURATION_ANIM_STOP + DURATION_ANIM_TRANSITION
    );


    /**
     * Singleton instance
     */
    private static UserInterface instance;

    /**
     * Properties
     */
    private Function<String, Void> _commandInputHandler;
    private Stage _primaryStage;
    private VBox _rootView;
    private InfoPanelController _infoPanelController;
    private CommandInputController _commandInputController;

    private StackPane _mainContainer;
    private AnchorPane _viewWrapper;
    private AnchorPane _notification;
    private NotificationToastController _notificationController;

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
        assert this._commandInputHandler != null;

        // Set primary stage
        this._primaryStage = ApplicationContext.mainContext().getPrimaryStage();
        try {
            this._primaryStage.initStyle(StageStyle.UNDECORATED);
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }

        this.initializeFonts();
        this.setRootView();
        this.registerCommandInput();
        this.registerInfoPanel();
        this.registerViewContainer();
        this.registerNotificationToast();
    }

    private void initializeFonts() {
        for (String font : SOURCES_FONT) {
            Font.loadFont(Resources.getInstance().getFontUrl(font), SIZE_FONT_DEFAULT);
        }
    }

    private void setRootView() {
        this._rootView = Resources.getInstance().getComponent("Window");
        Scene rootScene = new Scene(this._rootView);
        this._primaryStage.setScene(rootScene);
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
    }

    private void registerInfoPanel() {
        Pair<AnchorPane, InfoPanelController> infoPanelMetadata =
                Resources.getInstance().getComponentAndController("InfoPanelWrapper");

        AnchorPane infoPanelWrapper = infoPanelMetadata.getKey();
        // TODO: Delete this line to show info panel
        infoPanelWrapper.setClip(new Rectangle(0,0));
        this._infoPanelController = infoPanelMetadata.getValue();

        this._rootView.getChildren().add(infoPanelWrapper);
    }

    private void registerCommandInput() {
        assert (this._commandInputHandler != null);

        Pair<AnchorPane, CommandInputController> inputMetadata =
                Resources.getInstance().getComponentAndController("CommandInputWrapper");

        assert inputMetadata != null;

        AnchorPane commandInputWrapper = inputMetadata.getKey();
        this._rootView.getChildren().add(commandInputWrapper);

        this._commandInputController = inputMetadata.getValue();
        this._commandInputController.setInputSubmissionHandler(
                rawCommand -> this._commandInputHandler.apply(rawCommand)
        );
    }

    private void registerViewContainer() {
        StackPane main = new StackPane();
        main = new StackPane();
        main.getStyleClass().add(STYLE_CLASS_CONTAINER_MAIN);
        main.setMinHeight(HEIGHT_MAIN_CONTAINER_MIN);
        main.setMaxHeight(HEIGHT_MAIN_CONTAINER_MAX);

        AnchorPane wrapper = new AnchorPane();
        wrapper.getStyleClass().add(STYLE_CLASS_CONTAINER_MAIN);
        wrapper.setMinHeight(HEIGHT_MAIN_CONTAINER_MIN);
        wrapper.setMaxHeight(HEIGHT_MAIN_CONTAINER_MAX);
        this._viewWrapper = wrapper;
        main.getChildren().add(this._viewWrapper);

        main.setAlignment(Pos.BOTTOM_CENTER);

        this._mainContainer = main;
        this._rootView.getChildren().add(this._mainContainer);
    }

    private void registerNotificationToast() {
        assert this._mainContainer != null;

        Pair<AnchorPane, NotificationToastController> toastMetadata =
                Resources.getInstance().getComponentAndController("NotificationToast");

        AnchorPane notificationContainer = toastMetadata.getKey();
        notificationContainer.setLayoutY(
                this._mainContainer.getHeight() - notificationContainer.getHeight() - 20.0
        );
        notificationContainer.toFront();
        notificationContainer.setTranslateY(-20.0);

        // Hide this outside of view for now
        notificationContainer.setTranslateX(-OFFSET_HIDE);

        this._notification = notificationContainer;
        this._notificationController = toastMetadata.getValue();
        this._mainContainer.getChildren().add(notificationContainer);
    }

    @Override
    public void render(View view) {
        assert this._viewWrapper != null;
        assert view != null;

        // TODO: Account for similar controller, only update data
        this._viewWrapper.getChildren().clear();
        this._viewWrapper.getChildren().add(view.getComponent());
        this._commandInputController.setKeyInputInterceptor(view.getKeyInputInterceptor());
    }

    @Override
    public void cleanUp() {
        // Trickle down to controller
        this._commandInputController.cleanUp();
    }

    @Override
    public void showNotification(String notif) {
        assert this._notification != null;
        assert this._notificationController != null;

        // Set title first
        this._notificationController.setNotification(notif);

        final KeyValue initial = new KeyValue(
                this._notification.translateXProperty(),
                -OFFSET_HIDE
        );
        final KeyValue incoming = new KeyValue(
                this._notification.translateXProperty(),
                0,
                Interpolator.EASE_BOTH
        );
        final KeyValue outgoing = new KeyValue(
                this._notification.translateXProperty(),
                OFFSET_HIDE,
                Interpolator.EASE_BOTH
        );

        // Construct animation keyframes
        final KeyFrame initialFrame = new KeyFrame(Duration.ZERO, initial);
        final KeyFrame incomingFrame = new KeyFrame(TIMELINE_INCOMING, incoming);
        final KeyFrame hangingFrame = new KeyFrame(TIMELINE_HANGING, incoming);
        final KeyFrame outgoingFrame = new KeyFrame(TIMELINE_OUTGOING, outgoing);

        // Create the timeline and display
        final Timeline timeline = new Timeline(initialFrame, incomingFrame, hangingFrame, outgoingFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }
}
