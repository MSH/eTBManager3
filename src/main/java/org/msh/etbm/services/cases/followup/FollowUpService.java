package org.msh.etbm.services.cases.followup;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.Messages;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.services.cases.followup.data.CaseEventData;
import org.msh.etbm.services.cases.followup.data.FollowUpData;
import org.msh.etbm.services.cases.followup.data.FollowUpType;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Created by msantos on 7/7/16.
 */
@Service
public class FollowUpService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    Messages messages;

    @Autowired
    UserRequestService userRequestService;

    /**
     * Return information about a treatment of a given case
     *
     * @param caseId the ID of the case
     * @return
     */
    public QueryResult getData(UUID caseId) {
        QueryResult<FollowUpData> result = new QueryResult();
        result.setList(new ArrayList<>());

        for (FollowUpType type: FollowUpType.values()) {
            List<Object> followups = entityManager.createQuery("from " + type.getEntityClassName() + " e where e.tbcase.id = :caseId")
                                        .setParameter("caseId", caseId)
                                        .getResultList();

            //mount result list
            for (Object o : followups) {
                CaseEvent caseEvent = (CaseEvent) o;
                CaseEventData caseEventData = getDataInstance(type);
                mapper.map(o, caseEventData);

                FollowUpData data = new FollowUpData();

                data.setData(caseEventData);
                data.setMonthOfTreatment(getMonthTreatDisplay(caseEvent.getMonthTreatment()));
                data.setName(messages.get(type.getKey()));
                data.setType(type.name());

                result.getList().add(data);
            }

            //set result results count
            result.setCount(result.getCount() + followups.size());
        }

        return result;
    }

    private String getMonthTreatDisplay(Integer month) {
        if (month > 0) {
            String s = Integer.toString(month);
            return s + messages.get("global.monthth");
        }

        return messages.get("cases.exams.prevdt");
    }

    private CaseEventData getDataInstance(FollowUpType type){
        CaseEventData ret = null;

        try {
            ret = (CaseEventData) Class.forName(type.getDataClassCanonicalName()).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("ERROR getting data class - " + type.name());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("ERROR getting data class - " + type.name());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ERROR getting data class - " + type.getDataClassCanonicalName() + " - not found");
        }

        return ret;
    }
}
