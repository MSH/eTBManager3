package org.msh.etbm.services.admin.regimens;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Regimen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CRUD services for medicine regimens
 *
 * Created by rmemoria on 6/1/16.
 */
@Service
public class RegimenService extends EntityService<Regimen> {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    public QueryResult findMany(RegimenQueryParams qry) {
        QueryBuilder<Regimen> builder = queryBuilderFactory.createQueryBuilder(Regimen.class);

        // order by options
        builder.addDefaultOrderByMap(RegimenQueryParams.ORDERBY_NAME, "name");
        builder.addOrderByMap(RegimenQueryParams.ORDERBY_CLASSIFICATION, "classification, name");
        builder.addOrderByMap(RegimenQueryParams.ORDERBY_CLASSIFICATION + "_DESC", "classification desc, name");

        // profiles
        builder.addDefaultProfile(RegimenQueryParams.PROFILE_DEFAULT, RegimenData.class);
        builder.addProfile(RegimenQueryParams.PROFILE_ITEM, SynchronizableItem.class);
        builder.addProfile(RegimenQueryParams.PROFILE_DETAILED, RegimenDetailedData.class);

        builder.initialize(qry);

        if (!qry.isIncludeDisabled()) {
            builder.addRestriction("active = true");
        }

        return builder.createQueryResult();
    }
}
