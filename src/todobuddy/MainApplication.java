package todobuddy;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApplication extends Application {

    /**
     * Constants
     */
    private static final String LAYOUT_ROOT_LAYOUT = "views/RootLayout.fxml";
    private static final String LAYOUT_INPUT_PANE = "views/InputPane.fxml";
    private static final String WINDOW_TITLE = "TodoBuddy";

    /**
     * Properties
     */
    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(WINDOW_TITLE);

        try {
            initializeRootLayout();
            displayInputPane();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeRootLayout() throws IOException {
        // Load root layout from fxml file.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApplication.class.getResource(LAYOUT_ROOT_LAYOUT));
        rootLayout = (BorderPane) loader.load();

        // Show the scene containing the root layout.
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void displayInputPane() throws IOException {
        // Load person overview.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApplication.class.getResource(LAYOUT_INPUT_PANE));
        BorderPane inputPane = (BorderPane) loader.load();

        // Set person overview into the center of root layout.
        rootLayout.setBottom(inputPane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
