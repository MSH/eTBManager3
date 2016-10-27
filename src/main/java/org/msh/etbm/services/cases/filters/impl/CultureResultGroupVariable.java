package org.msh.etbm.services.cases.filters.impl;


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
    public String createKey(Object value) {
        if (value == null) {
            return ResultGroup.PENDING.toString();
        }

        if (!(value instanceof Number)) {
            return super.createKey(value);
        }

        CultureResult res = CultureResult.values()[((Number)value).intValue()];

        if (res.isPositive()) {
            return CultureResult.POSITIVE.toString();
        }

        if (res.isNegative()) {
            return CultureResult.NEGATIVE.toString();
        }

        if (res == CultureResult.CONTAMINATED) {
            return res.toString();
        }

        return CultureResult.OTHER.toString();
    }
}
