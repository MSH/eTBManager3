package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.forms.FormRequestHandler;

import java.util.List;

/**
 * Created by rmemoria on 3/2/16.
 */
public interface UserWsService extends EntityService<UserWsQueryParams>, FormRequestHandler<List<Item>> {
}

