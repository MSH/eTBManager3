package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.services.admin.tags.TagQueryParams;
import org.msh.etbm.services.admin.tags.TagService;

import java.util.List;
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

    @Override
    public List<Item> getOptions() {
        TagService tagService = getApplicationContext().getBean(TagService.class);

        TagQueryParams params = new TagQueryParams();
        params.setProfile(TagQueryParams.PROFILE_ITEM);

        QueryResult res = tagService.findMany(params);

        return res.getList();
    }
}
