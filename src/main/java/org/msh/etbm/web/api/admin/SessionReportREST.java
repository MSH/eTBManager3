package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.sessionreport.SessionReportData;
import org.msh.etbm.services.admin.sessionreport.SessionReportQueryParams;
import org.msh.etbm.services.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rmemoria on 10/3/16.
 */
@RestController
@RequestMapping("/api/admin/rep")
@Authenticated(permissions = {Permissions.ADMIN_REP_USERSESSIONS})
public class SessionReportREST {

    //@Autowired
    //OnlineReportService service;

    @RequestMapping(value = "/sessionreport/day", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult queryFilterByDay(@Valid @RequestBody SessionReportQueryParams query) {
        String app = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36";
        QueryResult ret = new QueryResult();
        ret.setList(new ArrayList<SessionReportQueryParams>());

        ret.getList().add(new SessionReportData("MSANTOS", "Mauricio Santos", new Date(), new Date(), "127.0.0.1", app));
        ret.getList().add(new SessionReportData("MSANTOS", "Mauricio Santos", new Date(), new Date(), "127.0.0.1", app));
        ret.getList().add(new SessionReportData("MSANTOS", "Mauricio Santos", new Date(), new Date(), "127.0.0.1", app));

        return null;
    }

    @RequestMapping(value = "/sessionreport/other", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult queryOtherFilters(@Valid @RequestBody SessionReportQueryParams query) {
        return null;
    }
}
