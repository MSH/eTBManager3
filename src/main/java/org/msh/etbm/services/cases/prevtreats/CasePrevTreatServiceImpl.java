package org.msh.etbm.services.cases.prevtreats;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.CaseEntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.db.entities.PrevTBTreatment;
import org.springframework.stereotype.Service;

/**
 * Created by Mauricio on 18/08/2016.
 */
@Service
public class CasePrevTreatServiceImpl extends CaseEntityServiceImpl<PrevTBTreatment, CasePrevTreatQueryParams> implements CasePrevTreatService {
    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE_PREVTREAT;
    }

    @Override
    protected void buildQuery(QueryBuilder<PrevTBTreatment> builder, CasePrevTreatQueryParams queryParams) {
        builder.setEntityAlias("c");

        // profiles
        builder.addDefaultProfile(CasePrevTreatQueryParams.PROFILE_DEFAULT, CasePrevTreatData.class);
        builder.addProfile(CasePrevTreatQueryParams.PROFILE_ITEM, SynchronizableItem.class);

        builder.addRestriction("c.tbcase.id = :caseId", queryParams.getTbcaseId());
    }
}
