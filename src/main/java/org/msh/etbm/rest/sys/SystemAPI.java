package org.msh.etbm.rest.sys;

import org.msh.etbm.services.sys.SystemInformation;
import org.msh.etbm.services.sys.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API to handle system requests
 *
 * Created by rmemoria on 16/8/15.
 */
@RestController
@RequestMapping("/api/sys")
public class SystemAPI {

    @Autowired
    SystemService systemService;

    /**
     * Return information about the system
     * @return instance of SystemInformation
     */
    @RequestMapping("/info")
    public SystemInformation getInformation() {
        return systemService.getInformation();
    }
}
