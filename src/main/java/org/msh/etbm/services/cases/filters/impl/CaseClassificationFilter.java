package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.services.cases.filters.FilterGroup;

import java.util.Map;

/**
 * Created by rmemoria on 17/8/16.
 */
public class CaseClassificationFilter extends EnumFilter<CaseClassification> {

    public CaseClassificationFilter() {
        super(FilterGroup.DATA, "case-classif", "${CaseClassification}");
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        String s = sqlRestriction("tbcase.classification", value);
        def.restrict(s);
    }

}
