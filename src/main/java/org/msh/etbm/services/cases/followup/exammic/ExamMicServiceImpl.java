package org.msh.etbm.services.cases.followup.exammic;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.db.entities.ExamMicroscopy;
import org.springframework.stereotype.Service;

/**
 * Created by msantos on 11/7/16.
 */
@Service
public class ExamMicServiceImpl extends EntityServiceImpl<ExamMicroscopy, ExamMicQueryParams> implements ExamMicService {

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_EXAM_MIC;
    }
}