package launcher;

import javafx.application.Application;
import javafx.stage.Stage;
import logic.Dispatcher;
import shared.ApplicationContext;
import skeleton.DispatcherSpec;

/**
 * @@author A0127046L
 */
public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application, initialising the full stack.
     * @param primaryStage the stage to be used for the application
     * @throws Exception
     */
    @Override public void start(Stage primaryStage) throws Exception {
        ApplicationContext.mainContext().setPrimaryStage(primaryStage);

        DispatcherSpec dispatcher = Dispatcher.getInstance();
        dispatcher.initialise();
        dispatcher.start();
    }
}
