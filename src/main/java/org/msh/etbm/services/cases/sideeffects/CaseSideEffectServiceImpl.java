package org.msh.etbm.services.cases.sideeffects;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.CaseEntityServiceImpl;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.db.entities.CaseSideEffect;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/**
 * Created by Mauricio on 12/08/2016.
 */
@Service
public class CaseSideEffectServiceImpl extends CaseEntityServiceImpl<CaseSideEffect, CaseSideEffectQueryParams> implements CaseSideEffectService {
    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE_SIDEEFFECT;
    }

    @Override
    protected void buildQuery(QueryBuilder<CaseSideEffect> builder, CaseSideEffectQueryParams queryParams) {
        // profiles
        builder.addDefaultProfile(CaseSideEffectQueryParams.PROFILE_DEFAULT, CaseSideEffectData.class);
        builder.addProfile(CaseSideEffectQueryParams.PROFILE_ITEM, SynchronizableItem.class);
    }

    @Override
    protected void beforeSave(EntityServiceContext<CaseSideEffect> context, Errors errors) {
        CaseSideEffect entity = context.getEntity();

        String medicines = "";
        if (entity.getSubstance() != null) {
            medicines += entity.getSubstance().getShortName();
        }

        if (entity.getSubstance2() != null) {
            if (!medicines.equals("")) {
                medicines += ", ";
            }

            medicines += entity.getSubstance2().getShortName();
        }

        entity.setMedicines(medicines.equals("") ? null : medicines);
    }
}
