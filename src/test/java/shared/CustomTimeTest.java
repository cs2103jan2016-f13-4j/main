package shared;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by maianhvu on 23/03/2016.
 */
public class CustomTimeTest {

    @Test public void CustomTime_lets_user_instantiate_without_date() {
        CustomTime timeWithoutDate = new CustomTime(null, LocalTime.now());
        assertThat(timeWithoutDate.hasDate(), is(false));
    }

    @Test public void CustomTime_lets_user_instantiate_without_time() {
        CustomTime dateWithoutTime = new CustomTime(LocalDate.now(), null);
        assertThat(dateWithoutTime.hasTime(), is(false));
    }

    @Test public void CustomTime_toString_is_in_ISO_format_up_to_minute() {
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

        CustomTime timeObject = new CustomTime(nowDate, nowTime);
        assertThat(timeObject.toString(), is(equalTo(
                encodeTime(nowDate, nowTime)
        )));
    }

    @Test public void CustomTime_decodes_correctly_from_ISO_up_to_minute_string() {
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        DateTimeFormatter isoFormatterDate = DateTimeFormatter.ISO_DATE;
        DateTimeFormatter isoFormatterTime = DateTimeFormatter.ofPattern("HH:mm");
        String encodedTime = encodeTime(nowDate, nowTime);
        CustomTime time = CustomTime.fromString(encodedTime);

        assertThat(time.getDate(), is(equalTo(nowDate)));
        assertThat(time.getTime(), is(equalTo(nowTime)));
    }

    private static String encodeTime(LocalDate date, LocalTime time) {
        DateTimeFormatter isoFormatterDate = DateTimeFormatter.ISO_DATE;
        DateTimeFormatter isoFormatterTime = DateTimeFormatter.ofPattern("HH:mm");
        return String.format("%sT%s",
                isoFormatterDate.format(date),
                isoFormatterTime.format(time));
    }

    @Test public void CustomTime_gets_today_at_correctly() {
        LocalTime fivePm = LocalTime.of(17,0);
        CustomTime todayAt5 = CustomTime.todayAt(fivePm);
        assertThat(todayAt5.getDate(), is(equalTo(LocalDate.now())));
        assertThat(todayAt5.getTime(), is(equalTo(fivePm)));
    }

    @Test public void CustomTime_gets_tomorrow_at_correctly() {
        LocalTime fivePm = LocalTime.of(17,0);
        CustomTime tomorrowAt5 = CustomTime.tomorrowAt(fivePm);
        assertThat(tomorrowAt5.getDate(), is(equalTo(LocalDate.now().plusDays(1))));
        assertThat(tomorrowAt5.getTime(), is(equalTo(fivePm)));
    }

    @Test public void CustomTime_gets_same_day_at_correctly() {
        CustomTime time = CustomTime.now();
        CustomTime newTime = time.sameDayAt(LocalTime.of(16,0));
        assertThat(newTime.getDate(), is(equalTo(time.getDate())));
        assertThat(newTime.getTime(), is(equalTo(LocalTime.of(16, 0))));
    }

    @Test public void CustomTime_with_both_date_and_time_compares_normally() {
        LocalDateTime reference1 = LocalDateTime.now();
        LocalDateTime reference2 = reference1.plusHours(45);

        CustomTime time1 = new CustomTime(reference1);
        CustomTime time2 = new CustomTime(reference2);
        assertThat(time1.compareTo(time2), is(equalTo(reference1.compareTo(reference2))));
    }

    @Test public void CustomTime_with_null_date_is_greater_than_specified_date() {
        CustomTime specificDay = new CustomTime(LocalDate.now(), null);
        CustomTime floatingDay = new CustomTime(null, null);
        assertThat(specificDay.compareTo(floatingDay) < 0, is(true));
    }

    @Test public void CustomTime_with_null_time_is_greater_than_specified_time() {
        CustomTime specificTime = new CustomTime(LocalDateTime.now());
        CustomTime floatingTime = new CustomTime(LocalDate.now(), null);
        assertThat(specificTime.compareTo(floatingTime) < 0, is(true));
    }

    @Test public void CustomTime_calculates_correct_time_difference() {
        CustomTime time1 = CustomTime.todayAt(LocalTime.of(5, 30));
        CustomTime time2 = CustomTime.todayAt(LocalTime.of(7, 0));
        assertThat(CustomTime.difference(time1, time2),
                is(equalTo(90)));
    }

    @Test public void CustomTime_calculates_absolute_difference() {
        CustomTime time1 = CustomTime.todayAt(LocalTime.of(5, 30));
        CustomTime time2 = CustomTime.todayAt(LocalTime.of(7, 0));
        assertThat(CustomTime.difference(time2, time1),
                is(equalTo(90)));
    }

}
