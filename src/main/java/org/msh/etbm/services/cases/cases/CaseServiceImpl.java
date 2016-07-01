package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceImpl;
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

    protected void deleteEntity() {
        /* If the patient don't have another case this should delete the patient register

        String ret = super.remove();
        if (!ret.equals("removed"))
            return ret;

        Patient patient = getInstance().getPatient();

        Long count = (Long)getEntityManager()
                .createQuery("select count(*) from TbCase c where c.patient.id = :id")
                .setParameter("id", patient.getId())
                .getSingleResult();

        if (count == 0) {
            getEntityManager().remove(patient);
        }
        return ret;*/
    }

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE;
    }
}