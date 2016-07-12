package org.msh.etbm.services.cases.followup.medexam;

import org.hibernate.exception.SQLGrammarException;
import org.msh.etbm.Messages;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandType;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.db.entities.MedicalExamination;
import org.msh.etbm.db.entities.Tag;
import org.msh.etbm.services.admin.tags.CasesTagsUpdateService;
import org.msh.etbm.services.admin.tags.TagData;
import org.msh.etbm.services.admin.tags.TagQueryParams;
import org.msh.etbm.services.admin.tags.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.persistence.PersistenceException;

/**
 * Created by msantos on 11/7/16.
 */
@Service
public class MedExamServiceImpl extends EntityServiceImpl<MedicalExamination, MedExamQueryParams> implements MedExamService {

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_MED_EXAM;
    }
}