package org.msh.etbm.commons.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Set of date and time utilities used throughout the app
 */
public class DateUtils {

    /**
     * Private method to avoid creation of instances of this class
     */
    private DateUtils() {
        super();
    }

    /**
     * Return the number of days between two dates
     *
     * @param a initial date
     * @param b ending date
     * @return number of days
     */
    public static int daysBetween(Date a, Date b) {
        int difference = 0;
        Calendar earlier = Calendar.getInstance();
        Calendar later = Calendar.getInstance();

        if (a.compareTo(b) < 0) {
            earlier.setTime(a);
            later.setTime(b);
        } else {
            earlier.setTime(b);
            later.setTime(a);
        }

        while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR)) {
            int tempDifference = 365 * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
            difference += tempDifference;

            earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
        }

        if (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR)) {
            int tempDifference = later.get(Calendar.DAY_OF_YEAR) - earlier.get(Calendar.DAY_OF_YEAR);
            difference += tempDifference;

            earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
        }

        return difference;
    }


    /**
     * Returns the dt parameter, incremented by <i>numDays</i> days
     *
     * @param dt
     * @param numDays
     * @return
     */
    public static Date incDays(Date dt, int numDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DAY_OF_MONTH, numDays);

        // fix bug in java... when incrementing in 1 day the date oct 17,2009, the hour information were changed
        c.set(Calendar.HOUR_OF_DAY, 0);

        return c.getTime();
    }


    /**
     * Returns the dt parameter, incremented by <i>numMonths</i> months
     *
     * @param dt
     * @param numMonths
     * @return
     */
    public static Date incMonths(Date dt, int numMonths) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, numMonths);
        return c.getTime();
    }


    /**
     * Returns the dt parameter, incremented by <i>numMonths</i> months
     *
     * @param dt
     * @param numYears
     * @return
     */
    public static Date incYears(Date dt, int numYears) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.YEAR, numYears);
        return c.getTime();
    }


    /**
     * Increments a date <code>dt</code> in <code>numHours</code> hours
     *
     * @param dt
     * @param numHours
     * @return
     */
    public static Date incHours(Date dt, int numHours) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.HOUR_OF_DAY, numHours);
        return c.getTime();
    }

    /**
     * Returns the year part of the date
     *
     * @param dt - Date
     * @return - year of the date dt
     */
    public static int yearOf(Date dt) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        return c.get(Calendar.YEAR);
    }


    /**
     * Returns the month part of the date
     *
     * @param dt - Date
     * @return - year of the given date
     */
    public static int monthOf(Date dt) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        return c.get(Calendar.MONTH);
    }


    /**
     * Returns the year part of the date
     *
     * @param dt - Date
     * @return - year of the date dt
     */
    public static int dayOf(Date dt) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Return number of hours between 2 dates
     *
     * @param first
     * @param second
     * @return
     */
    public static int hoursBetween(Date first, Date second) {
        double milliElapsed = second.getTime() - first.getTime();
        double hoursElapsed = milliElapsed / 3600F / 1000F;
        return Math.round(Math.round(hoursElapsed * 100F) / 100F);
    }


    /**
     * Return the number of minutes between two dates
     *
     * @param dtIni initial date
     * @param dtEnd end date
     * @return minutes between dtIni and dtEnd
     */
    public static int minutesBetween(Date dtIni, Date dtEnd) {
        return DateUtils.secondsBetween(dtIni, dtEnd) / 60;
    }


    /**
     * Return the number of seconds between two dates
     *
     * @param dtIni initial date
     * @param dtEnd end date
     * @return seconds between the two dates
     */
    public static int secondsBetween(Date dtIni, Date dtEnd) {
        long milliElapsed = dtIni.getTime() - dtEnd.getTime();
        if (milliElapsed < 0) {
            milliElapsed = -milliElapsed;
        }

        return (int) milliElapsed / 1000;
    }


    /**
     * Returns the number of months between 2 dates
     *
     * @param dt1
     * @param dt2
     * @return
     */
    public static int monthsBetween(Date dt1, Date dt2) {
        Date ini = dt1,
                end = dt2;

        if (ini.after(end)) {
            Date aux = ini;
            ini = end;
            end = aux;
        }

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(ini);
        c2.setTime(end);

        int y = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        int m = (c2.get(Calendar.MONTH) + (y * 12)) - c1.get(Calendar.MONTH);

        if (c2.get(Calendar.DAY_OF_MONTH) < c1.get(Calendar.DAY_OF_MONTH)) {
            m--;
        }

        return m;
    }

    /**
     * Return the number of years between two dates
     *
     * @param dt1
     * @param dt2
     * @return
     */
    public static int yearsBetween(Date dt1, Date dt2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(dt1);
        c2.setTime(dt2);

        if (c1.compareTo(c2) > 0) {
            Calendar aux = c1;
            c1 = c2;
            c2 = aux;
        }
        int num = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);

        if (num > 0 && c1.get(Calendar.DAY_OF_YEAR) > c2.get(Calendar.DAY_OF_YEAR)) {
            num--;
        }

        return num;
    }

    public static String FormatDateTime(String dateFormat, Date dt) {
        SimpleDateFormat f = new SimpleDateFormat(dateFormat);
        return f.format(dt);
    }

    /**
     * Return the current date without time information
     *
     * @return
     */
    public static Date getDate() {
        Calendar c = Calendar.getInstance();
        Calendar dtNow = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR, dtNow.get(Calendar.YEAR));
        c.set(Calendar.MONTH, dtNow.get(Calendar.MONTH));
        c.set(Calendar.DAY_OF_MONTH, dtNow.get(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * Remove time information of a {@link Date} object
     *
     * @param dt date to remove time information
     * @return {@link Date} instance without time information
     */
    public static Date getDatePart(Date dt) {
        Calendar c = Calendar.getInstance();
        Calendar aux = Calendar.getInstance();
        aux.setTime(dt);
        c.clear();
        c.set(Calendar.YEAR, aux.get(Calendar.YEAR));
        c.set(Calendar.MONTH, aux.get(Calendar.MONTH));
        c.set(Calendar.DAY_OF_YEAR, aux.get(Calendar.DAY_OF_YEAR));

        return c.getTime();
    }


    /**
     * Return the day of the week, starting in sun = 1
     *
     * @param dt Date to return the week day
     * @return week day
     */
    public static int dayOfWeek(Date dt) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        return c.get(Calendar.DAY_OF_WEEK);
    }


    /**
     * Calculate the day of the month according to the month, year, week in the month and weekday
     *
     * @param year
     * @param month
     * @param aweek
     * @param weekday - 1 is the first day of the week and 7 is the last day of the week
     * @return
     */
    public static int calcMonthDay(int year, int month, int aweek, int weekday) {
        int week = aweek;

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 1);
        int wd = c.get(Calendar.DAY_OF_WEEK);

        wd = weekday - wd + 1;
        if (wd <= 0) {
            if (week == 1) {
                return 1;
            }
            wd += 7;
            week--;
        }

        if (week == 1) {
            return wd;
        }

        int max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int d = wd + (week - 1) * 7;

        return d > max ? max : d;
    }


    /**
     * Returns the number of days in a specific month/year
     *
     * @param year
     * @param month
     * @return number of days in the month/year
     */
    public static int daysInAMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    /**
     * Create a new instance of a Date class
     *
     * @param year
     * @param month
     * @param day
     * @return date object
     */
    public static Date newDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }


    public static String formatDate(Date dt, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format(dt);
    }


    /**
     * Return the difference between two dates. The result is stored in an instance
     * of {@link Calendar} class
     *
     * @param d1
     * @param d2
     * @return
     */
    public static Calendar calcDifference(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);

        long len = c1.getTimeInMillis() - c2.getTimeInMillis();
        if (len < 0) {
            len = -len;
        }

        c1.setTimeInMillis(len);
        return c1;
    }


    /**
     * Check if a data has time information
     *
     * @param dt1
     * @return
     */
    public static boolean hasTimePart(Date dt1) {
        if (dt1 instanceof java.sql.Date) {
            return false;
        }

        Calendar c = Calendar.getInstance();
        return (c.get(Calendar.HOUR) > 0) || (c.get(Calendar.MINUTE) > 0) || (c.get(Calendar.SECOND) > 0) || (c.get(Calendar.MILLISECOND) > 0);
    }
}
