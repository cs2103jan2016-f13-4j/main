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
    private final String IN_WEEK_FORMAT = "EE";
    private final String DATE_FORMAT = "dd/MM";
    private final String HOUR_FORMAT = "ha";
    private final String DATE_YESTERDAY = "Yesterday";
    private final String DATE_TODAY = "Today";
    private final String DATE_TOMMOROW = "Tomorrow";
    private final String DATE_NEXT_WEEK = "Next %s";

    /**attribute **/
    private LocalDateTime _now;
    private DateTimeFormatter _inWeekFormat;
    private DateTimeFormatter _otherDateFormat;
    private DateTimeFormatter _timeFormat;

    public DateFormatterHelper() {
        this.updateCurrentTime();
        this._inWeekFormat = DateTimeFormatter.ofPattern(IN_WEEK_FORMAT);
        this._otherDateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT);
        this._timeFormat = DateTimeFormatter.ofPattern(HOUR_FORMAT);
    }

    public String getDateDisplay(LocalDateTime ldt) {
        updateCurrentTime();
        String title = "";

        if(ldt != null) {

            if (isToday(ldt)) {
                title = DATE_TODAY;
            } else if (isTomorrow(ldt)) {
                title = DATE_TOMMOROW;
            } else if (isYesterday(ldt)) {
                title = DATE_YESTERDAY;
            } else if (isSameWeek(ldt)) {
                title = ldt.format(_inWeekFormat);
            } else if (isNextWeek(ldt)){
                title = String.format(DATE_NEXT_WEEK,ldt.format(_inWeekFormat));
            } else {
                title = ldt.format(_otherDateFormat);
            }

        }
        return title;
    }

    private void updateCurrentTime() {
        _now = LocalDateTime.now();
    }

    boolean isToday(LocalDateTime ldt) {


        int curYear = this._now.getYear();
        int curDayOfYear = this._now.getDayOfYear();
        int taskYear = ldt.getYear();
        int taskDayOfYear = ldt.getDayOfYear();

        return (curYear == taskYear) && (curDayOfYear == taskDayOfYear);
    }

    boolean isTomorrow(LocalDateTime ldt){


        int curYear = this._now.getYear();
        int curDayOfYear = this._now.getDayOfYear();
        int taskYear = ldt.getYear();
        int taskDayOfYear = ldt.getDayOfYear();

        if (curYear == taskYear){
            return (taskDayOfYear -  curDayOfYear) == 1;
        } else {
            return (taskYear - curYear == 1) && ( taskDayOfYear == 1 && (curDayOfYear == 365 || curDayOfYear == 366));
        }


    }


     boolean isYesterday(LocalDateTime ldt) {

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


     boolean isSameWeek(LocalDateTime ldt){

        int curYear = this._now.getYear();
        int curDayOfYear = this._now.getDayOfYear();
        int curDayValue = this._now.getDayOfWeek().getValue();

        int taskYear = ldt.getYear();
        int taskDayOfYear = ldt.getDayOfYear();
        int taskDayValue = ldt.getDayOfWeek().getValue();

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

    boolean isNextWeek(LocalDateTime ldt){
        int sunday = DayOfWeek.SUNDAY.getValue();

        int curYear = this._now.getYear();
        int curDayOfYear = this._now.getDayOfYear();
        int curDayValue = this._now.getDayOfWeek().getValue();

        int taskYear = ldt.getYear();
        int taskDayOfYear = ldt.getDayOfYear();
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
    public void setNow(LocalDateTime newLdt){
        this._now = newLdt;
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
