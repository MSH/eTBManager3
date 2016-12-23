package org.msh.etbm.test.commons.indicators.fixtures;

import org.msh.etbm.commons.filters.FilterItem;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Created by rmemoria on 10/9/16.
 */
public class SimpleFieldFilter implements FilterItem {

    private String id;
    private String name;
    private String fieldName;

    public SimpleFieldFilter(String id, String name, String fieldName) {
        this.id = id;
        this.name = name;
        this.fieldName = fieldName;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void initialize(ApplicationContext context) {
    //
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        if (value == null) {
            return;
        }
        def.restrict(fieldName + " = ?", value.toString());
    }

    @Override
    public String getFilterType() {
        return "select";
    }

    @Override
    public Map<String, Object> getResources(Map<String, Object> params) {
        return null;
    }

    @Override
    public String valueToDisplay(Object value) {
        return null;
    }
}
