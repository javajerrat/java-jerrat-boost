/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.github.javajerrat.boost.basetools.datetime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.jetbrains.annotations.NotNull;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/11/13
 */
public class Dates extends DateUtils {

    /**
     * Returns the nearest future time that satisfies the given point.
     * @param hour hour
     * @param min min
     * @param seconds seconds
     * @param ms ms
     */
    public static Date nextNearDay(int hour, int min, int seconds, int ms) {
        return nextNearDay(TimeZone.getDefault(), hour, min, seconds, ms);
    }

    /**
     * Returns the nearest future time that satisfies the given point.
     * @param timeZone timeZone
     * @param hour hour
     * @param min min
     * @param seconds seconds
     * @param ms ms
     */
    public static Date nextNearDay(TimeZone timeZone, int hour, int min, int seconds, int ms) {
        Date now = new Date();
        Date nextDate = Dates.setTime(now, timeZone, hour, min, seconds, ms);
        if (nextDate.compareTo(now) <= 0) {
            nextDate = DateUtils.addDays(nextDate, 1);
        }
        return nextDate;

    }

    /**
     * Returns the nearest future time that satisfies the given point.
     * @param day day
     * @param hour hour
     * @param min min
     * @param seconds seconds
     * @param ms ms
     */
    public static Date nextNearMonth(int day, int hour, int min, int seconds, int ms) {
        return nextNearMonth(TimeZone.getDefault(), day, hour, min, seconds, ms);
    }

    /**
     * Returns the nearest future time that satisfies the given point.
     * @param timeZone timeZone
     * @param day day
     * @param hour hour
     * @param min min
     * @param seconds seconds
     * @param ms ms
     */
    public static Date nextNearMonth(TimeZone timeZone, int day, int hour, int min, int seconds, int ms) {
            Date now = new Date();
            Dates.DateTuple dateTuple = Dates.getDateTuple(now, timeZone);
            Date nextDate = Dates.setDate(now, timeZone, dateTuple.year, dateTuple.month, day);
            nextDate = Dates.setTime(nextDate, timeZone, hour, min, seconds, ms);
            if (nextDate.compareTo(now) < 0) {
                nextDate = DateUtils.addMonths(nextDate, 1);
            }
            return nextDate;
    }

    /**
     * Calculate time delta in milliseconds.
     * @param date1 subtrahend date
     * @param date2 minuend date
     * @return time delta
     */
    public static long interval(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }

    /**
     * Convert the date object to a more readable string.
     * This function is designed for debugging.
     * @param date date
     * @return display string
     */
    public static String toDisplayString(@NotNull Date date) {
        Objects.requireNonNull(date);
        return DateFormatUtils.format(date, DateFormats.YYYY_MM_DD_HH_MM_SS_SSS) + " " + displayTimeZone(TimeZone.getDefault());
    }

    private static String displayTimeZone(TimeZone timeZone) {
        // Thanks for https://mkyong.com/java/java-display-list-of-timezone-with-gmt/
        long hours = TimeUnit.MILLISECONDS.toHours(timeZone.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeZone.getRawOffset())
            - TimeUnit.HOURS.toMinutes(hours);
        // avoid -4:-30 issue
        minutes = Math.abs(minutes);
        if (hours > 0) {
            return String.format("(GMT+%d:%02d) %s", hours, minutes, timeZone.getID());
        } else {
            return String.format("(GMT%d:%02d) %s", hours, minutes, timeZone.getID());
        }
    }

    /**
     * Similar to {@link DateUtils#parseDate(String, String...)}, but the difference is that Time zone can be customized.
     * @param str  the date to parse, not null
     * @param timeZone timeZone
     * @param parsePattern  the date format patterns to use, see SimpleDateFormat, not null
     * @return the parsed date
     * @throws ParseException if none of the date patterns were suitable (or there were none)
     */
    public static Date parseDate(String str, TimeZone timeZone, String parsePattern) throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance(parsePattern, timeZone);
        return fastDateFormat.parse(str);
    }

    /**
     * <p>Formats a date/time into a specific pattern.</p>
     *
     * @param date  the date to format, not null
     * @param pattern  the pattern to use to format the date, not null
     * @return the formatted date
     */
    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * <p>Formats a date/time into a specific pattern in a time zone.</p>
     *
     * @param date  the date to format, not null
     * @param pattern  the pattern to use to format the date, not null
     * @param timeZone  the time zone  to use, may be <code>null</code>
     * @return the formatted date
     */
    public static String format(Date date, String pattern, TimeZone timeZone) {
        return DateFormatUtils.format(date, pattern, timeZone);
    }

    /**
     * <p>Formats a date/time into a specific pattern in a time zone  and locale.</p>
     *
     * @param date  the date to format, not null
     * @param pattern  the pattern to use to format the date, not null, not null
     * @param timeZone  the time zone  to use, may be <code>null</code>
     * @param locale  the locale to use, may be <code>null</code>
     * @return the formatted date
     */
    public static String format(Date date, String pattern, TimeZone timeZone, Locale locale) {
        return DateFormatUtils.format(date, pattern, timeZone, locale);
    }

    /**
     * <p>Truncates a date, leaving the field specified as the most
     * significant field.</p>
     *
     * <p>For example, if you had the date-time of 28 Mar 2002
     * 13:45:01.231, if you passed with HOUR, it would return 28 Mar
     * 2002 13:00:00.000.  If this was passed with MONTH, it would
     * return 1 Mar 2002 0:00:00.000.</p>
     *
     * @param date  the date to work with, not null
     * @param field  the field from {@code Calendar} or <code>SEMI_MONTH</code>
     * @return the different truncated date, not null
     * @throws IllegalArgumentException if the date is <code>null</code>
     * @throws ArithmeticException if the year is over 280 million
     */
    public static Date truncate(final Date date, TimeZone timeZone, final int field) {
        // Thanks for https://stackoverflow.com/questions/28266988/dateutilstruncate-to-work-in-utc-timezone
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        return DateUtils.truncate(calendar, field).getTime();
    }

    /**
     * <p>Rounds a date, leaving the field specified as the most
     * significant field.</p>
     *
     * @param date  the date to work with, not null
     * @param field  the field from {@code Calendar} or <code>SEMI_MONTH</code>
     * @return the different rounded date, not null
     * @throws IllegalArgumentException if the date is <code>null</code>
     * @throws ArithmeticException if the year is over 280 million
     */
    public static Date round(final Date date, TimeZone timeZone, final int field) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        return DateUtils.round(calendar, field).getTime();
    }

    /**
     * <p>Gets a date ceiling, leaving the field specified as the most
     * significant field.</p>
     *
     * @param date  the date to work with, not null
     * @param field  the field from {@code Calendar} or <code>SEMI_MONTH</code>
     * @return the different ceil date, not null
     * @throws IllegalArgumentException if the date is <code>null</code>
     * @throws ArithmeticException if the year is over 280 million
     */
    public static Date ceiling(final Date date, TimeZone timeZone, final int field) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        return DateUtils.ceiling(calendar, field).getTime();
    }

    @AllArgsConstructor
    @ToString
    public static class DateTuple {
        public final int year;
        public final int month;
        public final int day;
        public final int hour;
        public final int minute;
        public final int second;
        public final int millisecond;
    }

    /**
     * Gets a tuple containing year, month, day, hour, minute, second and millisecond.
     * @param date date
     * @return a tuple
     */
    public static DateTuple getDateTuple(Date date) {
        return getDateTuple(date, TimeZone.getDefault());
    }

    /**
     * Gets a tuple containing year, month, day, hour, minute, second and millisecond.
     * @param date date
     * @return a tuple
     */
    public static DateTuple getDateTuple(Date date, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        return new DateTuple(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND),
            calendar.get(Calendar.MILLISECOND));
    }


    /**
     * Set the hours, minutes, and seconds of the date
     * @param date date
     * @return A new date
     */
    public static Date setTime(Date date, TimeZone timeZone, int hour, int min, int seconds) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * Set the hours, minutes, seconds and milliseconds of the date
     * @param date date
     * @return A new date
     */
    public static Date setTime(Date date, int hour, int min, int seconds, int ms) {
        return setTime(date, TimeZone.getDefault(), hour, min, seconds, ms);
    }

    /**
     * Set the hours, minutes, seconds and milliseconds of the date
     * @param date date
     * @return A new date
     */
    public static Date setTime(Date date, TimeZone timeZone, int hour, int min, int seconds, int ms) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, ms);
        return calendar.getTime();
    }

    /**
     * Set the hours, minutes and seconds of the date
     * @param date date
     * @return A new date
     */
    public static Date setTime(Date date, int hour, int min, int seconds) {
        return setTime(date, TimeZone.getDefault(), hour, min, seconds);
    }

    /**
     * Set the year, month and day of the date
     * @param date date
     * @return A new date
     */
    public static Date setDate(Date date, int year, int month, int day) {
        return setDate(date, TimeZone.getDefault(), year, month, day);
    }

    /**
     * Set the year, month and day of the date
     * @param date date
     * @return A new date
     */
    public static Date setDate(Date date, TimeZone timeZone, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * Whether the given two dates are the same year and month.
     * @param date1 date
     * @param date2 date
     * @return True if they are the same, false otherwise
     */
    public static boolean isSameMonth(Date date1, Date date2) {
        return isSameMonth(date1, date2, TimeZone.getDefault());
    }

    /**
     * Whether the given two dates are the same year and month.
     * @param timeZone timeZone
     * @param date1 date
     * @param date2 date
     * @return True if they are the same, false otherwise
     */
    public static boolean isSameMonth(Date date1, Date date2, TimeZone timeZone) {
        DateTuple dateTuple1 = getDateTuple(date1, timeZone);
        DateTuple dateTuple2 = getDateTuple(date2, timeZone);
        return dateTuple1.year == dateTuple2.year && dateTuple1.month == dateTuple2.month;
    }

    /**
     * @return Returns the current timestamp
     */
    public static long timestamp() {
        return System.currentTimeMillis();
    }

    /**
     * This function is only used in special cases and fixes some historical errors.
     * A date is parsed in a wrong time zone given by the <code>fromTimeZone</code> parameter,
     * and corrected to the correct time zone given by the <code>toTimeZone</code> parameter.
     * @param date date
     * @param fromTimeZone fromTimeZone
     * @param toTimeZone toTimeZone
     * @return Corrected time zone
     */
    public static Date correctTimeZone(Date date, TimeZone fromTimeZone, TimeZone toTimeZone) {
        // Thanks for https://stackoverflow.com/questions/2891361/how-to-set-time-zone-of-a-java-util-date
        long fromTimeZoneOffset = getTimeZoneUTCAndDSTOffset(date, fromTimeZone);
        long toTimeZoneOffset = getTimeZoneUTCAndDSTOffset(date, toTimeZone);

        return new Date(date.getTime() + (toTimeZoneOffset - fromTimeZoneOffset));
    }

    /**
     * Calculates the offset of the <code>timeZone</code> from UTC, factoring in any
     * additional offset due to the time zone being in daylight savings time as of
     * the given <code>date</code>.
     * @param date date
     * @param timeZone timeZone
     * @return the offset
     */
    private static long getTimeZoneUTCAndDSTOffset(Date date, TimeZone timeZone) {
        return timeZone.getRawOffset() +
            (timeZone.inDaylightTime(date) ? timeZone.getDSTSavings() : 0);
    }
}
