package todobuddy.views;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class RootLayoutController extends Application {
    
    @FXML
    private Label Task1; 
    @FXML
    private Label Task2; 
    @FXML
    private Label Task3; 
    @FXML
    private Label Task4; 
    
    @FXML
    private Label Detail1; 
    @FXML
    private Label Detail2; 
    @FXML
    private Label Detail3; 
    @FXML
    private Label Detail4;
    
    @FXML 
    private Circle circle1;
    @FXML 
    private Circle circle2;
    @FXML 
    private Circle circle3;
    @FXML 
    private Circle circle4;
    
	@Override
	public void start(Stage primaryStage) {
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
