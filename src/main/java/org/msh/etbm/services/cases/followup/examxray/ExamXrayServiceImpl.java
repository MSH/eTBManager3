package org.msh.etbm.services.cases.followup.examxray;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.CaseEntityServiceImpl;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.entities.ExamXRay;
import org.springframework.stereotype.Service;

/**
 * Created by msantos on 14/7/16.
 */
@Service
public class ExamXrayServiceImpl extends CaseEntityServiceImpl<ExamXRay, EntityQueryParams> implements ExamXRayService {

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE_EXAM_XRAY;
    }
}