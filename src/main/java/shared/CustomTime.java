package shared;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * @@author Mai Anh Vu
 */
public class CustomTime implements Comparable<CustomTime> {
    /**
     * Constants
     */
    private static final char CHAR_SEPARATOR = 'T';
    private static final String TIME_FORMAT = "HH:mm";

    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ISO_DATE;
    private static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern(TIME_FORMAT);
    private static final ChronoUnit PRECISION_TIME_DEFAULT = ChronoUnit.MINUTES;
    private static final ChronoUnit PRECISION_TIME_ALL_NULL = ChronoUnit.FOREVER;

    /**
     * Properties
     */
    private final LocalDate _date;
    private final LocalTime _time;
    private final TemporalUnit _precision;

    public CustomTime(LocalDate date, LocalTime time, TemporalUnit precision) {
        this._date = date;
        this._time = time;
        if (time == null || time.getHour() == 0 && time.getMinute() == 0) {
            if (date == null) {
                this._precision = PRECISION_TIME_ALL_NULL;
            } else {
                this._precision = ChronoUnit.DAYS;
            }
        } else {
            this._precision = precision;
        }

    }

    public CustomTime(LocalDate date, LocalTime time) {
        this(date, time, PRECISION_TIME_DEFAULT);
    }

    public CustomTime(LocalDateTime dateTime) {
        this(dateTime.toLocalDate(), dateTime.toLocalTime());
    }

    public CustomTime(int year, Month month, int day, int hour, int minute) {
        this(LocalDate.of(year, month, day), LocalTime.of(hour, minute));
    }

    public CustomTime withPrecision(TemporalUnit unit) {
        return new CustomTime(this._date, this._time, unit);
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

    public CustomTime sameDayAt(LocalTime time) {
        return new CustomTime(this._date, time);
    }

    public CustomTime current(DayOfWeek dayOfWeek) {
        if (!this.hasDate()) {
            return null;
        }
        int thisDoW = this.getDate().getDayOfWeek().getValue();
        int destDoW = dayOfWeek.getValue();
        int offset = destDoW - thisDoW;
        if (offset < 0) {
            offset += 7;
        }
        return new CustomTime(this.getDate().plusDays(offset), this.getTime());
    }

    public CustomTime next(DayOfWeek dayOfWeek) {
        return new CustomTime(this.current(dayOfWeek).getDate().plusDays(7), null);
    }


    public LocalDate getDate() {
        return this._date;
    }

    public LocalTime getTime() {
        return this._time;
    }

    public TemporalUnit getPrecision() {
        return this._precision;
    }

    public boolean hasDate() {
        return this._date != null;
    }

    public boolean hasTime() {
        return this._time != null;
    }

    public boolean hasSameDate(CustomTime ct) { return this.getDate().equals(ct.getDate()); }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
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
        return sb.toString();
    }

    public static CustomTime fromString(String customTimeString) {
        if (customTimeString == null || customTimeString.trim().isEmpty()) {
            return null;
        }
        String[] values = customTimeString.split(Character.toString(CHAR_SEPARATOR));
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
        LocalDate thisDate = this.hasDate() ? this.getDate() : LocalDate.MAX;
        LocalDate otherDate = time.hasDate() ? time.getDate() : LocalDate.MAX;
        int comparison = thisDate.compareTo(otherDate);
        // If date differs return them straight away
        if (comparison != 0) {
            return comparison;
        }

        LocalTime thisTime = this.hasTime() ? this.getTime() : LocalTime.MAX;
        LocalTime otherTime = time.hasTime() ? time.getTime() : LocalTime.MAX;
        return thisTime.compareTo(otherTime);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (o instanceof CustomTime) {
            CustomTime time = (CustomTime) o;
            if (this.hasDate() ^ time.hasDate()) return false;
            if (this.hasDate() && !this.getDate().equals(time.getDate())) {
                return false;
            }

            if (this.hasTime() ^ time.hasTime()) return false;
            if (this.hasTime() && !this.getTime().equals(time.getTime())) {
                return false;
            }

            return true;
        } else if (o instanceof LocalDateTime) {
            CustomTime time = new CustomTime((LocalDateTime) o);
            return this.equals(time);
        } else {
            return false;
        }
    }
}
