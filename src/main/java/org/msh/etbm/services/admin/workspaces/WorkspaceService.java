package org.msh.etbm.services.admin.workspaces;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to handle CRUD operation in Workspaces
 *
 * Created by rmemoria on 12/11/15.
 */
@Service
public class WorkspaceService extends EntityService<Workspace> {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;


    public QueryResult findMany(WorkspaceQuery qry) {
        QueryBuilder<Workspace> builder = queryBuilderFactory.createQueryBuilder(Workspace.class);

        // set the profiles
        builder.addDefaultProfile(WorkspaceRequest.PROFILE_DEFAULT, WorkspaceData.class);
        builder.addProfile(WorkspaceRequest.PROFILE_ITEM, Item.class);
        builder.addProfile(WorkspaceRequest.PROFILE_DETAILED, WorkspaceDetailData.class);

        // set the order
        builder.addDefaultOrderByMap(WorkspaceRequest.ORDERBY_NAME, "name");

        builder.initialize(qry);

        return builder.createQueryResult();
    }

    @Override
    protected void mapRequest(Object request, Workspace entity) {
        super.mapRequest(request, entity);
        WorkspaceRequest req = (WorkspaceRequest)request;
    }

    @Override
    protected void saveEntity(Workspace entity) {
        super.saveEntity(entity);
    }
}
