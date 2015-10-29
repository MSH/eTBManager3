package org.msh.etbm.web.api.units;

import org.msh.etbm.services.admin.units.TbunitService;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.msh.etbm.web.api.authentication.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rmemoria on 28/10/15.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.ADMIN_UNITS_EDT})
public class TbunitsREST {

    @Autowired
    TbunitService tbunitService;


}
