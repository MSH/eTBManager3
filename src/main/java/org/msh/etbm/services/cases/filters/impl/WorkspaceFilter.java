package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.filters.FilterTypes;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Created by rmemoria on 5/10/16.
 */
public class WorkspaceFilter extends AbstractFilter {

    public WorkspaceFilter() {
        super("workspace", "${Workspace}");
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        def.restrict("$root.workspace_id = ?", value);
    }

    @Override
    public String getFilterType() {
        return FilterTypes.SELECT;
    }
}
