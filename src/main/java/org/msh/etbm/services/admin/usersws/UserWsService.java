package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.forms.options.OptionsProvider;
import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 3/2/16.
 */
@Service
public interface UserWsService extends EntityService<UserWsQueryParams>, OptionsProvider {
}

