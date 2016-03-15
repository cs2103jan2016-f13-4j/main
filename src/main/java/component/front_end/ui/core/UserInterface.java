package component.front_end.ui.core;

import component.front_end.ui.CommandPromptView;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utility.Resources;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Created by maianhvu on 6/3/16.
 */
public class UserInterface extends UserInterfaceSpec {

    /**
     * Constants
     */
    public static final String STRING_APP_TITLE = "Your MOM";

    /**
     * Properties
     */
    private BorderPane _rootLayout;
    private Stage _primaryStage;

    public UserInterface(Stage primaryStage) {
        this._primaryStage = primaryStage;

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

        this._rootLayout.setTop(header);
    }

    private void initializeCommandBox() {
        AnchorPane commandBox = Resources.getTemplate("CommandBox");

        if (commandBox == null) {
            return;
        }

        this._rootLayout.setBottom(commandBox);
    }


    @Override
    public void render(View viewToRender) {

    }

    @Override
    public String queryInput() {
        return null;
    }
}
