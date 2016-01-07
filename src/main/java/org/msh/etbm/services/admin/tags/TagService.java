package org.msh.etbm.services.admin.tags;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Regimen;
import org.msh.etbm.db.entities.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 6/1/16.
 */
@Service
public class TagService extends EntityService<Tag> {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    public QueryResult findMany(TagQuery qry) {
        QueryBuilder<Tag> builder = queryBuilderFactory.createQueryBuilder(Tag.class);

        // order by options
        builder.addDefaultOrderByMap(TagQuery.ORDERBY_NAME, "name");
        builder.addOrderByMap(TagQuery.ORDERBY_TYPE, "classification, name");

        // profiles
        builder.addDefaultProfile(TagQuery.PROFILE_DEFAULT, TagData.class);
        builder.addProfile(TagQuery.PROFILE_ITEM, SynchronizableItem.class);

        builder.initialize(qry);

        if (!qry.isIncludeDisabled()) {
            builder.addRestriction("active = true");
        }

        return builder.createQueryResult();
    }

}
