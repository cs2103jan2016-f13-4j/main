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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;
import shared.ApplicationContext;
import shared.Resources;
import skeleton.UserInterfaceSpec;
import ui.controller.CommandBarController;
import ui.controller.HeaderBarController;
import ui.controller.NotificationToastController;
import ui.view.View;

import java.util.function.Function;

/**
 *
 * UserInterface is a singleton class which construct the General User Interface structure and define most
 * of the components behavior. The only changing element in this class is the viewWrapper which display the
 * view constructed from the View object. Other than that the stage, and its other children stage is all persistent.
 *
 * @@author Mai Anh Vu
 */
public class UserInterface implements UserInterfaceSpec {

    /**
     * Constants
     */
    private static final String[] SOURCES_FONT = {
            "Lato-Bold",
            "Lato-Regular",
            "Lato-Light"
    };
    private static final double SIZE_FONT_DEFAULT = 16.0;
    private static final String STYLE_CLASS_CONTAINER_MAIN = "sub-container";
    private static final double HEIGHT_MAIN_CONTAINER_MIN = 450.0;
    private static final double HEIGHT_MAIN_CONTAINER_MAX = 450.0;
    private static final double OFFSET_HIDE = 500.0;

    private static final int DURATION_ANIM_TRANSITION = 250;
    private static final int DURATION_ANIM_STOP = 5000;

    private static final Duration TIMELINE_INCOMING = Duration.millis(DURATION_ANIM_TRANSITION);
    private static final Duration TIMELINE_HANGING  = Duration.millis(DURATION_ANIM_TRANSITION + DURATION_ANIM_STOP);
    private static final Duration TIMELINE_OUTGOING = Duration.millis(
            DURATION_ANIM_TRANSITION + DURATION_ANIM_STOP + DURATION_ANIM_TRANSITION
    );

    private static final int INDEX_X = 0;
    private static final int INDEX_Y = 1;


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
    private HeaderBarController _headerBarController;
    private CommandBarController _commandBarController;

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
     * This method will set up the GUI and its component. initialize() should only be called once when UserInterface is first
     * instantiated.
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

        this.registerHeaderBar();
        this.registerViewContainer();
        this.registerCommandBar();

        this.registerNotificationToast();
    }

    private void initializeFonts() {
        for (String font : SOURCES_FONT) {
            Font.loadFont(Resources.sharedResources().getFontUrl(font), SIZE_FONT_DEFAULT);
        }
    }

    private void setRootView() {
        this._rootView = Resources.sharedResources().getComponent("Window");
        Scene rootScene = new Scene(this._rootView);
        this._primaryStage.setScene(rootScene);
        this._primaryStage.setTitle("Your MOM");
        this._primaryStage.setResizable(false);
    }

    /**
     * This method will display the GUI.
     * show() should only be once called after the primary components of user interface, excluding the View,
     * has been constructed.
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
     * this method set the behavior for the commandInput Box when receiving input from the user
     *
     * @param onCommandInput the function that defines the actions taken after receiving input
     */
    @Override
    public void setOnCommandInputHandler(Function<String, Void> onCommandInput) {
        this._commandInputHandler = onCommandInput;
    }

    private void registerHeaderBar() {
        Pair<AnchorPane, HeaderBarController> headerBarMetadata =
                Resources.sharedResources().getComponentAndController("HeaderBar");

        AnchorPane headerBar = headerBarMetadata.getKey();
        this._headerBarController = headerBarMetadata.getValue();

        final double[] originalPosition = new double[2];
        final double[] offset = new double[2];

        // Header bar anchor pane will be used for moving
        headerBar.setOnMousePressed(event -> {
            Stage primaryStage = ApplicationContext.mainContext().getPrimaryStage();

            originalPosition[INDEX_X] = primaryStage.getX();
            originalPosition[INDEX_Y] = primaryStage.getY();

            offset[INDEX_X] = event.getScreenX();
            offset[INDEX_Y] = event.getScreenY();
        });
        headerBar.setOnMouseDragged(event -> {
            Stage primaryStage = ApplicationContext.mainContext().getPrimaryStage();
            double offsetX = event.getScreenX() - offset[INDEX_X];
            double offsetY = event.getScreenY() - offset[INDEX_Y];
            primaryStage.setX(originalPosition[INDEX_X] + offsetX);
            primaryStage.setY(originalPosition[INDEX_Y] + offsetY);
        });

        this._rootView.setTop(headerBar);
    }

    private void registerCommandBar() {
        assert (this._commandInputHandler != null);

        Pair<AnchorPane, CommandBarController> inputMetadata =
                Resources.sharedResources().getComponentAndController("CommandBar");

        assert inputMetadata != null;

        AnchorPane commandBar = inputMetadata.getKey();

        this._commandBarController = inputMetadata.getValue();
        this._commandBarController.setInputSubmissionHandler(
                rawCommand -> this._commandInputHandler.apply(rawCommand)
        );

        this._rootView.setBottom(commandBar);
    }

    private void registerViewContainer() {
        StackPane main = new StackPane();
        main.getStyleClass().add(STYLE_CLASS_CONTAINER_MAIN);
        main.setMinHeight(HEIGHT_MAIN_CONTAINER_MIN);
        main.setMaxHeight(HEIGHT_MAIN_CONTAINER_MAX);

        AnchorPane wrapper = new AnchorPane();
        wrapper.getStyleClass().add(STYLE_CLASS_CONTAINER_MAIN);
        wrapper.setMinHeight(HEIGHT_MAIN_CONTAINER_MIN);
        wrapper.setMaxHeight(HEIGHT_MAIN_CONTAINER_MAX);
        this._viewWrapper = wrapper;
        main.getChildren().add(this._viewWrapper);

        main.setAlignment(Pos.TOP_CENTER);

        this._mainContainer = main;
        this._rootView.setCenter(main);
    }

    private void registerNotificationToast() {
        assert this._mainContainer != null;

        Pair<AnchorPane, NotificationToastController> toastMetadata =
                Resources.sharedResources().getComponentAndController("NotificationToast");

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

    /***
     * The method will retrieve the display component which is constructed by the View Object and
     * attached it to the current display
     *
     * @param view View for the main Container, as of this version, view only has TaskListView object to display list of Task stored
     */
    @Override
    public void render(View view) {
        assert this._viewWrapper != null;
        assert view != null;

        // TODO: Account for similar controller, only update data
        this._viewWrapper.getChildren().clear();
        this._viewWrapper.getChildren().add(view.getComponent());
        this._commandBarController.setKeyInputInterceptor(view.getKeyInputInterceptor());
        this._commandBarController.requestFocus();
    }

    @Override
    public void cleanUp() {
        // Trickle down to controller
        this._commandBarController.cleanUp();
    }

    /***
     * This method will update the notification message following the given input parameter, and play
     * the animation to bring the notification bar on-screen for a given duration.
     *
     * @param notif message to be displayed in the notification bar
     */
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

    @Override
    public void setHeaderTitle(String title) {
        this._headerBarController.setTitle(title);
    }
}
