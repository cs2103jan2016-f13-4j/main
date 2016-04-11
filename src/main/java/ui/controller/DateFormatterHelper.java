package ui.controller;

import javafx.util.Pair;
import shared.CustomTime;
import shared.Task;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class manages the format in which the time is going to be displayed in the User Interface
 *
 * Created by Antonius Satrio Triatmoko on 4/1/2016.
 */
public class DateFormatterHelper {
    /**Constant**/
    private final String EMPTY_STRING = "";
    private final String IN_WEEK_FORMAT = "EE";
    private final String DATE_FORMAT = "dd/MM/YYYY";
    private final String DATE_YESTERDAY = "Yesterday";
    private final String DATE_TODAY = "Today";
    private final String DATE_TOMMOROW = "Tomorrow";
    private final String DATE_NEXT_WEEK = "Next %s";
    private final String DATE_PAIR_PATTERN = "%s to %s";
    private final String TIME_FORMAT = "hh:mm a" ;
    private final String TIME_FROM = "From %s";
    private final String TIME_BY = "By %s";
    private final int ONE_DAY_DIFFERENCE = 1;
    private final int FIRST_DAY = 1;
    private final int RANGE_BEFORE_DAY_IS_REPEATED = 6;
    /**attribute **/
    private CustomTime _now;
    private DateTimeFormatter _inWeekFormat;
    private DateTimeFormatter _otherDateFormat;
    private DateTimeFormatter _timeFormat;

    public DateFormatterHelper() {
        this.updateCurrentTime();
        this._inWeekFormat = DateTimeFormatter.ofPattern(IN_WEEK_FORMAT);
        this._otherDateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT);
        this._timeFormat = DateTimeFormatter.ofPattern(TIME_FORMAT);
    }

    /***
     * retrieve the date or day of the time relative to the current timing. the current time used as comparison will be time
     * when the function is called.
     * <ul>
     * <li> Today's date will be reflected as 'Today'
     * <li> Tomorrow's date will be reflected as 'Tomorrow'
     * <li> Yesterday's date will be reflected as 'Yesterday'
     * <li> Date that is still within the same week of today's date will be reflected as 'This [name of day]'
     * <li> Date that is within the next week from today will be reflected as 'Next [name of day]'
     * <li> Other date will be reflected as Date following the format DD/MM/YYYY
     * </ul>
     * @param time time that is going to be processed
     * @return String representing the specified date from the time parameter
     */
    public String getDateDisplay(CustomTime time) {
        updateCurrentTime();
        String date = EMPTY_STRING;

        if(time != null && time.hasDate()) {

            if (isToday(time)) {
                date = DATE_TODAY;
            } else if (isTomorrow(time)) {
                date = DATE_TOMMOROW;
            } else if (isYesterday(time)) {
                date = DATE_YESTERDAY;
            } else if (isSameWeek(time)) {
                date = time.getDate().format(_inWeekFormat);
            } else if (isNextWeek(time)) {
                date = String.format(DATE_NEXT_WEEK,time.getDate().format(_inWeekFormat));
            } else {
                date = time.getDate().format(_otherDateFormat);
            }

        }

        return date;
    }

    /***
     * this method return the time stored in the CustomTime object.
     * The format of the returned String is HH:mm
     * if the time is a null object, it will return an empty string
     *
     * @param time CustomTime object which time stored going to be formatted
     * @return time stored in CustomTime following the described format
     */
    public String getTimeDisplay(CustomTime time){
        String display = EMPTY_STRING;

        if (time.hasTime()) {
            display = this._timeFormat.format(time.getTime());
        }

        return display;
    }

    /***
     * This method return both the date and the time stored in the cTime parameter in one String. separated by an empty space.
     * The format of the return string is [DATE] [TIME]
     *
     * @param cTime stored time to be processed
     * @return String containing the date and the time from the cTime parameter
     */
    public String getDateTimeDisplay(CustomTime cTime) {
        assert cTime != null;

        String display =  getDateDisplay(cTime);
        if (display.isEmpty()) {
            if (cTime.hasTime()) {
                display  = getTimeDisplay(cTime);
            }
        } else {
            if (cTime.hasTime()) {
                display = display + " " + getTimeDisplay(cTime);
            }
        }

        return display;
    }



    /***
     * This special method is called to print the time stored in the task following the specification in the cell item.
     * Possible printed format:
     * <ul>
     * <li> [START TIME] to [END TIME]
     * <li> [START TIME] T0 [END DATE] [END TIME]
     * <li> by [START TIME]
     * <li> by [START DATE] [START TIME]
     *</ul>
     * @param task the task to be processed
     * @return String containing the formatted time information related to the task
     */
    public String getCellTimeTaskDisplay(Task task) {

        CustomTime startTime = task.getStartTime();
        CustomTime endTime = task.getEndTime();
        String display = EMPTY_STRING;

        if (startTime != null) {
            if (startTime.hasTime()) {
                display = this.getTimeDisplay(startTime);

                if (endTime != null) {
                    if (endTime.hasSameDate(startTime)) {
                        return String.format(DATE_PAIR_PATTERN, display, this.getTimeDisplay(endTime));
                    } else {
                        return String.format(DATE_PAIR_PATTERN, display, this.getDateTimeDisplay(endTime));
                    }
                }

                return String.format(TIME_FROM, display);
            } else {
                if (endTime != null) {
                    if (endTime.hasSameDate(startTime)) {
                        return String.format(TIME_BY, this.getTimeDisplay(endTime));
                    } else {
                        return String.format(TIME_BY, this.getDateTimeDisplay(endTime));
                    }
                }
                return display;
            }

        } else {
            if (endTime != null) {
                if (endTime.hasTime()) {
                    return String.format(TIME_BY, this.getTimeDisplay(endTime));
                }
            }
        }

        return display;
    }



    private void updateCurrentTime() {
        _now = CustomTime.now();
    }


    // ----------------------------------------------------------------------------------------
    //
    // Helper method to determine Task time relationship with the current time
    //
    // ----------------------------------------------------------------------------------------

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

        if (curYear == taskYear) {
            return (taskDayOfYear -  curDayOfYear) == ONE_DAY_DIFFERENCE;
        } else {
            return isNextYear(time) && ( taskDayOfYear == 1 && (curDayOfYear == this.dayYearValue(curYear)));
        }


    }

    boolean isLastYear(CustomTime time){
        int curYear = this._now.getDate().getYear();
        int taskYear = time.getDate().getYear();

        return curYear - taskYear == 1;
    }
    
    boolean isNextYear(CustomTime time){
        int curYear = this._now.getDate().getYear(); 
        int taskYear = time.getDate().getYear(); 
        
        return taskYear - curYear == 1;
    }

     boolean isYesterday(CustomTime time) {

         int curYear = this._now.getDate().getYear();
         int curDayOfYear = this._now.getDate().getDayOfYear();
         int taskYear = time.getDate().getYear();
         int taskDayOfYear = time.getDate().getDayOfYear();

        if (curYear == taskYear) {
            return (curDayOfYear - taskDayOfYear) == ONE_DAY_DIFFERENCE;
        } else {
            return isLastYear(time) && ( curDayOfYear == FIRST_DAY && (taskDayOfYear == dayYearValue(taskYear)));
        }


    }


     boolean isSameWeek(CustomTime time){

        int curYear = this._now.getDate().getYear();
        int curDayOfYear = this._now.getDate().getDayOfYear();
        int curDayValue = this._now.getDate().getDayOfWeek().getValue();

        int taskYear = time.getDate().getYear();
        int taskDayOfYear = time.getDate().getDayOfYear();
        int taskDayValue = time.getDate().getDayOfWeek().getValue();
         // difference in value of current day to the task day.
        int dayValueDifference = taskDayValue - curDayValue;

         if( curYear == taskYear) {
             return  taskDayOfYear - curDayOfYear  == dayValueDifference;
         } else {
            if (isNextYear(time)) {
                return (curDayOfYear + dayValueDifference) % dayYearValue(curYear) == taskDayOfYear;
            } else if(isLastYear(time)) {
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

        int maxRange = curNewWeekDistance + RANGE_BEFORE_DAY_IS_REPEATED;

        int dayValueDifference;

        if (curYear == taskYear) {
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

    /***
     * Set the current time attribute of the class to the specified input time. Should only be used in Testing.
     *
     * @param newTime time to be set as the current time
     */
    void setNow(CustomTime newTime){
        this._now = newTime;
    }

    /** determine if the input year is a leap year */
    private boolean isLeapYear(int year){
        if (year%4 == 0) {
            if (year%100 == 0) {
                if (year%400 == 0) {
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    /** method to obtain the day of year value of the year */
    private int dayYearValue(int year){
        if (isLeapYear(year)) {
            return 366;
        } else {
            return 365;
        }
    }

}
