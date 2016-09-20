package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.sqlquery.QueryDefs;

import java.util.Map;
import java.util.UUID;

/**
 * Define a filter by tag
 *
 * Created by rmemoria on 18/9/16.
 */
public class TagFilter extends AbstractFilter {

    public TagFilter() {
        super("${Tag}");
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        if (value == null) {
            return;
        }

        UUID tagId = value instanceof UUID ? (UUID)value : UUID.fromString(value.toString());

        def.join("tags_case", "$this.case_id = $root.id")
            .restrict("$this.tag_id = ?", tagId);
    }

    @Override
    public String getFilterType() {
        return "select";
    }
}
