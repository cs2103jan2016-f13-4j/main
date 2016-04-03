package ui.controller;

import shared.CustomTime;
import shared.Task;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Antonius Satrio Triatmoko on 4/1/2016.
 */
public class DateFormatterHelper {
    /**Constant**/
    private final String IN_WEEK_FORMAT = "EE";
    private final String DATE_FORMAT = "dd/MM";
    private final String HOUR_FORMAT = "ha";
    private final String DATE_YESTERDAY = "Yesterday";
    private final String DATE_TODAY = "Today";
    private final String DATE_TOMMOROW = "Tomorrow";
    private final String DATE_NEXT_WEEK = "Next %s";

    /**attribute **/
    private CustomTime _now;
    private DateTimeFormatter _inWeekFormat;
    private DateTimeFormatter _otherDateFormat;
    private DateTimeFormatter _timeFormat;

    public DateFormatterHelper() {
        this.updateCurrentTime();
        this._inWeekFormat = DateTimeFormatter.ofPattern(IN_WEEK_FORMAT);
        this._otherDateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT);
        this._timeFormat = DateTimeFormatter.ofPattern(HOUR_FORMAT);
    }

    public String getDateDisplay(CustomTime time) {
        updateCurrentTime();
        String title = "";

        if(time.hasDate()) {

            if (isToday(time)) {
                title = DATE_TODAY;
            } else if (isTomorrow(time)) {
                title = DATE_TOMMOROW;
            } else if (isYesterday(time)) {
                title = DATE_YESTERDAY;
            } else if (isSameWeek(time)) {
                title = time.getDate().format(_inWeekFormat);
            } else if (isNextWeek(time)) {
                title = String.format(DATE_NEXT_WEEK,time.getDate().format(_inWeekFormat));
            } else {
                title = time.getDate().format(_otherDateFormat);
            }

        }
        return title;
    }

    private void updateCurrentTime() {
        _now = CustomTime.now();
    }

    boolean isToday(CustomTime time) {


        int curYear = this._now.getDate().getYear();
        int curDayOfYear = this._now.getDate().getDayOfYear();
        int taskYear = time.getDate().getYear();
        int taskDayOfYear = time.getDate().getDayOfYear();

        return (curYear == taskYear) && (curDayOfYear == taskDayOfYear);
    }

    boolean isTomorrow(CustomTime time){


        int curYear = this._now.getDate().getYear();
        int curDayOfYear = this._now.getDate().getDayOfYear();
        int taskYear = time.getDate().getYear();
        int taskDayOfYear = time.getDate().getDayOfYear();

        if (curYear == taskYear){
            return (taskDayOfYear -  curDayOfYear) == 1;
        } else {
            return (taskYear - curYear == 1) && ( taskDayOfYear == 1 && (curDayOfYear == 365 || curDayOfYear == 366));
        }


    }


     boolean isYesterday(CustomTime time) {

         int curYear = this._now.getDate().getYear();
         int curDayOfYear = this._now.getDate().getDayOfYear();
         int taskYear = time.getDate().getYear();
         int taskDayOfYear = time.getDate().getDayOfYear();

        if(curYear == taskYear){
            return (curYear == taskYear) && ((curDayOfYear - taskDayOfYear) == 1);
        } else {
            return (curYear - taskYear == 1) && ( curDayOfYear == 1 && (taskDayOfYear == 365 || taskDayOfYear == 366));
        }


    }


     boolean isSameWeek(CustomTime time){

        int curYear = this._now.getDate().getYear();
        int curDayOfYear = this._now.getDate().getDayOfYear();
        int curDayValue = this._now.getDate().getDayOfWeek().getValue();

        int taskYear = time.getDate().getYear();
        int taskDayOfYear = time.getDate().getDayOfYear();
        int taskDayValue = time.getDate().getDayOfWeek().getValue();

        int dayValueDifference = taskDayValue - curDayValue;
        //System.out.println(dayValueDifference);
        if( curYear == taskYear){
            //System.out.println(taskDayOfYear - curDayOfYear);
             return  taskDayOfYear - curDayOfYear  == dayValueDifference;
        } else {
            if (taskYear - curYear == 1){
                return (curDayOfYear + dayValueDifference) % dayYearValue(curYear) == taskDayOfYear;
            } else if(curYear - taskYear == 1){
                return (taskDayOfYear - dayValueDifference) % dayYearValue(taskYear) == curDayOfYear;
            }
        }

        return false;
    }

    boolean isNextWeek(CustomTime time){
        int sunday = DayOfWeek.SUNDAY.getValue();

        int curYear = this._now.getDate().getYear();
        int curDayOfYear = this._now.getDate().getDayOfYear();
        int curDayValue = this._now.getDate().getDayOfWeek().getValue();

        int taskYear = time.getDate().getYear();
        int taskDayOfYear = time.getDate().getDayOfYear();
        int curNewWeekDistance = sunday - curDayValue;

        int maxRange = curNewWeekDistance + 6;

        int dayValueDifference;

        if(curYear == taskYear) {
            dayValueDifference = taskDayOfYear - curDayOfYear;

            if(dayValueDifference > 0) {
                return (dayValueDifference > curNewWeekDistance) && ( dayValueDifference <= maxRange);
            }
        } else if (taskYear > curYear) {
            int carryOver = dayYearValue(curYear);
            dayValueDifference = carryOver + taskDayOfYear - curDayOfYear;

            return (dayValueDifference > curNewWeekDistance) && (dayValueDifference <= maxRange);
        }

        return false;

    }

    // help in testing, to be deceprated.
    public void setNow(CustomTime newTime){
        this._now = newTime;
    }

    private boolean isLeapYear(int year){

        if(year%4 == 0) {
            if(year%100 == 0){
                if(year%400 == 0){
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    private int dayYearValue(int year){
        if(isLeapYear(year)){
            return 366;
        } else {
            return 365;
        }
    }

}
