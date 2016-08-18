package org.msh.etbm.services.cases.contacts;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.CaseEntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.db.entities.CaseContact;
import org.springframework.stereotype.Service;

/**
 * Created by Mauricio on 01/08/2016.
 */
@Service
public class CaseContactServiceImpl extends CaseEntityServiceImpl<CaseContact, CaseContactQueryParams> implements CaseContactService {
    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE_CONTACT;
    }

    @Override
    protected void buildQuery(QueryBuilder<CaseContact> builder, CaseContactQueryParams queryParams) {
        builder.setEntityAlias("c");

        // profiles
        builder.addDefaultProfile(CaseContactQueryParams.PROFILE_DEFAULT, CaseContactData.class);
        builder.addProfile(CaseContactQueryParams.PROFILE_ITEM, SynchronizableItem.class);

        builder.addRestriction("c.tbcase.id = :caseId", queryParams.getTbcaseId());
    }
}
