package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.filters.FilterException;
import org.msh.etbm.commons.filters.FilterTypes;
import org.msh.etbm.commons.filters.UnexpectedFilterException;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.sqlquery.QueryDefs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * Define a filter for date fields
 *
 * Created by rmemoria on 16/11/16.
 */
public class PeriodFilter extends AbstractFilter {

    /**
     * Filter types
     */
    public enum PeriodType { FIXED, DAILY, MONTHLY, QUARTERLY, YEARLY }

    /**
     * Possible values for fixed periods
     * @author Ricardo Memoria
     *
     */
    private enum FixedPeriod {
        LAST_3MONTHS,
        LAST_6MONTHS,
        LAST_12MONTHS,
        PREVIOUS_QUARTER,
        PREVIOUS_YEAR
    }


    private static final String RES_TYPE = "type";

    /**
     * The field name of type date used in the query. May include the table name in the format "table.field"
     */
    private String fieldName;

    /**
     * The type of the period
     */
    private PeriodType type;


    /**
     * Default constructor
     * @param id the filter ID
     * @param label the label in the format used by {@link org.msh.etbm.commons.Messages#eval(String)}
     * @param fieldName the field name
     * @param type The type of the period to be analysed
     */
    public PeriodFilter(String id, String label, String fieldName, PeriodType type) {
        super(id, label);
        this.fieldName = fieldName;
        if (type == PeriodType.FIXED) {
            throw new UnexpectedFilterException("Period type not supported: " + type);
        }
        this.type = type;
    }


    @Override
    public Map<String, Object> getResources(Map<String, Object> params) {
        return Collections.singletonMap(RES_TYPE, type.toString());
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        // check if filter has the right value
        if (!(value instanceof Map)) {
            throw new FilterException("Invalid value type for " + this.getClass() + " value = " + value);
        }
        Map<String, Object> valueParams = (Map<String, Object>)value;

        // make any join, if necessary
        String[] s = fieldName.split("\\.");
        if (s.length > 1) {
            def.join(s[0]);
        }

        // get the type of period to calculate
        String typeValueName = (String)valueParams.get("type");
        PeriodType ptype = ObjectUtils.stringToEnum(typeValueName, PeriodType.class);

        if (ptype == null) {
            throw new FilterException("Invalid filter type: " + typeValueName);
        }

        String ini = (String)valueParams.get("ini");
        String end = (String)valueParams.get("end");
        String fixedValue = (String)valueParams.get("value");

        switch (ptype) {
            case FIXED:
                prepareFixedFilter(def, fixedValue);
                return;
            case DAILY:
                prepareDailyFilter(def, ini, end);
                return;
            case MONTHLY:
                prepareMonthlyFilter(def, ini, end);
                return;
            case QUARTERLY:
                prepareQuarterlyFilter(def, ini, end);
                return;
            case YEARLY:
                prepareYearlyFilter(def, ini, end);
                return;
            default: throw new FilterException("Filter type not supported: " + ptype);
        }
    }

    /**
     * Prepare the filter to include restrictions of a fixed value
     * @param def
     * @param fval
     */
    private void prepareFixedFilter(QueryDefs def, String fval) {
        FixedPeriod fperiod = ObjectUtils.stringToEnum(fval, FixedPeriod.class);
        if (fperiod == null) {
            throw new FilterException("Invalid fixed period value: " + fval);
        }

        Date dtIni, dtEnd;

        switch (fperiod) {
            case LAST_3MONTHS:
                dtEnd = DateUtils.getDate();
                dtIni = DateUtils.incMonths(dtEnd, -3);
                break;
            case LAST_6MONTHS:
                dtEnd = DateUtils.getDate();
                dtIni = DateUtils.incMonths(dtEnd, -6);
                break;
            case LAST_12MONTHS:
                dtEnd = DateUtils.getDate();
                dtIni = DateUtils.incMonths(dtEnd, -12);
                break;
            case PREVIOUS_QUARTER:
                Period p = calcPreviousQuarter();
                dtEnd = p.getEndDate();
                dtIni = p.getIniDate();
                break;
            case PREVIOUS_YEAR:
                Period p2 = calcPreviousYear();
                dtEnd = p2.getEndDate();
                dtIni = p2.getIniDate();
                break;
            default:
                throw new FilterException("Not supported fixed type: " + fperiod);
        }

        def.restrict(fieldName + " between ? and ?", dtIni, dtEnd);
    }


    /**
     * Apply filter by a period of dates
     * @param def the instance of {@link QueryDefs}
     * @param ini the initial date in the format YYYY-MM-DD
     * @param end the final date in the format YYYY-MM-DD
     */
    private void prepareDailyFilter(QueryDefs def, String ini, String end) {
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
        try {
            Date dtIni = formatter.parse(ini);
            Date dtEnd = formatter.parse(end);
            def.restrict(fieldName + " between ? and ?", dtIni, dtEnd);
        } catch (ParseException e) {
            throw new FilterException("Invalid date period: " + ini + ", " + end);
        }
    }

    /**
     * Apply filter by a given initial and final year-month
     * @param def the instance of {@link QueryDefs}
     * @param ini the initial date in the format YYYY-MM
     * @param end the final date in the format YYYY-MM
     */
    private void prepareMonthlyFilter(QueryDefs def, String ini, String end) {
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM");
        Date dtini;
        try {
            dtini = formatter.parse(ini);
        } catch (ParseException e) {
            throw new FilterException("Invalid initial year-month value: " + ini);
        }

        Date dtend;
        try {
            dtend = formatter.parse(end);
            dtend = createEndDate(dtend);
        } catch (ParseException e) {
            throw new FilterException("Invalid final year-month value: " + end);
        }

        def.restrict(fieldName + " between ? and ?", dtini, dtend);
    }


    /**
     * Restrict the query by the given quarter
     * @param def instance of {@link QueryDefs}
     * @param ini the initial quarter in the format YYYY-Q
     * @param end the final quarter in the format YYYY-Q
     */
    private void prepareQuarterlyFilter(QueryDefs def, String ini, String end) {
        int[] iniVals = parseQuarter(ini);
        if (iniVals == null) {
            throw new RuntimeException("Invalid initial quarter definition (YYYY-Q): " + ini);
        }

        int[] endVals = parseQuarter(end);
        if (endVals == null) {
            throw new RuntimeException("Invalid initial quarter definition (YYYY-Q): " + end);
        }

        // calc ini date of the quarter
        int iniYear = iniVals[0];
        int iniMonth = (iniVals[1] - 1) * 3;
        Date iniDate = DateUtils.newDate(iniYear, iniMonth, 1);

        int endYear = endVals[0];
        int endMonth = ((endVals[1] - 1) * 3) + 2;
        Date endDate = DateUtils.newDate(endYear, endMonth, DateUtils.daysInAMonth(endYear, endMonth));

        def.restrict(fieldName + " between ? and ?", iniDate, endDate);
    }

    /**
     * Parse a quarter in a string in the format YYYY-Q
     * @param val the quarter in string format
     * @return an array containing the year and quarter, or null if value is invalid
     */
    private int[] parseQuarter(String val) {
        if (val == null) {
            return null;
        }

        String[] s = val.split("-");
        if (s.length != 2) {
            return null;
        }

        try {
            int year = Integer.parseInt(s[0]);
            if (year < 1900) {
                return null;
            }

            int quarter = Integer.parseInt(s[1]);
            if (quarter < 1 || quarter > 4) {
                return null;
            }

            int[] res = { year, quarter };
            return res;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Prepare filter by year range
     * @param defs instance of {@link QueryDefs} to receive the query restrictions
     * @param ini the initial year, in string format
     * @param end the final year, in string format
     */
    private void prepareYearlyFilter(QueryDefs defs, String ini, String end) {
        Integer iniYear = null;
        Integer endYear = null;

        try {
            iniYear = Integer.parseInt(ini);
        } catch (NumberFormatException e) {
            throw new FilterException("Invalid initial year: " + ini);
        }

        try {
            endYear = Integer.parseInt(end);
        } catch (NumberFormatException e) {
            throw new FilterException("Invalid final year: " + end);
        }

        defs.restrict("year(" + fieldName + ") = ? and year(" + fieldName + ") = ?", iniYear, endYear);
    }

    /**
     * Calculate the previous year
     * @return period within the previous year
     */
    private Period calcPreviousYear() {
        Date dt = DateUtils.getDate();
        int year = DateUtils.yearOf(dt) - 1;

        Date ini = DateUtils.newDate(year, 0, 1);
        Date end = DateUtils.newDate(year, 11, DateUtils.daysInAMonth(year, 11));
        return new Period(ini, end);
    }


    /**
     * Calculate the initial and final date of the previous quarter
     * @return period of the previous quarter
     */
    protected Period calcPreviousQuarter() {
        Date dt = DateUtils.getDate();
        int month = DateUtils.monthOf(dt);
        int year = DateUtils.yearOf(dt);
        // calculate the previous quarter
        int quarter = (month / 4) - 1;
        if (quarter == -1) {
            quarter = 3;
            year--;
        }
        // calculate the initial and final date of the quarter
        Date ini = DateUtils.newDate(year, quarter * 3, 1);
        Date end = DateUtils.newDate(year, (quarter * 3) + 2, 1);

        return new Period(ini, createEndDate(end));
    }

    /**
     * Return the initial date of a period just setting the day of the month
     * of the given date to 1
     * @param date date of the initial date
     * @return date set its day of month to 1
     */
    protected Date createIniDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return DateUtils.getDatePart(c.getTime());
    }

    /**
     * Return the final date of the period. Using the given date, it returns
     * the date with the last day of the month
     * @param date
     * @return
     */
    protected Date createEndDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, DateUtils.daysInAMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH)));
        return c.getTime();
    }



    @Override
    public void prepareVariableQuery(QueryDefs def, int iteration) {
        String[] s = fieldName.split("\\.");
        if (s.length > 1) {
            def.join(s[0]);
        }

        def.select("year(" + fieldName + ")");

        if (type == PeriodType.QUARTERLY || type == PeriodType.MONTHLY) {
            def.select("month(" + fieldName + ")");
        }
    }

    @Override
    public String getFilterType() {
        return FilterTypes.PERIOD;
    }


    @Override
    public String valueToDisplay(Object value) {
        return null;
    }
}
