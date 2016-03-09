package util;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Test;

import utility.StringParser;


public class StringParserTest {

    @Test
    public void testAsDateTime() {
        String origDateTimeStr = "15051993 2000";
        LocalDateTime ldt = StringParser.asDateTime(origDateTimeStr);
        
        assertEquals(15, ldt.getDayOfMonth());
        assertEquals(Month.MAY, ldt.getMonth());
        assertEquals(1993, ldt.getYear());
        assertEquals(20, ldt.getHour());
        assertEquals(0, ldt.getMinute());
    }

}
