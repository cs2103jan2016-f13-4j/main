package ui.controller;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import ui.controller.DateFormatterHelper;

import java.time.LocalDateTime;

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
        LocalDateTime ldt =  LocalDateTime.now();
        LocalDateTime notNow = LocalDateTime.of(2016,12,12,12,12);
        assertTrue(this._dfh.isToday(ldt));
        assertFalse(this._dfh.isToday(notNow));
        ldt = ldt.minusSeconds(55);
        assertTrue(this._dfh.isToday(ldt));
    }

    @Test public void Helper_Return_Expected_Day_Are_Tommorrow_Test(){
        // test for same day. tommorrow, and yesterday
        LocalDateTime ldt = LocalDateTime.now();
        assertFalse(this._dfh.isTomorrow(ldt));
        ldt = ldt.plusDays(1);
        assertTrue(this._dfh.isTomorrow(ldt));
        ldt = ldt.minusDays(2);
        assertFalse(this._dfh.isTomorrow(ldt));

        // test for different years
        ldt = LocalDateTime.of(2016,12,31,12,12);
        this._dfh.setNow(ldt);
        ldt = ldt.plusDays(1);
        assertTrue(this._dfh.isTomorrow(ldt));

        // test for different month :
        ldt = ldt.of(2016,11,30,12,12);
        this._dfh.setNow(ldt);
        ldt = ldt.plusDays(1);
        assertTrue(this._dfh.isTomorrow(ldt));
    }

    @Test public void Helper_Return_Expected_Days_Are_Yesterday_Test(){
        LocalDateTime ldt = LocalDateTime.now();
        assertFalse(this._dfh.isYesterday(ldt));
        ldt = ldt.minusDays(1);
        assertTrue(this._dfh.isYesterday(ldt));
        ldt = ldt.plusDays(2);
        assertFalse(this._dfh.isYesterday(ldt));

        // check for different year
        ldt = LocalDateTime.of(2016,1,1,12,12);
        this._dfh.setNow(ldt);
        ldt = ldt.minusDays(1);
        assertTrue(this._dfh.isYesterday(ldt));;
    }

    @Test public void Helper_Return_Expected_Days_Are_Same_Week(){
        LocalDateTime ldt = LocalDateTime.of(2016,3,31,12,00);
        this._dfh.setNow(ldt);
        ldt = ldt.plusDays(1);
        assertTrue(this._dfh.isSameWeek(ldt));
        ldt = ldt.minusDays(3);
        assertTrue(this._dfh.isSameWeek(ldt));
        // check for different year task > cur. 31/12/2016 is Saturday
        ldt = LocalDateTime.of(2016,12,31,12,12);
        this._dfh.setNow(ldt);
        ldt = ldt.plusDays(1);
        assertTrue(this._dfh.isSameWeek(ldt));
        ldt = ldt.plusDays(1);
        assertFalse(this._dfh.isSameWeek(ldt));
        ldt = ldt.minusDays(5);
        assertTrue(this._dfh.isSameWeek(ldt));
        ldt = ldt.minusDays(3);
        assertFalse(this._dfh.isSameWeek(ldt));
        // check for different eyar cur > task. 1/1/2017 is Sunday
        ldt = LocalDateTime.of(2017,1,1,12,12);
        this._dfh.setNow(ldt);
        ldt = ldt.minusDays(1);
        assertTrue(this._dfh.isSameWeek(ldt));
        ldt = ldt.minusDays(5);
        assertTrue(this._dfh.isSameWeek(ldt));

    }

    @Test public void Helper_Days_Are_Next_Week(){
        LocalDateTime ldt = LocalDateTime.of(2016,12,31,12,12);
        this._dfh.setNow(ldt);
        ldt = ldt.plusDays(2);
        assertTrue(this._dfh.isNextWeek(ldt));
        ldt = ldt.plusDays(6);
        assertFalse(this._dfh.isNextWeek(ldt));
        ldt = ldt.minusDays(14);
        assertFalse(this._dfh.isNextWeek(ldt));
    }
    @Test public void Helper_Get_Display_Test(){
        LocalDateTime ldt = LocalDateTime.now();
        assertTrue(this._dfh.getDateDisplay(ldt).equals("Today"));
        ldt = ldt.plusDays(1);
        assertTrue(this._dfh.getDateDisplay(ldt).equals("Tomorrow"));
        ldt = ldt.minusDays(2);
        assertTrue(this._dfh.getDateDisplay(ldt).equals("Yesterday"));

    }

}
