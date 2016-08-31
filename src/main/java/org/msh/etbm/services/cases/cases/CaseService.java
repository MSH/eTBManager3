package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.query.QueryResult;

import java.util.List;

/**
 * Created by msantos on 26/3/16.
 */
public interface CaseService extends EntityService<CaseQueryParams> {

    /**
     * Search patient during new notification flush.
     * @param params
     * @return list of last patients that fit the params and its last case information
     */
    QueryResult<PatientSearchItem> searchPatient(PatientSearchQueryParams params);
}
