package ui;


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

    @Test
    public void Helper_Return_Expected_Same_Day_Test() {
        LocalDateTime ldt =  LocalDateTime.now();
        LocalDateTime notNow = LocalDateTime.of(2016,12,12,12,12);
        assertTrue(this._dfh.isToday(ldt));
        assertFalse(this._dfh.isToday(notNow));
        ldt = ldt.minusSeconds(55);
        assertTrue(this._dfh.isToday(ldt));
    }

    @Test
    public void Helper_Return_Expected_Day_Is_Tommorrow(){
        // test for same day. tommorrow, and yesterday
        LocalDateTime ldt = LocalDateTime.now();
        assertFalse(this._dfh.isTommorrow(ldt));
        ldt = ldt.plusDays(1);
        assertTrue(this._dfh.isTommorrow(ldt));
        ldt = ldt.minusDays(2);
        assertFalse(this._dfh.isTommorrow(ldt));
        // test for different years
        ldt = LocalDateTime.of(2016,12,31,12,12);
        this._dfh.setNow(ldt);
        ldt = ldt.plusDays(1);

        assertTrue(this._dfh.isTommorrow(ldt));
        // test for different month :
        ldt = ldt.of(2016,11,30,12,12);
        this._dfh.setNow(ldt);
        ldt = ldt.plusDays(1);
        assertTrue(this._dfh.isTommorrow(ldt));
    }
}
