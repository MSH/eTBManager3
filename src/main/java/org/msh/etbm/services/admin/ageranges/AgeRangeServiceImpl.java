package org.msh.etbm.services.admin.ageranges;

import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.AgeRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 6/1/16.
 */
@Service
public class AgeRangeServiceImpl extends EntityServiceImpl<AgeRange, EntityQueryParams>
    implements AgeRangeService {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    /**
     * Return the list of age ranges
     * @return
     */
    @Override
    public QueryResult findMany(EntityQueryParams params) {
        QueryBuilder<AgeRange> builder = queryBuilderFactory.createQueryBuilder(AgeRange.class);

        builder.addDefaultOrderByMap("age", "iniAge");
        builder.setOrderByKey("age");

        return builder.createQueryResult(AgeRangeData.class);
    }
}
