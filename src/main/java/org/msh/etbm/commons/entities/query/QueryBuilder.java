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
     * Add a like restriction only if value is a string different of null
     * @param field
     * @param value
     */
    void addLikeRestriction(String field, String value);

    /**
     * Add an orderBy map, which will map a key (informed by the consumer) and it(s) field(s) to
     * be used in the order by instruction
     * @param key the key to reference the order by fields
     * @param field the fields to be used in the order by operation
     */
    void addOrderByMap(String key, String field);

    /**
     * Add an orderBy map as the default, if none is specified
     * be used in the order by instruction
     * @param key the key to reference the order by fields
     * @param field the fields to be used in the order by operation
     */
    void addDefaultOrderByMap(String key, String field);

    /**
     * Get the order by key to use
     * @return
     */
    String getOrderByKey();

    /**
     * Set the order by key to use in the query
     * @param key is the key used in the orderByMap
     */
    void setOrderByKey(String key);

    /**
     * Set if order by will be ascending or descending (default is ascending)
     * @param value true indicate it is descending, and false is ascending
     */
    void setOrderByDescending(boolean value);

    /**
     * Return true if the order by operation will be descending
     * @return boolean value
     */
    boolean isOrderByDescending();

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

    /**
     * Query database by using the selected profile as the data class
     * @return
     */
    QueryResult createQueryResult();

    /**
     * Assign a data class by its profile name. When using initialize, the profile is automatically assigned
     * and issuing createQueryResult with no data class, the profile class is automatically assigned
     * @param profname the
     * @param dataClass
     */
    void addProfile(String profname, Class dataClass);

    /**
     * Add a default profile, when no profile is assigned
     * @param profname
     * @param dataClass
     */
    void addDefaultProfile(String profname, Class dataClass);

    /**
     * Initialize the query builder with the parameters in the entityQuery object
     * @param qry the query
     */
    void initialize(EntityQuery qry);

    /**
     * Select the profile to be used
     * @param profile
     */
    void setProfile(String profile);

    /**
     * Get the profile in use
     * @return
     */
    String getProfile();

}
