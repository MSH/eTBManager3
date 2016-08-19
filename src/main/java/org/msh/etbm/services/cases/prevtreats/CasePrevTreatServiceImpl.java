package org.msh.etbm.services.cases.prevtreats;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.entities.CaseEntityServiceImpl;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.db.entities.PrevTBTreatment;
import org.msh.etbm.db.entities.Tag;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Date;

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

    @Override
    protected void beforeSave(EntityServiceContext<PrevTBTreatment> context, Errors errors) {
        PrevTBTreatment treat = context.getEntity();

        if (!isEmpty(treat.getOutcomeMonth()) && isEmpty(treat.getOutcomeYear())){
            errors.rejectValue("outcomeMonth", "cases.prevtreat.msg1");
            errors.rejectValue("outcomeYear", "cases.prevtreat.msg1");
        }

        // if both periods are filled in
        if (!isEmpty(treat.getMonth()) && !isEmpty(treat.getYear()) && !isEmpty(treat.getOutcomeMonth()) && !isEmpty(treat.getOutcomeYear())) {
            Date iniDate = DateUtils.newDate(treat.getYear(), treat.getMonth() - 1, 1);
            Date endDate = DateUtils.newDate(treat.getOutcomeYear(), treat.getOutcomeMonth() - 1, 1);

            if (iniDate.compareTo(endDate) > 0) {
                errors.rejectValue("outcomeMonth", "cases.prevtreat.msg0");
                errors.rejectValue("outcomeYear", "cases.prevtreat.msg0");
                errors.rejectValue("month", "cases.prevtreat.msg0");
                errors.rejectValue("year", "cases.prevtreat.msg0");
            }
        }
    }

    private boolean isEmpty(Integer i) {
        return i == null || i.intValue() == 0;
    }
}
