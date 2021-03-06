package org.msh.etbm.services.admin.substances;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.db.entities.Substance;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

/**
 * CRUD service to handle substance operations
 * <p>
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
        builder.addDefaultOrderByMap(SubstanceQueryParams.ORDERBY_NAME, "name");

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
    public String getCommandType() {
        return CommandTypes.ADMIN_SUBSTANCES;
    }

    @Override
    protected void beforeSave(EntityServiceContext<Substance> context, Errors errors) {
        Substance sub = context.getEntity();

        // check if name is unique
        if (!checkUnique(sub, "name")) {
            errors.rejectValue("name", Messages.NOT_UNIQUE);
        }

        // check if short name is unique
        if (!checkUnique(sub, "shortName")) {
            errors.rejectValue("shortName", Messages.NOT_UNIQUE);
        }
    }


    @Override
    public String getFormCommandName() {
        return CMD_NAME;
    }

    /**
     * Return the list of substances to a form from a from request
     *
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
        for (SubstanceData sub : res.getList()) {
            lst.add(new SynchronizableItem(sub.getId(), "(" + sub.getShortName() + ") " + sub.getName()));
        }

        return lst;
    }
}

