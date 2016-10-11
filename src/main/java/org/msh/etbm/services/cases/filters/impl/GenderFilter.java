package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.db.enums.Gender;
import org.msh.etbm.services.cases.filters.CaseFilters;

import java.util.Map;

/**
 * Created by rmemoria on 19/8/16.
 */
public class GenderFilter extends EnumFilter {
    public GenderFilter() {
        super(CaseFilters.GENDER, Gender.class, "${Gender}", "patient.gender");
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        addEnumRestrictions(def.join("patient"), value);
    }

    @Override
    public void prepareVariableQuery(QueryDefs def, int iteration) {
        def.join("patient").select("gender");
    }
}
