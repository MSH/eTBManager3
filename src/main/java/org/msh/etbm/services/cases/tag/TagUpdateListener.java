package org.msh.etbm.services.cases.tag;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceEvent;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.services.cases.CaseActionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

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
     * Listen to events published when an entity is created, updated or deleted
     * @param event
     */
    @EventListener(condition = "T(org.msh.etbm.services.cases.tag.TagUpdateListener).shouldUpdateTags(#event)")
    public void caseEntityListener(EntityServiceEvent event) {
        UUID caseId;
        if (event.getResult().getCommandType().getPath().equals(CommandTypes.CASES_CASE)) {
            caseId = event.getResult().getId();
        } else {
            caseId = event.getResult().getParentId();
        }

        TbCase tbcase = entityManager.find(TbCase.class, caseId);

        // TbCase not found with provided id
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

    /**
     * Checks if a case entity or a case is being created, updated or deleted
     * @param event
     * @return
     */
    public static boolean shouldUpdateTags(EntityServiceEvent event) {
        if (event == null || event.getResult() == null || event.getResult().getCommandType() == null ||
                event.getResult().getCommandType().getPath() == null ||
                !event.getResult().getCommandType().getPath().contains("cases.case")) {
            return false;
        }

        return true;
    }
}
