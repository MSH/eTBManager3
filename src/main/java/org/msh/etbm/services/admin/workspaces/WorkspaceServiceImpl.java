package org.msh.etbm.services.admin.workspaces;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.cmdlog.EntityCmdLogHandler;
import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.db.entities.UserWorkspace;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.services.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * Service to handle CRUD operation in Workspaces
 *
 * Created by rmemoria on 12/11/15.
 */
@Service
public class WorkspaceServiceImpl extends EntityServiceImpl<Workspace, WorkspaceQueryParams>
    implements WorkspaceService {

    @Autowired
    WorkspaceCreator workspaceCreator;

    @Autowired
    UserRequestService userRequestService;


    @Override
    public void buildQuery(QueryBuilder<Workspace> builder, WorkspaceQueryParams qry) {
        // set the profiles
        builder.addDefaultProfile(WorkspaceQueryParams.PROFILE_DEFAULT, WorkspaceData.class);
        builder.addProfile(WorkspaceQueryParams.PROFILE_ITEM, SynchronizableItem.class);
        builder.addProfile(WorkspaceQueryParams.PROFILE_DETAILED, WorkspaceDetailData.class);

        // set the order
        builder.addDefaultOrderByMap(WorkspaceQueryParams.ORDERBY_NAME, "name");
    }

    @Transactional
    @CommandLog(type = EntityCmdLogHandler.CREATE, handler = EntityCmdLogHandler.class)
    @Override
    public ServiceResult create(@Valid @NotNull Object req) {
        // keep other request classes being handled by the standard create method
        if (!(req instanceof WorkspaceFormData)) {
            return super.create(req);
        }

        WorkspaceFormData frmdata = (WorkspaceFormData)req;

        // check if name is present in the request
        if (!frmdata.getName().isPresent()) {
            // if name is not available, raise an exception about missing name
            raiseRequiredFieldException(frmdata, "name");
        }

        // create a new workspace using the template file
        WorkspaceData data = workspaceCreator.create(frmdata.getName().get());

        // get the 'real' workspace
        Workspace entity = getEntityManager().find(Workspace.class, data.getId());

        addCurrentUser(entity);

        // create the result of the service
        ServiceResult res = createResult(entity);
        res.setId( entity.getId() );

        res.setLogValues(createValuesToLog(entity, Operation.NEW));

        return res;
    }

    /**
     * Add the current user to the new workspace, so he will be able to enter there
     * @param workspace the new workspace
     */
    private void addCurrentUser(Workspace workspace) {
        EntityManager em = getEntityManager();

        UUID userId = userRequestService.getUserSession().getUserId();

        workspaceCreator.addUserToWorkspace(userId, workspace.getId());
    }

    @Override
    public String getFormCommandName() {
        return "workspaces";
    }

    @Override
    public List<SynchronizableItem> execFormRequest(FormRequest req) {
        WorkspaceQueryParams p = new WorkspaceQueryParams();
        p.setProfile(WorkspaceQueryParams.PROFILE_ITEM);
        p.setOrderBy(WorkspaceQueryParams.ORDERBY_NAME);

        QueryResult res = findMany(p);

        List<SynchronizableItem> lst = res.getList();
        return lst;
    }
}
