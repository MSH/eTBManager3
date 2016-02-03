package org.msh.etbm.services.admin.workspaces;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.EntityServiceImpl;
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
public interface WorkspaceService extends EntityService<WorkspaceQueryParams> {

}
