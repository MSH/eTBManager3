package org.msh.etbm.services.admin.userprofiles;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.forms.FormRequestHandler;

import java.util.List;

/**
 * Created by rmemoria on 26/1/16.
 */
public interface UserProfileService extends EntityService<UserProfileQueryParams>, FormRequestHandler<List<Item>> {

}
