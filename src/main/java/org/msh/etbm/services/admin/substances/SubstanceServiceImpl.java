package org.msh.etbm.services.admin.substances;

import org.msh.etbm.commons.ErrorMessages;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityServiceImpl;
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
public class SubstanceServiceImpl extends EntityServiceImpl<Substance, SubstanceQueryParams>
        implements SubstanceService {

    @Override
    protected void buildQuery(QueryBuilder<Substance> builder, SubstanceQueryParams queryParams) {
        // add the available profiles
        builder.addDefaultProfile(SubstanceQueryParams.PROFILE_DEFAULT, SubstanceData.class);
        builder.addProfile(SubstanceQueryParams.PROFILE_ITEM, SynchronizableItem.class);

        // add the order by keys
        builder.addDefaultOrderByMap(SubstanceQueryParams.ORDERBY_DISPLAYORDER, "displayOrder");
        builder.addOrderByMap(SubstanceQueryParams.ORDERBY_NAME, "name");

        if (queryParams.isDstResultForm()) {
            builder.addRestriction("dstResultForm = true");
        }

        if (queryParams.isPrevTreatmentForm()) {
            builder.addRestriction("prevTreatmentForm = true");
        }

        if (!queryParams.isIncludeDisabled()) {
            builder.addRestriction("active = true");
        }
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

