package org.msh.etbm.test.commons;

import org.junit.Test;
import org.msh.etbm.commons.date.DateUtils;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;


/**
 * Simple tests with the {@link org.msh.etbm.commons.date.DateUtils} class
 * Created by rmemoria on 10/8/16.
 */
public class DateUtilsTest {

    @Test
    public void createDate() {
        Date dt = DateUtils.newDate(2015, 0, 1);

        Calendar c = Calendar.getInstance();
        c.setTime(dt);

        assertEquals(2015, c.get(Calendar.YEAR));
        assertEquals(0, c.get(Calendar.MONTH));
        assertEquals(1, c.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testMonthsBetween() {
        Date dtini = DateUtils.newDate(2015, 0, 1);
        Date dtend = DateUtils.newDate(2015, 0, 15);

        // less than 1 month
        int months = DateUtils.monthsBetween(dtini, dtend);
        assertEquals(0, months);

        // almost 1 month
        dtend = DateUtils.newDate(2015, 0, 31);
        months = DateUtils.monthsBetween(dtini, dtend);
        assertEquals(0, months);

        // exactly 1 month
        dtend = DateUtils.newDate(2015, 1, 1);
        months = DateUtils.monthsBetween(dtini, dtend);
        assertEquals(1, months);

        // 1 month + 1 day
        dtend = DateUtils.newDate(2015, 1, 2);
        months = DateUtils.monthsBetween(dtini, dtend);
        assertEquals(1, months);

        // 12 months
        dtend = DateUtils.newDate(2016, 0, 1);
        months = DateUtils.monthsBetween(dtini, dtend);
        assertEquals(12, months);
    }
}
