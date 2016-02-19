package todobuddy.views;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class RootLayoutController extends Application {
    
    @FXML
    private Label taskLabel1; 
    @FXML
    private Label taskLabel2; 
    @FXML
    private Label taskLabel3; 
    @FXML
    private Label taskLabel4; 
    
    @FXML
    private Label detailLabel1; 
    @FXML
    private Label detailLabel2; 
    @FXML
    private Label detailLabel3; 
    @FXML
    private Label detailLabel4;
    
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
	
	public void ShowTask(int start,int range,Task[] taskContainers){
	    
	    
	}
	 
	public static void main(String[] args) {
		launch(args);
	}
}
