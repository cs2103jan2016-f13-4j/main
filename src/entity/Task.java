package entity;

import java.time.LocalDateTime;

/**
 * 
 * created by thenaesh on Mar 5, 2016
 *
 */
public class Task {
    private String taskName_; 
    private String taskDescription_; 
    private LocalDateTime timeBegin_; 
    private LocalDateTime timeEnd_;
    //private int priority_; 
    // constructor
    public Task(String name, String detail,LocalDateTime start, LocalDateTime finish){
        this.taskName_ = name; 
        this.taskDescription_ = detail; 
        this.timeBegin_ = start; 
        this.timeEnd_ = finish; 
    }
    
    // functions to access object property
    public String getTask(){
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
    
    public void setName(String newName){
        this.taskName_ = newName; 
    }
    
    public void setDescription(String newDescription){
        this.taskDescription_ = newDescription; 
    }
    
    public void setStartingTime(LocalDateTime newStartTime){
        this.timeBegin_ = newStartTime; 
    }
    
    public void setEndingTime(LocalDateTime newEndingTime){
        this.timeEnd_ = newEndingTime; 
    }
}
