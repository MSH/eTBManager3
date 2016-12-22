package org.msh.etbm.services.cases.filters.impl;


import org.msh.etbm.commons.indicators.keys.Key;
import org.msh.etbm.db.enums.CultureResult;
import org.msh.etbm.services.cases.filters.CaseFilters;

/**
 * Display the culture result grouped by positive, negative, contaminated, pending and others
 * Created by ricardo on 25/08/14.
 */
public class CultureResultGroupVariable extends EnumFilter {

    public enum ResultGroup {
        POSITIVE,
        NEGATIVE,
        CONTAMINATED,
        PENDING,
        OTHER
    }

    public CultureResultGroupVariable() {
        super(CaseFilters.CULTURE_RESULT_GROUP, CultureResult.class, "manag.reportgen.var.cultresgroup", "examculture.result");
    }

    /* (non-Javadoc)
     * @see org.msh.tb.reports2.VariableImpl#createKey(java.lang.Object)
     */
    @Override
    public Key createKey(Object[] values, int iteration) {
        if (values[0] == null) {
            return Key.of(ResultGroup.PENDING);
        }

        int index = (Integer)values[0];

        CultureResult res = CultureResult.values()[index];

        if (res.isPositive()) {
            return Key.of(CultureResult.POSITIVE);
        }

        if (res.isNegative()) {
            return Key.of(CultureResult.NEGATIVE);
        }

        if (res == CultureResult.CONTAMINATED) {
            return Key.of(res);
        }

        return Key.of(CultureResult.OTHER);
    }
}
