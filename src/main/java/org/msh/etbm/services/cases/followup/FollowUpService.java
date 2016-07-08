package org.msh.etbm.services.cases.followup;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.cases.followup.data.FollowUpData;
import org.msh.etbm.services.cases.followup.data.FollowUpType;
import org.msh.etbm.services.cases.treatment.data.PrescriptionData;
import org.msh.etbm.services.cases.treatment.data.PrescriptionPeriod;
import org.msh.etbm.services.cases.treatment.data.TreatmentData;
import org.msh.etbm.services.cases.treatment.data.TreatmentUnitData;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

/**
 * Created by msantos on 7/7/16.
 */
@Service
public class FollowUpService {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Return information about a treatment of a given case
     *
     * @param caseId the ID of the case
     * @return
     */
    public QueryResult getData(UUID caseId) {
        QueryResult<FollowUpData> result = new QueryResult();

        for (FollowUpType type: FollowUpType.values()) {
            List<Object> followups = entityManager.createQuery("from " + type.getEntityClassName() + " e where e.tbcase.id = :caseId")
                                        .setParameter("caseId", caseId)
                                        .getResultList();

            //mount result list
            for (Object o : followups) {
                CaseEvent caseEvent = (CaseEvent) o;
                FollowUpData data = new FollowUpData();

                data.setData(caseEvent);// TODO: add data class
                data.setMonthOfTreatment(caseEvent.getMonthDisplay());
                data.setNameKey(type.getKey());
                data.setType(type.name());

                result.getList().add(data);
            }

            //set result results count
            result.setCount(result.getCount() + followups.size());
        }

        return result;
    }
}
