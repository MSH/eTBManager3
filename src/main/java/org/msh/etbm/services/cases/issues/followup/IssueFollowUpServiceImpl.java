package org.msh.etbm.services.cases.issues.followup;

import org.msh.etbm.commons.PersonNameUtils;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.entities.Issue;
import org.msh.etbm.db.entities.IssueFollowup;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.entities.UserWorkspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Mauricio on 04/08/2016.
 */
@Service
public class IssueFollowUpServiceImpl extends EntityServiceImpl<IssueFollowup, EntityQueryParams> implements IssueFollowUpService {

    @Autowired
    PersonNameUtils personNameUtils;

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE_ISSUEFOLLOWUP;
    }

    @Override
    protected void beforeValidate(EntityServiceContext<IssueFollowup> context) {
        IssueFollowup entity = context.getEntity();
        UserWorkspace userw = getEntityManager().find(UserWorkspace.class, userRequestService.getUserSession().getUserWorkspaceId());

        if (entity.getId() == null || entity.getFollowupDate() == null) {
            entity.setFollowupDate(new Date());
        }

        entity.setUser(userw.getUser());
        entity.setUnit(userw.getUnit());
    }

    @Override
    protected void afterSave(EntityServiceContext<IssueFollowup> context, ServiceResult res) {
        Issue issue = context.getEntity().getIssue();
        getEntityManager().persist(issue);
        getEntityManager().flush();
    }

    /**
     * Create the result to be returned by the create, update or delete operation of CaseEntity
     *
     * @param entity the entity involved in the operation
     * @return instance of {@link ServiceResult}
     */
    @Override
    protected ServiceResult createResult(IssueFollowup entity) {
        ServiceResult res = super.createResult(entity);

        TbCase tbcase = entity.getIssue().getTbcase();

        res.setParentId(entity.getIssue().getTbcase().getId());
        res.setEntityName("(" + tbcase.getClassification() + ") " +
                personNameUtils.displayPersonName(tbcase.getPatient().getName()));

        return res;
    }

}
