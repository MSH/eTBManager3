package org.msh.etbm.services.cases.issues.followup;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.entities.Issue;
import org.msh.etbm.db.entities.IssueFollowup;
import org.msh.etbm.db.entities.UserWorkspace;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Mauricio on 04/08/2016.
 */
@Service
public class IssueFollowUpServiceImpl extends EntityServiceImpl<IssueFollowup, EntityQueryParams> implements IssueFollowUpService{
    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE_ISSUEFOLLOWUP;
    }

    @Override
    protected void beforeValidate(EntityServiceContext<IssueFollowup> context) {
        IssueFollowup entity = context.getEntity();
        UserWorkspace userw = getEntityManager().find(UserWorkspace.class, userRequestService.getUserSession().getUserWorkspaceId());

        entity.setUser(userw.getUser());
        entity.setUnit(userw.getUnit());
        entity.setFollowupDate(new Date());
    }

    @Override
    protected void afterSave(EntityServiceContext<IssueFollowup> context, ServiceResult res) {
        Issue issue = context.getEntity().getIssue();
        issue.setLastAnswerDate(context.getEntity().getFollowupDate());
        getEntityManager().persist(issue);
        getEntityManager().flush();
    }

}
