package org.msh.etbm.services.cases.followup.examxpert;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.entities.ExamXpert;
import org.msh.etbm.db.enums.ExamStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/**
 * Created by msantos on 14/7/16.
 */
@Service
public class ExamXpertServiceImpl extends EntityServiceImpl<ExamXpert, EntityQueryParams> implements ExamXpertService {

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE_EXAM_XPERT;
    }

    @Override
    protected void beforeSave(EntityServiceContext<ExamXpert> context, Errors errors) {
        ExamXpert entity = context.getEntity();

        if (entity.getDateRelease() != null && entity.getDateRelease().before(entity.getDate())) {
            errors.rejectValue("dateRelease", "cases.exams.datereleasebeforecol");
        }

        entity.setStatus(ExamStatus.PERFORMED);
    }

}