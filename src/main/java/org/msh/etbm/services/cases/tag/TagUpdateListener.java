package org.msh.etbm.services.cases.tag;

import org.msh.etbm.commons.entities.EntityServiceEvent;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.services.cases.CaseActionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Mauricio on 07/10/2016.
 */
@Component
public class TagUpdateListener {

    @Autowired
    AutoGenTagsCasesService autoGenTagsCasesService;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Listen to events published when a case or a caseEntity is created, updated or deleted
     * @param event
     */
    @EventListener(condition = "#event.result.entityClass.getSimpleName() eq 'TbCase'")
    public void caseEntityListener(EntityServiceEvent event) {
        if (event.getResult() == null || event.getResult().getId() == null) {
            return;
        }

        TbCase tbcase = entityManager.find(TbCase.class, event.getResult().getId());

        // TbCase was deleted
        if (tbcase == null) {
            return;
        }

        autoGenTagsCasesService.updateTags(tbcase.getId());
    }

    /**
     * Listen to events published when a case action is executed
     * @param event
     */
    @EventListener
    public void caseActionListener(CaseActionEvent event) {
        if (event.getResponse() == null || event.getResponse().getCaseId() == null) {
            return;
        }

        autoGenTagsCasesService.updateTags(event.getResponse().getCaseId());
    }
}
