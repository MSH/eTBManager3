package org.msh.etbm.services.cases.followup.examcul;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.CaseEntityServiceImpl;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.entities.ExamCulture;
import org.msh.etbm.db.enums.ExamStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/**
 * Created by msantos on 11/7/16.
 */
@Service
public class ExamCulServiceImpl extends CaseEntityServiceImpl<ExamCulture, EntityQueryParams> implements ExamCulService {

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE_EXAM_CUL;
    }

    @Override
    protected void beforeSave(EntityServiceContext<ExamCulture> context, Errors errors) {
        ExamCulture entity = context.getEntity();

        if (entity.getDateRelease() != null && entity.getDateRelease().before(entity.getDate())) {
            errors.rejectValue("dateRelease", "cases.exams.datereleasebeforecol");
        }

        entity.setStatus(ExamStatus.PERFORMED);
    }
}