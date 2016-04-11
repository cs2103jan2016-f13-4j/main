package helpers;

import javafx.scene.control.Control;
import javafx.stage.Stage;
import logic.Dispatcher;
import shared.ApplicationContext;
import skeleton.DispatcherSpec;

/**
 * @@author A0127046L
 */
public class IntegrationTestHelper {

    public static void startTestApplication(Stage primaryStage) {
        // Register the main stage and flag the application as testing
        ApplicationContext context = ApplicationContext.mainContext();
        context.setPrimaryStage(primaryStage);
        context.setTestingMode(true);

        // Start dispatcher
        DispatcherSpec dispatcher = Dispatcher.getInstance();
        dispatcher.initialise();
        dispatcher.start();
    }

    public static void shutdownTestApplication() {
        Dispatcher.getInstance().shutdown();
    }

    public static <T extends Control> T findComponent(String query) {
        try {
            return (T) ApplicationContext.mainContext().getPrimaryStage().getScene()
                    .lookup(query);
        } catch (ClassCastException|NullPointerException e) {
            return null;
        }
    }
}
