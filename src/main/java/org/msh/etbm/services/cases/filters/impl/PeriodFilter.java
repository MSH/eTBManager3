package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.filters.FilterException;
import org.msh.etbm.commons.filters.FilterTypes;
import org.msh.etbm.commons.indicators.keys.Key;
import org.msh.etbm.commons.indicators.variables.VariableOptions;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.sqlquery.QueryDefs;

import java.text.DateFormatSymbols;
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
    public enum PeriodFilterType {
        CONSTANT,
        DATE,
        QUARTER
    }

    /**
     * Variables type
     */
    public enum PeriodVariableType {
        MONTHLY, QUARTERLY, YEARLY
    }

    /**
     * Possible values for fixed periods
     * @author Ricardo Memoria
     *
     */
    private enum ConstantPeriod {
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
    private PeriodVariableType type;


    /**
     * Default constructor
     * @param id the filter ID
     * @param label the label in the format used by {@link org.msh.etbm.commons.Messages#eval(String)}
     * @param fieldName the field name
     * @param type The type of the period to be analysed
     */
    public PeriodFilter(String id, String label, String fieldName, PeriodVariableType type) {
        super(id, label);
        this.fieldName = fieldName;
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
        PeriodFilterType ptype = ObjectUtils.stringToEnum(typeValueName, PeriodFilterType.class);

        if (ptype == null) {
            throw new FilterException("Invalid filter type: " + typeValueName);
        }

        switch (ptype) {
            case CONSTANT:
                String fixedValue = (String)valueParams.get("value");
                prepareFixedFilter(def, fixedValue);
                return;
            case DATE:
                prepareDateFilter(def, valueParams);
                return;
            case QUARTER:
                prepareQuarterlyFilter(def, valueParams);
                return;
            default: throw new FilterException("Filter type not supported: " + ptype);
        }
    }

    private void prepareDateFilter(QueryDefs def, Map<String, Object> params) {
        Period p = parseIniEndDates(params);

        if (p == null) {
            p = parsePeriodDescription(params);
        }

        if (p == null) {
            return;
        }

        if (p.getIniDate() == null) {
            def.restrict(fieldName + " <= ?", p.getEndDate());
            return;
        }

        if (p.getEndDate() == null) {
            def.restrict(fieldName + " >= ?", p.getIniDate());
            return;
        }

        def.restrict(fieldName + " between ? and ?", p.getIniDate(), p.getEndDate());
    }

    private Date parseDate(String s, boolean initial) {
        if (s == null) {
            return null;
        }

        String[] p = s.split("-");

        try {
            int year = Integer.parseInt(p[0]);
            if (year < 1900 || year > 2050) {
                throwInvalidDate(s);
            }

            int month = p.length > 1 ? Integer.parseInt(p[1]) : 1;
            if (month < 1 || month > 12) {
                throwInvalidDate(s);
            }

            int daysInAMonth = DateUtils.daysInAMonth(year, month);
            int defaultDay = initial ? 1 : daysInAMonth;

            int day = p.length > 2 ? Integer.parseInt(p[2]) : defaultDay;
            if (day < 1 || day > DateUtils.daysInAMonth(year, month)) {
                throwInvalidDate(s);
            }

            return DateUtils.newDate(year, month - 1, day);
        } catch (NumberFormatException e) {
            throwInvalidDate(s);
        }

        return null;
    }

    private Period parsePeriodDescription(Map<String, Object> params) {
        Integer iniMonth = (Integer)params.get("iniMonth");
        Integer iniYear = (Integer)params.get("iniYear");
        Integer endMonth = (Integer)params.get("endMonth");
        Integer endYear = (Integer)params.get("endYear");

        if (iniMonth == null && iniYear == null && endMonth == null && endYear == null) {
            return null;
        }

        Date ini = null;
        if (iniYear != null) {
            if (iniMonth == null) {
                iniMonth = 1;
            }

            ini = DateUtils.newDate(iniYear, iniMonth, 1);
        }

        Date end = null;
        if (endYear != null) {
            if (endMonth == null) {
                endMonth = 12;
            }
            end = DateUtils.newDate(endYear, endMonth, DateUtils.daysInAMonth(endYear, endMonth));
        }

        return new Period(ini, end);
    }

    private Period parseIniEndDates(Map<String, Object> params) {
        String ini = (String)params.get("ini");
        String end = (String)params.get("end");

        if (ini == null && end == null) {
            return null;
        }

        Date dtini = parseDate(ini, true);
        Date dtend = parseDate(end, false);

        return new Period(dtini, dtend);
    }

    private void throwInvalidDate(String date) {
        throw new FilterException("Invalid date: " + date);
    }

    /**
     * Prepare the filter to include restrictions of a fixed value
     * @param def
     * @param fval
     */
    private void prepareFixedFilter(QueryDefs def, String fval) {
        ConstantPeriod fperiod = ObjectUtils.stringToEnum(fval, ConstantPeriod.class);
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
     * Apply a restriction of the date period. If both dates are null, no restriction is
     * applied. If just one date is null, so just one is used in the restriction
     * @param def the instance of {@link QueryDefs}
     * @param dtini the initial date
     * @param dtend the final date
     */
    private void applyDateRestriction(QueryDefs def, Date dtini, Date dtend) {
        if (dtini == null && dtend == null) {
            return;
        }

        if (dtini == null) {
            def.restrict(fieldName + " <= ?", dtend);
            return;
        }

        if (dtend == null) {
            def.restrict(fieldName + " >= ?", dtini);
            return;
        }

        def.restrict(fieldName + " between ? and ?", dtini, dtend);
    }

    /**
     * Restrict the query by the given quarter
     * @param def instance of {@link QueryDefs}
     * @param params the map containing the initial and final quarter in the format YYYY-Q
     */
    private void prepareQuarterlyFilter(QueryDefs def, Map<String, Object> params) {
        String ini = (String)params.get("ini");
        String end = (String)params.get("end");

        int[] iniVals = parseQuarter(ini);
        int[] endVals = parseQuarter(end);

        // calc ini date of the quarter
        Date iniDate = null;
        if (iniVals != null) {
            int iniYear = iniVals[0];
            int iniMonth = (iniVals[1] - 1) * 3;
            iniDate = DateUtils.newDate(iniYear, iniMonth, 1);
        }

        Date endDate = null;
        if (endVals != null) {
            int endYear = endVals[0];
            int endMonth = ((endVals[1] - 1) * 3) + 2;
            endDate = DateUtils.newDate(endYear, endMonth, DateUtils.daysInAMonth(endYear, endMonth));
        }

        applyDateRestriction(def, iniDate, endDate);
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
            throwInvalidDate(val);
        }

        try {
            int year = Integer.parseInt(s[0]);
            if (year < 1900) {
                throwInvalidDate(val);
            }

            int quarter = Integer.parseInt(s[1]);
            if (quarter < 1 || quarter > 4) {
                throwInvalidDate(val);
            }

            int[] res = { year, quarter };
            return res;
        } catch (NumberFormatException e) {
            throwInvalidDate(val);
        }
        return null;
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

        int lastMonth = (quarter * 3) + 2;
        int lastDay = DateUtils.daysInAMonth(year, month);
        Date end = DateUtils.newDate(year, (quarter * 3) + 2, lastDay);

        return new Period(ini, end);
    }

    @Override
    public Key createKey(Object[] values, int iteration) {
        if (values[0] == null) {
            return Key.asNull();
        }

        Integer year = (Integer)values[0];
        Integer month = hasMonth() ? (Integer)values[1] : null;

        return hasMonth() ? Key.of(year, month) : Key.of(year);
    }

    @Override
    public String getKeyDisplay(Key key) {
        if (key.isNull()) {
            return super.getKeyDisplay(key);
        }

        if (hasMonth()) {
            int month = (Integer)key.getValue();

            DateFormatSymbols sym = new DateFormatSymbols();
            return sym.getMonths()[month - 1];
        } else {
            // year only
            int year = (Integer)key.getValue();
            return Integer.toString(year);
        }
    }

    @Override
    public String getGroupKeyDisplay(Key key) {
        Integer year = (Integer)key.getGroup();

        return year != null ? Integer.toString(year) : super.getGroupKeyDisplay(key);
    }

    @Override
    public boolean isGrouped() {
        return true;
    }


    @Override
    public void prepareVariableQuery(QueryDefs def, int iteration) {
        String[] s = fieldName.split("\\.");
        if (s.length > 1) {
            def.join(s[0]);
        }

        def.select("year(" + fieldName + ")");

        if (type == PeriodVariableType.QUARTERLY || type == PeriodVariableType.MONTHLY) {
            def.select("month(" + fieldName + ")");
        }
    }

    /**
     * Return true if variable data contains month information
     * @return boolean value
     */
    private boolean hasMonth() {
        return type == PeriodVariableType.QUARTERLY || type == PeriodVariableType.MONTHLY;
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
