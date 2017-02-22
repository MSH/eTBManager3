package org.msh.etbm.web.api.dashboard;

import org.msh.etbm.services.dashboard.DashboardRequest;
import org.msh.etbm.services.dashboard.DashboardResponse;
import org.msh.etbm.services.dashboard.DashboardService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * REST API for dashboard handling
 *
 * Created by rmemoria on 22/10/16.
 */
@RestController
@RequestMapping("/api/dashboard")
@Authenticated(permissions = {Permissions.CASES_CLOSE})
public class DashboardREST {

    @Autowired
    DashboardService dashboardService;

    @Autowired
    UserRequestService userRequestService;

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public StandardResult generate(@RequestBody @Valid @NotNull DashboardRequest req) {
        UUID wsId = userRequestService.getUserSession().getWorkspaceId();
        DashboardResponse resp = dashboardService.generate(wsId, req);

        return new StandardResult(resp, null, true);
    }
}
