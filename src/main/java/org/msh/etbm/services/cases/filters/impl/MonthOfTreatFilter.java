package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.filters.FilterTypes;
import org.msh.etbm.commons.indicators.keys.Key;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.services.cases.filters.CaseFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 23/12/16.
 */
public class MonthOfTreatFilter extends AbstractFilter {

    public MonthOfTreatFilter() {
        super(CaseFilters.MONTH_OF_TREAT, "${manag.reportgen.var.monthtreat}");
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        if (value == null) {
            return;
        }

        final StringBuilder s = new StringBuilder();
        iterateValues(value, it -> {
            if (s.length() > 0) {
                s.append(" or ");
            }

            int month = it instanceof String ?
                    Integer.parseInt((String)it) :
                    ((Number)it).intValue();

            if (month == 36) {
                s.append("(timestampdiff(month, tbcase.initreatmentdate, tbcase.endtreatmentdate) >= " + month + ")");
            } else {
                s.append("(timestampdiff(month, tbcase.initreatmentdate, tbcase.endtreatmentdate) = " + month + ")");
            }
        });

        String sql = "(" + s.toString() + ")";
        def.restrict(sql);
    }

    @Override
    public String getFilterType() {
        return FilterTypes.MULTI_SELECT;
    }

    @Override
    public List<Item> getOptions() {
        List<Item> lst = new ArrayList<Item>();
        for (int i = 1; i <= 36; i++) {
            lst.add(new Item(i, Integer.toString(i)));
        }
        lst.add(new Item(37, getMessages().format("manag.reportgen.over", 36)));
        return lst;
    }

    @Override
    public void prepareVariableQuery(QueryDefs def, int iteration) {
        def.select("timestampdiff(month, tbcase.initreatmentdate, tbcase.endtreatmentdate)");
        def.restrict("tbcase.endtreatmentdate is not null");
    }

    @Override
    public Key createKey(Object[] values, int iteration) {
        if (values[0] == null) {
            return Key.of(0L);
        }

        Long val = ((Long)values[0]) + 1;

        if (val > 36) {
            val = 37L;
        }

        return Key.of(val);
    }

    @Override
    public String getKeyDisplay(Key key) {
        // if it's null, return undefined
        if (key.isNull()) {
            return super.getKeyDisplay(key);
        }

        if (key.getValue().equals(37L)) {
            return getMessages().format("manag.reportgen.over", 36);
        }

        // if range is not found, return undefined
        return super.getKeyDisplay(key);
    }
}
