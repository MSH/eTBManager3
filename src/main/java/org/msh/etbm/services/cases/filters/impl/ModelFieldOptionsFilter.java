package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.services.cases.filters.FilterContext;
import org.msh.etbm.services.cases.filters.FilterGroup;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 30/8/16.
 */
public class ModelFieldOptionsFilter extends AbstractFilter {

    private String fieldName;
    private String modelName;

    public ModelFieldOptionsFilter(FilterGroup grp, String id, String label, String modelName, String fieldName) {
        super(grp, id, label);
        this.modelName = modelName;
        this.fieldName = fieldName;
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        ModelDAOFactory factory = getApplicationContext().getBean(ModelDAOFactory.class);

        ModelDAO dao = factory.create(modelName);
        final List<Item> options = dao.getFieldOptions(fieldName);

        addValuesRestriction(def, fieldName, value, item -> {
            // check if item is a valid option
            Item opt = options.stream()
                    .filter(it -> it.getId().equals(item))
                    .findFirst()
                    .get();
            return opt != null ? opt.getId() : null;
        });
    }

    @Override
    public String getFilterType() {
        return "multi-select";
    }

    @Override
    public Map<String, Object> getResources(FilterContext context, Map<String, Object> params) {
        ModelDAO dao = context.getModelDAOFactory().create(modelName);
        List<Item> options = dao.getFieldOptions(fieldName);

        return options != null ? Collections.singletonMap("options", options) : null;
    }
}
