package org.msh.etbm.services.admin.substances;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Substance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CRUD service to handle substance operations
 *
 * Created by rmemoria on 12/11/15.
 */
@Service
public class SubstanceService extends EntityService<Substance> {

    public static final String PROFILE_ITEM = "item";
    public static final String PROFILE_DEFAULT = "default";

    public static final String ORDERBY_NAME = "name";
    public static final String ORDERBY_DISPLAYORDER = "displayOrder";

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    /**
     * Return a list of substances based on the query result
     * @return
     */
    public QueryResult findMany(SubstanceQuery qry) {
        QueryBuilder<Substance> builder = queryBuilderFactory.createQueryBuilder(Substance.class);

        // add the available profiles
        builder.addDefaultProfile(PROFILE_DEFAULT, SubstanceData.class);
        builder.addProfile(PROFILE_ITEM, Item.class);

        // add the order by keys
        builder.addDefaultOrderByMap(ORDERBY_DISPLAYORDER, "displayOrder");
        builder.addOrderByMap(ORDERBY_NAME, "name");

        // initialize builder with standard query values
        builder.initialize(qry);

        if (qry.isDstResultForm()) {
            builder.addRestriction("dstResultForm = true");
        }

        if (qry.isPrevTreatmentForm()) {
            builder.addRestriction("prevTreatmentForm = true");
        }

        return builder.createQueryResult();
    }
}
