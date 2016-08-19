package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.db.enums.ValidationState;
import org.msh.etbm.services.cases.filters.FilterGroup;

import java.util.Map;

/**
 * Created by rmemoria on 17/8/16.
 */
public class ValidationStateFilter extends EnumFilter<ValidationState> {

    public ValidationStateFilter() {
        super(FilterGroup.DATA, "validation-state", "${ValidationState}");
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        String s = sqlRestriction("tbcase.validationState", value);
        def.restrict(s);
    }

    @Override
    public String getFilterType() {
        return null;
    }
}
