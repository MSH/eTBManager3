package org.msh.etbm.services.cases.tag;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.tags.TagData;
import org.msh.etbm.services.cases.cases.data.CaseItem;

/**
 * Created by rmemoria on 21/9/16.
 */
public class TagQueryResult extends QueryResult<CaseItem> {

    private TagData tag;

    public TagData getTag() {
        return tag;
    }

    public void setTag(TagData tag) {
        this.tag = tag;
    }
}
