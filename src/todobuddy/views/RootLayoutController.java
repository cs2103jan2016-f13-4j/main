package todobuddy.views;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import todobuddy.Task;



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
    
    private static final int HIGH_PRIORITY = 2; 
    private static final int MEDIUM_PRIORITY = 1; 
    private static final int LOW_PRIORITY = 0;
    
    private Label[] _labelList = new Label[8];
    
	@Override
	public void start(Stage primaryStage) {
		
	}
	
	public void ShowTask(int start,Task[] taskContainers){

	    for(int i = 0; i < 4 ; i++){
	        _labelList[ 0 + 2*i ] = new Label(taskContainers[start + i].getNameProperty());
	        _labelList[ 1 + 2*i ] = new Label(taskContainers[start + i].getDetailString());
	        }
	    taskLabel1 = _labelList[0];
	    detailLabel1 = _labelList[1];
	    
	    taskLabel2 = _labelList[2];
	    detailLabel2 = _labelList[3];
	    
	    taskLabel3 = _labelList[4];
        detailLabel3 = _labelList[5];
        
        taskLabel4 = _labelList[6];
        detailLabel4 = _labelList[7];
	    
	}
	 
	
}
