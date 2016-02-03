package org.msh.etbm.services.admin.tags;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 6/1/16.
 */
public interface TagService extends EntityService<TagQueryParams> {
}
