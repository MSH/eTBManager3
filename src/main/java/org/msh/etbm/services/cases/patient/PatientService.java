package org.msh.etbm.services.cases.patient;

import org.msh.etbm.commons.entities.query.QueryResult;

/**
 * Created by msantos on 01/09/2016.
 */
public interface PatientService {

    /**
     * Search patient during new notification flush.
     * @param params
     * @return list of last patients that fit the params and its last case information
     */
    QueryResult<PatientSearchItem> searchPatient(PatientQueryParams params);
}
