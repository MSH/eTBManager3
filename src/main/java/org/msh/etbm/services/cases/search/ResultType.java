package org.msh.etbm.services.cases.search;

/**
 * Options used in {@link CaseSearchRequest} to specify the query to be applied.
 * CASES_ONLY will just return the list of cases. COUNT_ONLY will just count the
 * number of cases.
 *
 * Created by rmemoria on 17/9/16.
 */
public enum ResultType {

    CASES_ONLY,
    COUNT_ONLY;

}
