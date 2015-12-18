package org.msh.etbm.services.admin.substances;

import org.msh.etbm.commons.ErrorMessages;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Substance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

/**
 * CRUD service to handle substance operations
 *
 * Created by rmemoria on 12/11/15.
 */
@Service
public class SubstanceService extends EntityService<Substance> {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    /**
     * Return a list of substances based on the query result
     * @return
     */
    public QueryResult findMany(SubstanceQuery qry) {
        QueryBuilder<Substance> builder = queryBuilderFactory.createQueryBuilder(Substance.class);

        // add the available profiles
        builder.addDefaultProfile(SubstanceRequest.PROFILE_DEFAULT, SubstanceData.class);
        builder.addProfile(SubstanceRequest.PROFILE_ITEM, SynchronizableItem.class);

        // add the order by keys
        builder.addDefaultOrderByMap(SubstanceRequest.ORDERBY_DISPLAYORDER, "displayOrder");
        builder.addOrderByMap(SubstanceRequest.ORDERBY_NAME, "name");

        // initialize builder with standard query values
        builder.initialize(qry);

        if (qry.isDstResultForm()) {
            builder.addRestriction("dstResultForm = true");
        }

        if (qry.isPrevTreatmentForm()) {
            builder.addRestriction("prevTreatmentForm = true");
        }

        if (!qry.isIncludeDisabled()) {
            builder.addRestriction("active = true");
        }

        return builder.createQueryResult();
    }

    @Override
    protected void prepareToSave(Substance entity, BindingResult bindingResult) {
        super.prepareToSave(entity, bindingResult);

        if (bindingResult.hasErrors()) {
            return;
        }

        if (!checkUnique(entity, "name")) {
            bindingResult.rejectValue("name", ErrorMessages.NOT_UNIQUE);
        }

        if (!checkUnique(entity, "shortName")) {
            bindingResult.rejectValue("shortName", ErrorMessages.NOT_UNIQUE);
        }
    }
}
