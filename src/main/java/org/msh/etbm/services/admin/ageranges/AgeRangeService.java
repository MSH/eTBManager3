package org.msh.etbm.services.admin.ageranges;

import org.msh.etbm.commons.entities.EntityService;
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
public class AgeRangeService extends EntityService<AgeRange> {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    /**
     * Return the list of age ranges
     * @return
     */
    public QueryResult findMany() {
        QueryBuilder<AgeRange> builder = queryBuilderFactory.createQueryBuilder(AgeRange.class);

        builder.addDefaultOrderByMap("age", "iniAge");
        builder.setOrderByKey("age");

        return builder.createQueryResult(AgeRangeData.class);
    }
}
