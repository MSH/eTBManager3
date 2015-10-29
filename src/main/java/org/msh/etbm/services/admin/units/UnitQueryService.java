package org.msh.etbm.services.admin.units;

import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceContext;

/**
 * Service to query units in the database based on a given list of query criterias
 *
 * Created by rmemoria on 28/10/15.
 */
@Service
public class UnitQueryService {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @PersistenceContext
    public QueryResult<Unit> queryUnits(UnitQuery qry) {
        QueryBuilder<Unit> builder = queryBuilderFactory.createQueryBuilder(Unit.class);

        builder.addOrderByMap("name", "name", true);

        builder.addLikeRestriction("name", qry.getKey());

        if (qry.getName() != null && !qry.getName().isEmpty()) {
            builder.addRestriction("name = :name");
            builder.setParameter("name", qry.getName());
        }

        return builder.createQueryResult(UnitData.class);
    }
}
