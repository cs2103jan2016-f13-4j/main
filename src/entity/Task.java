package entity;

import java.time.LocalDateTime;

import component.back_end.storage.PrimaryKey;
import component.back_end.storage.PrimaryKeySpec;
import component.back_end.storage.Relation;
import component.back_end.storage.TaskPrimaryKey;

/**
 * 
 * created by thenaesh on Mar 5, 2016
 *
 */

public class Task extends Relation {
    private String taskName_; 
    private String taskDescription_; 
    private LocalDateTime timeBegin_; 
    private LocalDateTime timeEnd_;
    //private int priority_; 
    private PrimaryKey<TaskPrimaryKey> pKey_;
    
    // constructor
    public Task(String name, String detail,LocalDateTime start, LocalDateTime finish){
        this.taskName_ = name; 
        this.taskDescription_ = detail; 
        this.timeBegin_ = start; 
        this.timeEnd_ = finish;
        
        // Set primary key upon creation
        this.updatePrimaryKey();
    }
    
    @Override
    public PrimaryKeySpec<?> getPrimaryKey() {
        return this.pKey_;
    }
    
    public void updatePrimaryKey() {
        TaskPrimaryKey key = new TaskPrimaryKey(this.taskName_, this.timeBegin_);
        PrimaryKey<TaskPrimaryKey> primaryKey = new PrimaryKey<>(key);
        this.pKey_ = primaryKey;
    }

    // Getters
    public String getName(){
        return this.taskName_;
    }
    
    public String getDescription(){
        return this.taskDescription_;
    }
    
    public LocalDateTime getStartingTime(){
        return this.timeBegin_;
    }
    
    public LocalDateTime getEndingTime(){
        return this.timeEnd_;
    }
    
    // Setters
    public void setName(String newName){
        this.taskName_ = newName; 
        this.updatePrimaryKey(); // need to update primary key after changing task name
    }
    
    public void setDescription(String newDescription){
        this.taskDescription_ = newDescription; 
    }
    
    public void setStartingTime(LocalDateTime newStartTime){
        this.timeBegin_ = newStartTime;
        this.updatePrimaryKey(); // need to update primary key after changing starting time
    }
    
    public void setEndingTime(LocalDateTime newEndingTime){
        this.timeEnd_ = newEndingTime; 
    }
}
