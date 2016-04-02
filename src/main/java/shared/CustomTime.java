package shared;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @@author Mai Anh Vu
 */
public class CustomTime implements Comparable<CustomTime> {
    private static final char CHAR_WRAPPER_LEFT = '[';
    private static final char CHAR_WRAPPER_RIGHT = ']';
    private static final char CHAR_SEPARATOR = ',';

    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ISO_DATE;
    private static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ISO_TIME;


    private LocalDate _date;
    private LocalTime _time;

    public CustomTime(LocalDate date, LocalTime time) {
        this._date = date;
        this._time = time;
    }

    public static CustomTime now() {
        return new CustomTime(LocalDate.now(), LocalTime.now());
    }

    public static CustomTime todayAt(LocalTime time) {
        return new CustomTime(LocalDate.now(), time);
    }

    public static CustomTime tomorrowAt(LocalTime time) {
        return new CustomTime(LocalDate.now().plusDays(1), time);
    }

    public LocalDate getDate() {
        return this._date;
    }

    public LocalTime getTime() {
        return this._time;
    }

    public boolean hasDate() {
        return this._date != null;
    }

    public boolean hasTime() {
        return this._time != null;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CHAR_WRAPPER_LEFT);
        if (this._date != null) {
            sb.append(FORMATTER_DATE.format(this._date));
        } else {
            sb.append("null");
        }
        sb.append(CHAR_SEPARATOR);
        if (this._time != null) {
            sb.append(FORMATTER_TIME.format(this._time));
        } else {
            sb.append("null");
        }
        sb.append(CHAR_WRAPPER_RIGHT);
        return sb.toString();
    }

    public static CustomTime fromString(String customTimeString) {
        if (customTimeString == null || customTimeString.trim().isEmpty()) {
            return null;
        }
        // Wrong format
        if (customTimeString.charAt(0) != CHAR_WRAPPER_LEFT ||
                customTimeString.charAt(customTimeString.length() - 1) != CHAR_WRAPPER_RIGHT) {
            return null;
        }
        String[] values = customTimeString.substring(1, customTimeString.length() - 1)
                .split(Character.toString(CHAR_SEPARATOR));
        LocalDate date;
        LocalTime time;
        if (values[0].equals("null")) {
            date = null;
        } else {
            date = LocalDate.parse(values[0], FORMATTER_DATE);
        }
        if (values[1].equals("null")) {
            time = null;
        } else {
            time = LocalTime.parse(values[1], FORMATTER_TIME);
        }
        return new CustomTime(date, time);
    }

    @Override
    public int compareTo(CustomTime time) {
        return 0; // TODO: stub
    }
}
