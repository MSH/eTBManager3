package org.msh.etbm.services.admin.substances;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.forms.FormRequestHandler;

import java.util.List;

/**
 * CRUD service to handle substance operations
 *
 * Created by rmemoria on 12/11/15.
 */
public interface SubstanceService extends EntityService<SubstanceQueryParams>, FormRequestHandler<List<Item>> {

}