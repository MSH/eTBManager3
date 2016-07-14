package org.msh.etbm.services.admin.errorlogrep;

import org.msh.etbm.commons.entities.query.QueryResult;

/**
 * Created by msantos on 05/7/16.
 */
public interface ErrorLogRepService {
    QueryResult getResult(ErrorLogRepQueryParams query);
}
