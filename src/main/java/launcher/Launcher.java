package launcher;

import javafx.application.Application;
import javafx.stage.Stage;
import logic.Dispatcher;
import shared.ApplicationContext;
import skeleton.DispatcherSpec;

/**
 * @@author Mai Anh Vu
 */
public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) throws Exception {
        ApplicationContext.setPrimaryStage(primaryStage);

        DispatcherSpec dispatcher = Dispatcher.getInstance();
        dispatcher.initialise();
        dispatcher.start();
    }
}
