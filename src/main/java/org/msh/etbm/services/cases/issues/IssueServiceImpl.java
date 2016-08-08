package org.msh.etbm.services.cases.issues;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.CaseEntityServiceImpl;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.entities.Issue;
import org.msh.etbm.db.entities.UserWorkspace;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Mauricio on 03/08/2016.
 */
@Service
public class IssueServiceImpl extends CaseEntityServiceImpl<Issue, EntityQueryParams> implements IssueService {
    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE_ISSUE;
    }

    @Override
    protected void beforeValidate(EntityServiceContext<Issue> context) {
        Issue entity = context.getEntity();
        UserWorkspace userw = getEntityManager().find(UserWorkspace.class, userRequestService.getUserSession().getUserWorkspaceId());

        if (entity.getId() == null || entity.getCreationDate() == null) {
            entity.setCreationDate(new Date());
        }

        entity.setUser(userw.getUser());
        entity.setUnit(userw.getUnit());
    }
}
