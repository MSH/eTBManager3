package org.msh.etbm.services.cases.followup.exammic;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.entities.ExamMicroscopy;
import org.msh.etbm.db.enums.ExamStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/**
 * Created by msantos on 11/7/16.
 */
@Service
public class ExamMicServiceImpl extends EntityServiceImpl<ExamMicroscopy, EntityQueryParams> implements ExamMicService {

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE_EXAM_MIC;
    }

    @Override
    protected void beforeSave(EntityServiceContext<ExamMicroscopy> context, Errors errors) {
        ExamMicroscopy entity = context.getEntity();

        if (entity.getDateRelease() != null && entity.getDateRelease().before(entity.getDate())) {
            errors.rejectValue("dateRelease", "cases.exams.datereleasebeforecol");
        }

        entity.setStatus(ExamStatus.PERFORMED);
    }
}