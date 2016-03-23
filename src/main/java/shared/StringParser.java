package shared;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @@author Mai Anh Vu
 */
public class StringParser {
    // we don't want this class to be instantiated
    private StringParser() {
    }

    // date pattern: DDMMYYYY HHMM
    private static final Pattern PATTERN_DATE = Pattern.compile(
            "(\\d\\d)(\\d\\d)(\\d\\d\\d\\d) (\\d\\d)(\\d\\d)");

    public static LocalDateTime asDateTime(String str) {
        assert str != null;
        Matcher matcher = PATTERN_DATE.matcher(str);
        matcher.matches();

        String dayStr = matcher.group(1);
        String monthStr = matcher.group(2);
        String yearStr = matcher.group(3);
        String hourStr = matcher.group(4);
        String minuteStr = matcher.group(5);

        int day = Integer.parseInt(dayStr);
        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr);
        int hour = Integer.parseInt(hourStr);
        int minute = Integer.parseInt(minuteStr);

        return LocalDateTime.of(year, month, day, hour, minute);
    }
}
