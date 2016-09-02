package org.msh.etbm.services.admin.regimens;


import org.hibernate.engine.spi.QueryParameters;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestHandler;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.MedicineRegimen;
import org.msh.etbm.db.entities.Regimen;
import org.msh.etbm.db.enums.CaseClassification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CRUD services for medicine regimens
 * <p>
 * Created by rmemoria on 6/1/16.
 */
@Service
public class RegimenServiceImpl extends EntityServiceImpl<Regimen, RegimenQueryParams>
        implements RegimenService, FormRequestHandler<List<Item>> {

    @Override
    protected void buildQuery(QueryBuilder<Regimen> builder, RegimenQueryParams queryParams) {
        // order by options
        builder.addDefaultOrderByMap(RegimenQueryParams.ORDERBY_NAME, "name");
        builder.addOrderByMap(RegimenQueryParams.ORDERBY_CLASSIFICATION, "classification, name");
        builder.addOrderByMap(RegimenQueryParams.ORDERBY_CLASSIFICATION + "_DESC", "classification desc, name");

        // profiles
        builder.addDefaultProfile(RegimenQueryParams.PROFILE_DEFAULT, RegimenData.class);
        builder.addProfile(RegimenQueryParams.PROFILE_ITEM, SynchronizableItem.class);
        builder.addProfile(RegimenQueryParams.PROFILE_DETAILED, RegimenDetailedData.class);

        if (!queryParams.isIncludeDisabled()) {
            builder.addRestriction("active = true");
        }

        builder.addRestriction("classification = :cla", queryParams.getCaseClassification());
    }

    @Override
    public String getCommandType() {
        return CommandTypes.ADMIN_REGIMENS;
    }

    @Override
    protected void mapRequest(EntityServiceContext<Regimen> context) {
        Regimen regimen = context.getEntity();

        // clear previous regimens
        regimen.getMedicines().clear();

        // run default mapping
        super.mapRequest(context);

        // manually set the parent regimen, since it cannot be done by dozer
        for (MedicineRegimen mr : regimen.getMedicines()) {
            mr.setRegimen(regimen);
        }
    }

    @Override
    public String getFormCommandName() {
        return "regimens";
    }

    @Override
    public List<Item> execFormRequest(FormRequest req) {
        RegimenQueryParams params = new RegimenQueryParams();
        params.setProfile(RegimenQueryParams.PROFILE_ITEM);

        String cla = (String)req.getParams().get("classification");
        if (cla != null) {
            params.setCaseClassification(ObjectUtils.stringToEnum(cla, CaseClassification.class));
        }

        QueryResult<Item> res = findMany(params);

        return res.getList();
    }
}
