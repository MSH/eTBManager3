package org.msh.etbm.services.admin.tags;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 6/1/16.
 */
@Service
public class TagServiceImpl extends EntityServiceImpl<Tag, TagQueryParams> implements TagService {

    @Override
    protected void buildQuery(QueryBuilder<Tag> builder, TagQueryParams queryParams) {
        // order by options
        builder.addDefaultOrderByMap(TagQueryParams.ORDERBY_NAME, "name");
        builder.addOrderByMap(TagQueryParams.ORDERBY_TYPE, "classification, name");

        // profiles
        builder.addDefaultProfile(TagQueryParams.PROFILE_DEFAULT, TagData.class);
        builder.addProfile(TagQueryParams.PROFILE_ITEM, SynchronizableItem.class);

        if (!queryParams.isIncludeDisabled()) {
            builder.addRestriction("active = true");
        }
    }

}
