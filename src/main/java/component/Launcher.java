package component;


import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utility.Resources;

/**
 * Created by maianhvu on 8/3/16.
 */
public class Launcher extends Application {

    /**
     * Constants
     */
    public static final String STRING_APP_TITLE = "Your MOM";

    /**
     * Properties
     */
    private BorderPane _rootLayout;
    private Stage _primaryStage;

    /**
     * Main executable
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
//        DispatcherSpec application = new Dispatcher();
//        application.pulse();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this._rootLayout = Resources.getTemplate("MainContainer");

        if (this._rootLayout == null) {
            return;
        }

        primaryStage.setTitle(STRING_APP_TITLE);
        primaryStage.setScene(new Scene(this._rootLayout));
        primaryStage.show();

        this._primaryStage = primaryStage;

        this.displayHeader();
    }

    private void displayHeader() {
        AnchorPane header = Resources.getTemplate("Header");

        if (header == null) {
            return;
        }

        this._rootLayout.setTop(header);
    }

    private void displayCommandBar() {

    }

}
