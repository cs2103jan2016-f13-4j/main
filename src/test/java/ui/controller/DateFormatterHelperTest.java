package ui.controller;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import shared.CustomTime;
import ui.controller.DateFormatterHelper;

import java.time.LocalDate;
import java.time.LocalTime;


/**
 * Created by Antonius Satrio Triatmoko
 */
public class DateFormatterHelperTest {

    private DateFormatterHelper _dfh;


    @Before
    public void setup(){
        this._dfh = new DateFormatterHelper();
    }

    @Test public void Helper_Return_Expected_Same_Day_Test() {
        LocalDate ld = LocalDate.of(2015,12,12);
        LocalTime lt = LocalTime.of(12,12);
        CustomTime time =  CustomTime.now();
        CustomTime notNow = new CustomTime(ld,lt);
        assertTrue(this._dfh.isToday(time));
        assertFalse(this._dfh.isToday(notNow));
        time =  new CustomTime(time.getDate(),time.getTime().minusSeconds(55));
        assertTrue(this._dfh.isToday(time));
    }

    @Test public void Helper_Return_Expected_Day_Are_Tommorrow_Test(){
        // test for same day. tommorrow, and yesterday
        CustomTime time = CustomTime.now();
        assertFalse(this._dfh.isTomorrow(time));
        time = new CustomTime(time.getDate().plusDays(1),time.getTime());
        assertTrue(this._dfh.isTomorrow(time));
        time = new CustomTime(time.getDate().minusDays(1),time.getTime());
        assertFalse(this._dfh.isTomorrow(time));

        // test for different years
        LocalDate ld = LocalDate.of(2016,12,31);
        LocalTime lt = LocalTime.of(12,12);
        time = new CustomTime(ld,lt);
        this._dfh.setNow(time);
        time = new CustomTime(time.getDate().plusDays(1),lt);
        assertTrue(this._dfh.isTomorrow(time));

        // test for different month :
        ld = LocalDate.of(2016,11,30);
        time = new CustomTime(ld,lt);
        this._dfh.setNow(time);
        time = new CustomTime(ld.plusDays(1),lt);
        assertTrue(this._dfh.isTomorrow(time));
    }


    @Test public void Helper_Return_Expected_Days_Are_Yesterday_Test(){
        CustomTime time = CustomTime.now();
        assertFalse(this._dfh.isYesterday(time));
        time = new CustomTime( time.getDate().minusDays(1), time.getTime());
        assertTrue(this._dfh.isYesterday(time));
        time = new CustomTime( time.getDate().plusDays(2), time.getTime());
        assertFalse(this._dfh.isYesterday(time));

        // check for different year
        time =  new CustomTime( LocalDate.of(2016,1,1),  LocalTime.of(12,12));
        this._dfh.setNow(time);
        time = new CustomTime( LocalDate.of(2016,1,1).minusDays(1),  LocalTime.of(12,12));
        assertTrue(this._dfh.isYesterday(time));;
    }

    @Test public void Helper_Return_Expected_Days_Are_Same_Week(){
        CustomTime time = new CustomTime( LocalDate.of(2016,3,31),LocalTime.of(12,0));
        this._dfh.setNow(time);
        time = new CustomTime( LocalDate.of(2016,3,31).plusDays(1),LocalTime.of(12,0));
        assertTrue(this._dfh.isSameWeek(time));
        time = new CustomTime( LocalDate.of(2016,3,31).minusDays(3),LocalTime.of(12,0));
        assertTrue(this._dfh.isSameWeek(time));
        // check for different year task > cur. 31/12/2016 is Saturday
        time = new CustomTime(LocalDate.of(2016,12,31),LocalTime.of(12,12));
        this._dfh.setNow(time);
        time = new CustomTime(LocalDate.of(2016,12,31).plusDays(1),LocalTime.of(12,12));
        assertTrue(this._dfh.isSameWeek(time));
        time = new CustomTime(LocalDate.of(2016,12,31).plusDays(2),LocalTime.of(12,12));
        assertFalse(this._dfh.isSameWeek(time));
        time = new CustomTime(LocalDate.of(2016,12,31).minusDays(3),LocalTime.of(12,12));
        assertTrue(this._dfh.isSameWeek(time));
        time = new CustomTime(LocalDate.of(2016,12,31).minusDays(8),LocalTime.of(12,12));
        assertFalse(this._dfh.isSameWeek(time));
        // check for different eyar cur > task. 1/1/2017 is Sunday
        time = new CustomTime( LocalDate.of(2017,1,1), LocalTime.of(12,12));
        this._dfh.setNow(time);
        time = new CustomTime( LocalDate.of(2017,1,1).minusDays(1), LocalTime.of(12,12));
        assertTrue(this._dfh.isSameWeek(time));
        time = new CustomTime( LocalDate.of(2017,1,1).minusDays(6), LocalTime.of(12,12));
        assertTrue(this._dfh.isSameWeek(time));

    }


    @Test public void Helper_Days_Are_Next_Week(){
        CustomTime time = new CustomTime(LocalDate.of(2016,12,31),LocalTime.of(12,12));
        System.out.println(time.toString());
        this._dfh.setNow(time);
        time = new CustomTime(LocalDate.of(2016,12,31).plusDays(2),LocalTime.of(12,12));
        assertTrue(this._dfh.isNextWeek(time));
        time = new CustomTime(LocalDate.of(2016,12,31).plusDays(8),LocalTime.of(12,12));
        assertFalse(this._dfh.isNextWeek(time));
        time = new CustomTime(LocalDate.of(2016,12,31).minusDays(6),LocalTime.of(12,12));
        assertFalse(this._dfh.isNextWeek(time));
    }


    @Test public void Helper_Get_Display_Test(){
        CustomTime time = CustomTime.now();
        assertTrue(this._dfh.getDateDisplay(time).equals("Today"));
        time = new CustomTime(time.getDate().plusDays(1),time.getTime());
        assertTrue(this._dfh.getDateDisplay(time).equals("Tomorrow"));
        time = new CustomTime(time.getDate().minusDays(2),time.getTime());
        assertTrue(this._dfh.getDateDisplay(time).equals("Yesterday"));

    }


}