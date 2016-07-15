package org.msh.etbm.services.cases.followup.examcul;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.entities.ExamCulture;
import org.springframework.stereotype.Service;

/**
 * Created by msantos on 11/7/16.
 */
@Service
public class ExamCulServiceImpl extends EntityServiceImpl<ExamCulture, EntityQueryParams> implements ExamCulService {

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_EXAM_CUL;
    }

}