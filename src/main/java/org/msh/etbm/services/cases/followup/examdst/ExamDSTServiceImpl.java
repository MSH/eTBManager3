package org.msh.etbm.services.cases.followup.examdst;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.entities.ExamDST;
import org.msh.etbm.db.enums.ExamStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/**
 * Created by msantos on 15/7/16.
 */
@Service
public class ExamDSTServiceImpl extends EntityServiceImpl<ExamDST, EntityQueryParams> implements ExamDSTService {

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_EXAM_DST;
    }

    @Override
    protected void beforeSave(EntityServiceContext<ExamDST> context, Errors errors) {
        ExamDST entity = context.getEntity();

        if (entity.getDateRelease() != null && entity.getDateRelease().before(entity.getDate())) {
            errors.rejectValue("dateRelease", "cases.exams.datereleasebeforecol");
        }

        entity.setStatus(ExamStatus.PERFORMED);
    }


}