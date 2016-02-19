package todobuddy;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This task represents an abstraction of a task.
 * The controller works primarily with this abstraction.
 * The model and view must be capable of using this abstraction for communication with the controller (regarding tasks).
 * 
 * @author thenaesh
 *
 */
public class Task {
    private StringProperty _name; 
    private StringProperty _venue; 
    private StringProperty _day; 
    private IntegerProperty _priority; 
    
    public Task() {
        this("beAlive","NUS","Everyday",2);
    }
    
    public Task(String name,String venue, String day, int priority){
        this._name = new SimpleStringProperty(name);
        this._venue = new SimpleStringProperty(venue);
        this._day = new SimpleStringProperty(day);
        this._priority = new SimpleIntegerProperty(priority);
    }
    
    public String getNameProperty() {
        return _name.get(); 
    }
    
    public String getVenueProperty() {
        return _venue.get(); 
    }
    
    public String getDayProperty() {
        return _day.get(); 
    }
    
    public int getPriorityProperty() {
        return _priority.get(); 
    }
    
    public String getDetailString(){
        String result = "";
        if(this.getVenueProperty() != null){
            result = result + "Venue: " + this.getVenueProperty();
        } 
        if(this.getDayProperty() != null){
            result = result + "Date: " + this.getDayProperty(); 
        }
        
        if(result.equals("")){
            result = null; 
        }
        return result; 
    }
    
    
    
}
