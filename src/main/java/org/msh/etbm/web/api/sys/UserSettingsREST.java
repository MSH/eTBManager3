package org.msh.etbm.web.api.sys;

import org.msh.etbm.services.usersettings.UserSettingsFormData;
import org.msh.etbm.services.usersettings.UserSettingsService;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by rmemoria on 15/5/16.
 */
@Controller
@RequestMapping(path = "/api/sys/usersettings")
@Authenticated
public class UserSettingsREST {

    @Autowired
    UserSettingsService userSettingsService;

    @RequestMapping(method = RequestMethod.GET)
    public UserSettingsFormData getUserSettings() {
        return userSettingsService.get();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void updateUserSettings(UserSettingsFormData data) {
        userSettingsService.update(data);
    }
}
