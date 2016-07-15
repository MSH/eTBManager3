package org.msh.etbm.services.cases.followup.examhiv;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.entities.ExamHIV;
import org.springframework.stereotype.Service;

/**
 * Created by msantos on 14/7/16.
 */
@Service
public class ExamHIVServiceImpl extends EntityServiceImpl<ExamHIV, EntityQueryParams> implements ExamHIVService {

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_EXAM_HIV;
    }
}