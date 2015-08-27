package org.msh.etbm.web.api.init;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by rmemoria on 22/8/15.
 */
@RestController
@RequestMapping("/api/init")
public class InitWorkspaceRest {

    @RequestMapping(value = "/workspace", method = RequestMethod.POST)
    public boolean registerWorkspace(@Valid @RequestBody InitWorkspaceForm frm) {
        return false;
    }
}
