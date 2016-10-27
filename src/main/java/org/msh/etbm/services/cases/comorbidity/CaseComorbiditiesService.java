package org.msh.etbm.services.cases.comorbidity;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.db.entities.CaseComorbidities;
import org.msh.etbm.db.entities.TbCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Created by Mauricio on 27/10/2016.
 */
@Service
public class CaseComorbiditiesService {

    // TODO: [MSANTOS] register commandlog

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DozerBeanMapper mapper;

    @Transactional
    public void update(UUID caseId, CaseComorbiditiesData data) {
        TbCase tbcase = entityManager.find(TbCase.class, caseId);

        if (tbcase == null) {
            throw new EntityNotFoundException();
        }

        CaseComorbidities comorbidities = tbcase.getComorbidities();
        if (comorbidities == null) {
            comorbidities = new CaseComorbidities();
            comorbidities.setTbCase(tbcase);
        }

        mapper.map(data, comorbidities);

        entityManager.persist(comorbidities);
    }

}
