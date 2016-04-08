package org.msh.etbm.services.admin.workspaces;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.db.entities.Workspace;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to handle CRUD operation in Workspaces
 *
 * Created by rmemoria on 12/11/15.
 */
@Service
public class WorkspaceServiceImpl extends EntityServiceImpl<Workspace, WorkspaceQueryParams>
    implements WorkspaceService {


    @Override
    public void buildQuery(QueryBuilder<Workspace> builder, WorkspaceQueryParams qry) {
        // set the profiles
        builder.addDefaultProfile(WorkspaceQueryParams.PROFILE_DEFAULT, WorkspaceData.class);
        builder.addProfile(WorkspaceQueryParams.PROFILE_ITEM, SynchronizableItem.class);
        builder.addProfile(WorkspaceQueryParams.PROFILE_DETAILED, WorkspaceDetailData.class);

        // set the order
        builder.addDefaultOrderByMap(WorkspaceQueryParams.ORDERBY_NAME, "name");
    }

    @Override
    protected void mapRequest(Object request, Workspace entity) {
        super.mapRequest(request, entity);
        WorkspaceRequest req = (WorkspaceRequest)request;
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
