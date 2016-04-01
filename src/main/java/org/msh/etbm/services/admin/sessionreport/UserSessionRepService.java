package org.msh.etbm.services.admin.sessionreport;

import org.msh.etbm.commons.entities.query.QueryResult;

/**
 * Created by msantos on 11/3/16.
 */
public interface UserSessionRepService {
    QueryResult getResultByDay(UserSessionRepQueryParams query);

    QueryResult getResult(UserSessionRepQueryParams query);
}
