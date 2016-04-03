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
     * TODO: Write JavaDoc
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
     * TODO: Write JavaDoc
     *
     * @return
     */
    public Stage getPrimaryStage() {
        assert (instance != null);
        assert (instance._primaryStage != null);
        return this._primaryStage;
    }

    /**
     * TODO: Write JavaDoc
     *
     * @param primaryStage
     */
    public void setPrimaryStage(Stage primaryStage) {
        assert primaryStage != null;
        this._primaryStage = primaryStage;
    }

    public boolean isTestingMode() {
        return this._isTestingMode;
    }

    public void setTestingMode(boolean testing) {
        this._isTestingMode = testing;
    }
}
