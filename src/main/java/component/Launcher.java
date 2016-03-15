package component;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import skeleton.DispatcherSpec;
import utility.Resources;

/**
 * Created by maianhvu on 8/3/16.
 */
public class Launcher extends Application {

    /**
     * Main executable
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        DispatcherSpec application = new Dispatcher(primaryStage);
//        application.pulse();
    }

}
