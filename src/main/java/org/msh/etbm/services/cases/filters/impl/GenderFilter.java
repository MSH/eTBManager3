package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.db.enums.Gender;
import org.msh.etbm.services.cases.filters.FilterGroup;

import java.util.Map;

/**
 * Created by rmemoria on 19/8/16.
 */
public class GenderFilter extends EnumFilter {
    public GenderFilter() {
        super(FilterGroup.DATA, Gender.class, "gender", "${Gender}", null);
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        def.join("patient", "patient.id = tbcase.id")
                .restrict(sqlRestriction("patient.gender", value));
    }
}
