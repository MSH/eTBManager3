package org.msh.etbm.services.cases.followup.exammic;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.entities.ExamMicroscopy;
import org.msh.etbm.db.enums.ExamStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/**
 * Created by msantos on 11/7/16.
 */
@Service
public class ExamMicServiceImpl extends EntityServiceImpl<ExamMicroscopy, EntityQueryParams> implements ExamMicService {

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_EXAM_MIC;
    }

    @Override
    protected void beforeSave(ExamMicroscopy entity, Errors errors) {
        if (entity.getDateRelease() != null && entity.getDateRelease().before(entity.getDate())) {
            // TODOMS: provisoriamente no campo comments, precisa colcoar esse erro no dateRelease
            // o componente datepicker parece estar tendo algum problema para receber a mensagem de erro.
            errors.rejectValue("dateRelease", "cases.exams.datereleasebeforecol");
        }

        entity.setStatus(ExamStatus.PERFORMED);
    }
}