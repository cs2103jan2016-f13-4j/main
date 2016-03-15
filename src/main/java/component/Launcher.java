package component;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
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

        // Constraint
        primaryStage.setMinWidth(this._rootLayout.getMinWidth());
        primaryStage.setMaxWidth(this._rootLayout.getMaxWidth());
        primaryStage.setMinHeight(this._rootLayout.getMinHeight());

        this._primaryStage = primaryStage;
        this.initializeHeader();
        this.initializeCommandBox();

        primaryStage.show();
    }

    private void initializeHeader() {
        AnchorPane header = Resources.getTemplate("Header");

        if (header == null) {
            return;
        }

        // Drop shadow for header
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(0);
        shadow.setOffsetY(5.0);
        shadow.setColor(Color.GRAY);

        this._rootLayout.setTop(header);
    }

    private void initializeCommandBox() {
        AnchorPane commandBox = Resources.getTemplate("CommandBox");

        if (commandBox == null) {
            return;
        }

        this._rootLayout.setBottom(commandBox);
    }

}
