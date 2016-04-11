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
    private boolean _isTestingMode;

    /**
     * Private singleton constructor
     */
    private ApplicationContext() {
        this._isTestingMode = false;
    }

    /**
     * Returns the main application instance.
     *
     * @return
     */
    public static ApplicationContext mainContext() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    /**
     * Returns the primary stage used by this JavaFX application.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        assert (instance != null);
        assert (instance._primaryStage != null);
        return this._primaryStage;
    }

    /**
     * Manually determines the primary stage to be used by this application.
     *
     * @param primaryStage the stage to be used
     */
    public void setPrimaryStage(Stage primaryStage) {
        assert primaryStage != null;
        this._primaryStage = primaryStage;
    }

    /**
     * Returns whether the current application stack is running on test mode.
     * @return whether application is in test mode
     */
    public boolean isTestingMode() {
        return this._isTestingMode;
    }

    /**
     * Manually determines if testing mode is to be activated.
     * @param testing whether to activate testing mode
     */
    public void setTestingMode(boolean testing) {
        this._isTestingMode = testing;
    }
}
