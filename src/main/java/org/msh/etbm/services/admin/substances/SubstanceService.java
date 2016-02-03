package org.msh.etbm.services.admin.substances;

import org.msh.etbm.commons.ErrorMessages;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Substance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

/**
 * CRUD service to handle substance operations
 *
 * Created by rmemoria on 12/11/15.
 */
public interface SubstanceService extends EntityService<SubstanceQueryParams> {

}