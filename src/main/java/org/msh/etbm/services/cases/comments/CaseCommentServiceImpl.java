package org.msh.etbm.services.cases.comments;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.CaseEntityServiceImpl;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.entities.CaseComment;
import org.msh.etbm.db.entities.User;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by msantos on 11/7/16.
 */
@Service
public class CaseCommentServiceImpl extends CaseEntityServiceImpl<CaseComment, EntityQueryParams> implements CaseCommentService {

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE_COMMENT;
    }

    @Override
    protected void beforeValidate(EntityServiceContext<CaseComment> context) {
        CaseComment entity = context.getEntity();
        User user = getEntityManager().find(User.class, userRequestService.getUserSession().getUserId());

        if (entity.getUser() == null) {
            entity.setUser(user);
        }

        if (entity.getDate() == null) {
            entity.setDate(new Date());
        }
    }
}