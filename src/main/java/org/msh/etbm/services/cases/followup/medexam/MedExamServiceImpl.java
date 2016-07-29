package org.msh.etbm.services.cases.followup.medexam;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.CaseEntityServiceImpl;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.entities.MedicalExamination;
import org.springframework.stereotype.Service;

/**
 * Created by msantos on 11/7/16.
 */
@Service
public class MedExamServiceImpl extends CaseEntityServiceImpl<MedicalExamination, EntityQueryParams> implements MedExamService {

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE_MED_EXAM;
    }
}