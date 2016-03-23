package shared;

import javafx.stage.Stage;

/**
 * @@author Mai Anh Vu
 */
public class ApplicationContext {

    /**
     * Singleton instance
     */
    private static ApplicationContext instance;

    /**
     * Properties
     */
    private Stage _primaryStage;

    /**
     * Private singleton constructor
     */
    public ApplicationContext() {
    }

    /**
     * TODO: Write JavaDoc
     *
     * @return
     */
    private static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    /**
     * TODO: Write JavaDoc
     *
     * @return
     */
    public static Stage getPrimaryStage() {
        assert (instance != null);
        assert (instance._primaryStage != null);
        return getInstance()._primaryStage;
    }

    /**
     * TODO: Write JavaDoc
     *
     * @param primaryStage
     */
    public static void setPrimaryStage(Stage primaryStage) {
        getInstance()._primaryStage = primaryStage;
    }
}
