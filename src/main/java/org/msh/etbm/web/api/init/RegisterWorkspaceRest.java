package org.msh.etbm.web.api.init;

import org.msh.etbm.services.init.RegisterWorkspaceRequest;
import org.msh.etbm.services.init.RegisterWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Created by rmemoria on 22/8/15.
 */
@RestController
@RequestMapping("/api/init")
public class RegisterWorkspaceRest {

    @Autowired
    RegisterWorkspaceService registerWorkspaceService;

    @RequestMapping(value = "/workspace", method = RequestMethod.POST)
    public UUID registerWorkspace(@Valid @RequestBody RegisterWorkspaceRequest frm) {
        return registerWorkspaceService.register(frm);
    }
}
