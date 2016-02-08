package org.msh.etbm.services.admin.substances;

import org.msh.etbm.commons.ErrorMessages;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.db.entities.Substance;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

/**
 * CRUD service to handle substance operations
 *
 * Created by rmemoria on 12/11/15.
 */
@Service
public class SubstanceServiceImpl extends EntityServiceImpl<Substance, SubstanceQueryParams>
        implements SubstanceService {

    public static final String CMD_NAME = "substances";

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

        // there is any validation error ?
        if (bindingResult.hasErrors()) {
            return;
        }

        // check if name is unique
        if (!checkUnique(entity, "name")) {
            bindingResult.rejectValue("name", ErrorMessages.NOT_UNIQUE);
        }

        // check if short name is unique
        if (!checkUnique(entity, "shortName")) {
            bindingResult.rejectValue("shortName", ErrorMessages.NOT_UNIQUE);
        }
    }

    @Override
    public String getFormCommandName() {
        return CMD_NAME;
    }

    /**
     * Return the list of substances to a form from a from request
     * @param req the form request
     * @return list of substances
     */
    @Override
    public List<Item> execFormRequest(FormRequest req) {
        SubstanceQueryParams qry = new SubstanceQueryParams();
        qry.setProfile(SubstanceQueryParams.PROFILE_DEFAULT);
        qry.setOrderBy(SubstanceQueryParams.ORDERBY_NAME);
        QueryResult<SubstanceData> res = findMany(qry);

        // mount list to include the short name in the label
        List<Item> lst = new ArrayList<>();
        for (SubstanceData sub: res.getList()) {
            lst.add(new SynchronizableItem(sub.getId(), "(" + sub.getShortName() + ") " + sub.getName()));
        }

        return lst;
    }
}

