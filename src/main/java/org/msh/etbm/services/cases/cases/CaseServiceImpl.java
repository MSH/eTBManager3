package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.db.entities.Patient;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.services.admin.tags.CasesTagsUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by msantos on 26/3/16.
 */
@Service
public class CaseServiceImpl extends EntityServiceImpl<TbCase, CaseQueryParams> implements CaseService {

    @Autowired
    CasesTagsUpdateService casesTagsUpdateService;

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE;
    }

    @Override
    protected void afterDelete(TbCase entity, ServiceResult res) {
        // removes patient from database if this patient don't have any other case registered
        Patient patient = entity.getPatient();

        Long count = (Long)getEntityManager()
                .createQuery("select count(*) from TbCase c where c.patient.id = :id")
                .setParameter("id", patient.getId())
                .getSingleResult();

        if (count == 0) {
            getEntityManager().remove(patient);
        }
    }
}