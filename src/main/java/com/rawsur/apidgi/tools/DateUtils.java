package com.rawsur.apidgi.tools;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

public class DateUtils {

    public static Long getFirstDayOfYearToTimestamp() {

        LocalDate now = LocalDate.now(); //
        LocalDate firstDay = now.with(firstDayOfYear()); //
        return getTimeStamp(firstDay.toString());

    }

    public static String getFirstDayOfYear() {

        LocalDate now = LocalDate.now(); //
        LocalDate firstDay = now.with(firstDayOfYear()); //
        return firstDay.toString();

    }

    public static String convertDate(LocalDate value) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(value);

        return date;
    }

    public static Long getLastDayOfYearToTimestamp() {
        LocalDate now = LocalDate.now(); //
        LocalDate lastDay = now.with(lastDayOfYear()); //
        return getTimeStamp(lastDay.toString());
    }

    public static Long getToDayToTimestamp() {
        LocalDate now = LocalDate.now(); //
        return getTimeStamp(now.toString());
    }

    public static String getToDay() {
        LocalDate now = LocalDate.now(); //
        return now.toString();
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDateTime convertToLocalDateTimeViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private static Long getTimeStamp(String value) {
        Date date_stamp = getDate("yyyy-MM-dd", value);
        return date_stamp.getTime();
    }

    public static String getDate(String value) {
        Date date = getDate("yyyy-MM-dd", value);
        return date.toString();
    }

    private static Date getDate(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date;
    }

    public static String getTimeNow() {

        // convert seconds to milliseconds
        Date date = new Date();
        // format of the date
        SimpleDateFormat jdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String my_date = jdf.format(date);

        return my_date;
    }

    public static Long getTimeStampMinuit() {
        String time = DateUtils.getTimeNow();
        Long time_stamp = DateUtils.getDate("dd-MM-yyyy HH:mm:ss z", time).getTime();

        return time_stamp;
    }

    public static long diffMinutes(long start, long end) {
        long diff = end - start;
        long diffMin = diff / (60 * 1000);
        return diffMin;
    }

    static public Date fromLdt(LocalDateTime ldt) {
        ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
        GregorianCalendar cal = GregorianCalendar.from(zdt);
        return cal.getTime();
    }

    static public Date setFirstTime(Date date) {
        LocalDateTime date1 = convertToLocalDateTimeViaInstant(date);
        LocalDateTime date2 = LocalDateTime.of(date1.getYear(), date1.getMonthValue(), date1.getDayOfMonth(), 0, 0,
                0);
        return fromLdt(date2);
    }
    static public Date setLastTime(Date date) {
        LocalDateTime date1 = convertToLocalDateTimeViaInstant(date);
        LocalDateTime date2 = LocalDateTime.of(date1.getYear(), date1.getMonthValue(), date1.getDayOfMonth(), 23, 59,
                59);
        return fromLdt(date2);
    }

}
