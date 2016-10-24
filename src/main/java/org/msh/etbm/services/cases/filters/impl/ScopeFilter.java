package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.filters.FilterException;
import org.msh.etbm.commons.filters.FilterTypes;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
import org.msh.etbm.services.admin.admunits.data.AdminUnitData;

import java.util.Map;

/**
 * A filter used to restrict the data by workspace, administrative unit or unit.
 * Created by rmemoria on 5/10/16.
 */
public class ScopeFilter extends AbstractFilter {

    public ScopeFilter() {
        super("workspace", "${Workspace}");
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        if (!(value instanceof ScopeFilterValue)) {
            throw new FilterException("Not a valid value for a ScopeFilter");
        }

        ScopeFilterValue filterValue = (ScopeFilterValue)value;

        switch (filterValue.getScope()) {
            case WORKSPACE:
                def.restrict("$root.workspace_id = ?", filterValue.getValue());
                break;
            case UNIT:
                def.restrict(filterValue.getUnitFieldName() + " = ?", filterValue.getValue());
                break;
            case ADMINUNIT:
                restrictAdminUnit(def, filterValue);
                break;
            default:
                throw new FilterException("Not supported filter for scope " + filterValue.getScope());
        }
    }

    /**
     * Restrict data by the administrative unit
     * @param def
     * @param value
     */
    protected void restrictAdminUnit(QueryDefs def, ScopeFilterValue value) {
        AdminUnitService srv = getApplicationContext().getBean(AdminUnitService.class);
        AdminUnitData admunit = srv.findOne(value.getValue(), AdminUnitData.class);
        int level = admunit.getLevel();

        def.join(value.getAdminUnitTable()).restrict("($this.id = ? or " +
                "$this.pid" + level + " = ?)",
                value.getValue(), value.getValue());
    }

    @Override
    public String getFilterType() {
        return FilterTypes.SELECT;
    }
}
