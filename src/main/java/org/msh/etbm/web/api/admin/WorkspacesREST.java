package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.workspaces.WorkspaceDetailData;
import org.msh.etbm.services.admin.workspaces.WorkspaceQueryParams;
import org.msh.etbm.services.admin.workspaces.WorkspaceRequest;
import org.msh.etbm.services.admin.workspaces.WorkspaceService;
import org.msh.etbm.services.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * CRUD API that exposes CRUD operations to workspaces
 * Created by rmemoria on 12/11/15.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.TABLE_WORKSPACES_EDT})
public class WorkspacesREST {

    @Autowired
    WorkspaceService service;


    @RequestMapping(value = "/workspace/{id}", method = RequestMethod.GET)
    @Authenticated()
    public WorkspaceDetailData get(@PathVariable UUID id) {
        return service.findOne(id, WorkspaceDetailData.class);
    }

    @RequestMapping(value = "/workspace", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody WorkspaceRequest req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/workspace/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody WorkspaceRequest req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/workspace/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        ServiceResult res = service.delete(id);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/workspace/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody WorkspaceQueryParams query) {
        return service.findMany(query);
    }

}
