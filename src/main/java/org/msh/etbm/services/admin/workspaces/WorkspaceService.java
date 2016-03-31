package org.msh.etbm.services.admin.workspaces;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.forms.FormRequestHandler;

import java.util.List;

/**
 * Service to handle CRUD operation in Workspaces
 *
 * Created by rmemoria on 12/11/15.
 */
public interface WorkspaceService extends EntityService<WorkspaceQueryParams>, FormRequestHandler<List<SynchronizableItem>> {

}
