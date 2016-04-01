package ui.controller;

import shared.Task;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Antonius Satrio Triatmoko on 4/1/2016.
 */
public class DateFormatterHelper {
    /**Constant**/
    private final String DATE_FORMAT = "EE";
    private final String HOUR_FORMAT = "ha";
    private final String DATE_YESTERDAY = "YTD";
    private final String DATE_TODAY = "TDY";
    private final String DATE_TOMMOROW = "TMR";
    private final String DATE_SAME_WEEK = "This %s";
    /**attribute **/
    private LocalDateTime _now;
    private DateTimeFormatter _dateFormat;
    private DateTimeFormatter _timeFormat;

    public DateFormatterHelper() {
        this.updateCurrentTime();
        this._dateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT);
    }
    /**
    public String getDateDisplay(Task task) {
        updateCurrentTime();
        // obtain necessary data for

        assert task != null;
        // check the task date
        if(isSameWeek()){
            if(isToday()){
                return DATE_TODAY;
            } else if (isTommorow()){
                return DATE_TOMMOROW;
            } else if (isYesterday()){
                return DATE_YESTERDAY;
            } else {
                // return this + dayString
            }
        } else {
            //return date;
        }
        return "";
    }
    **/
    private void updateCurrentTime() {
        _now = LocalDateTime.now();
    }

    public boolean isToday(LocalDateTime ldt) {
    //    assert(isStillSameWeek(ldt));

        int curYear = this._now.getYear();
        int curDayOfYear = this._now.getDayOfYear();
        int taskYear = ldt.getYear();
        int taskDayOfYear = ldt.getDayOfYear();

        return (curYear == taskYear) && ( curDayOfYear == taskDayOfYear);
    }

    public boolean isTommorrow(LocalDateTime ldt){

        //assert(isStillSameWeek(ldt));

        int curYear = this._now.getYear();
        int curDayOfYear = this._now.getDayOfYear();
        int taskYear = ldt.getYear();
        int taskDayOfYear = ldt.getDayOfYear();

        if (curYear == taskYear){
            return (taskDayOfYear -  curDayOfYear) == 1;
        } else {
            return (taskYear - curYear == 1) && ( taskDayOfYear == 1 && (curDayOfYear == 365) || (curDayOfYear == 366));
        }


    }


    public boolean isYesterday(LocalDateTime ldt) {
       // assert(isStillSameWeek(ldt));
        int curYear = this._now.getYear();
        int curDayOfYear = this._now.getDayOfYear();
        int taskYear = ldt.getYear();
        int taskDayOfYear = ldt.getDayOfYear();

        if(curYear == taskYear){
            return (curYear == taskYear) && ((curDayOfYear - taskDayOfYear) == 1);
        } else {
            return (curYear - taskYear == 1) && ( curDayOfYear == 1 && (taskDayOfYear == 365 || taskDayOfYear == 366));
        }


    }

    /**
    private boolean isStillSameWeek(LocalDateTime ldt){
            int dayMaxValue = DayOfWeek.SUNDAY.getValue();
        // if today is sunday,
            int curYear = this._now.getYear();
            int curDayOfYear = this._now.getDayOfYear();
            int curDayValue = this._now.getDayOfWeek().getValue();
            int taskYear = ldt.getYear();
            int taskDayOfYear = ldt.getDayOfYear();
            int taskdayValue = ldt.getDayOfWeek().getValue();
            int dayValueDifference = dayMaxValue - curDayValue;

        if( curYear == taskYear){
             return (Math.abs(curDayOfYear - taskDayOfYear) <=7 ) && (()||())
        } else {

        }
    }
     **/
    // help in testing, to be deceprated.
    public void setNow(LocalDateTime newLdt){
        this._now = newLdt;
    }

}
