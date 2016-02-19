package todobuddy.views;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import todobuddy.MainApplication;
import todobuddy.Task;



public class RootLayoutController  {
    
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
    
    
    private MainApplication mainz = null;
    private static final int HIGH_PRIORITY = 2; 
    private static final int MEDIUM_PRIORITY = 1; 
    private static final int LOW_PRIORITY = 0;
    
    private Label[] _labelList = new Label[8];
    
    @FXML
	private void initialize(){
	    
	}
	public void showTask(int start,ObservableList<Task> taskContainers){

	    for(int i = 0; i < 4 ; i++){
	        _labelList[ 0 + 2*i ] = new Label(taskContainers.get(start + i).getNameProperty());
	        _labelList[ 1 + 2*i ] = new Label(taskContainers.get(start + i).getDetailString());
	        }
	    taskLabel1.setText(_labelList[0].getText());
	    detailLabel1.setText(_labelList[1].getText());
	    
	    taskLabel2.setText(_labelList[2].getText());
	    detailLabel2.setText(_labelList[3].getText());
	    
	    taskLabel3.setText(_labelList[4].getText());
        detailLabel3.setText(_labelList[5].getText());
        
        taskLabel4.setText(_labelList[6].getText());
        detailLabel4.setText(_labelList[7].getText());
	    
	}
	

	
}
