package org.msh.etbm.services.cases.followup;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.CaseEvent;
import org.msh.etbm.services.cases.followup.data.FollowUpData;
import org.msh.etbm.services.cases.followup.data.FollowUpType;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

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
                data.setName(type.getKey());
                data.setType(type.name());

                result.getList().add(data);
            }

            //set result results count
            result.setCount(result.getCount() + followups.size());
        }

        return result;
    }
}
