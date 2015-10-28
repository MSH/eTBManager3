package org.msh.etbm.commons.entities.query;

import java.util.List;

/**
 * Class to help construction of queries to return entities
 * Created by rmemoria on 28/10/15.
 */
public interface QueryBuilder<E> {
    /**
     * Set a parameter to be included in the query
     * @param paramName
     * @param value
     */
    void setParameter(String paramName, Object value);

    /**
     * Add a restriction to the query
     * @param restiction
     */
    void addRestriction(String restiction);

    /**
     * Return the number of entities found
     * @return
     */
    long getCount();

    /**
     * Return the result list of the query
     * @return
     */
    List<E> getResultList();

    /**
     * Create query result object containing the result of the query
     * @param destClass the destination class where values will be mapped to (using Dozer mapping strategy)
     * @return instance of {@link QueryResult}
     */
    QueryResult createQueryResult(Class destClass);
}
