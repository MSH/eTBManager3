package org.msh.etbm.commons.entities.query;

import java.util.List;
import java.util.UUID;

/**
 * Class to help construction of queries to return entities
 * Created by rmemoria on 28/10/15.
 */
public interface QueryBuilder<E> {

    /**
     * Select the join HQL instruction to be added after the HQL FROM clause
     * @param hql HQL join instruction
     */
    void setHqlJoin(String hql);

    /**
     * Set a query defined parameter. A parameter is defined in a restriction, for example
     * @param paramName the name of the parameter
     * @param value the value of the parameter
     */
    void setParameter(String paramName, Object value);

    /**
     * Add a restriction to the query
     * @param restiction an HQL restriction to be included in the WHERE clause
     */
    void addRestriction(String restiction);

    /**
     * Add a restriction with its parameter values. The parameters are automatically
     * parsed from the restriction and added to the list of parameters. If any parameter
     * is null, the restriction is not added
     * @param restriction the HQL restriction to be included in the query
     * @param args the parameter values
     */
    void addRestriction(String restriction, Object... args);

    /**
     * Add a like HQL restriction only if value is a string different of null. The instruction is the same as
     * add a restriction as 'field like :value%'
     * @param field the field name
     * @param value the string value of the like search
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
     * @return the order by key
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
     * Return the number of entities found based on the query restrictions. A count(*) HQL is issued
     * when this method is called
     * @return the number of entities found in the query
     */
    long getCount();

    /**
     * Return the result list of the query. The query is executed and the list is a simple return of the query
     * @return the list of entities
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
     * @return the result of the query
     */
    QueryResult createQueryResult();

    /**
     * Assign a data class by its profile name. When using initialize, the profile is automatically assigned
     * and issuing createQueryResult with no data class, the profile class is automatically assigned
     * @param profname the profile name to be passed in the query filters
     * @param dataClass the data class to be used when the profile key is selected
     */
    void addProfile(String profname, Class dataClass);

    /**
     * Add a default profile, when no profile is assigned
     * @param profname the profile name
     * @param dataClass the data class assigned to the profile
     */
    void addDefaultProfile(String profname, Class dataClass);

    /**
     * Initialize the query builder with the parameters in the entityQuery object
     * @param qry the query
     */
    void initialize(EntityQueryParams qry);

    /**
     * Select the profile to be used
     * @param profile the selected profile key to be used in the query
     */
    void setProfile(String profile);

    /**
     * Get the profile in use
     * @return the name of the profile key
     */
    String getProfile();


    /**
     * Change the entity class in use in the query builder
     * @param entity the entity class
     */
    void setEntityClass(Class entity);

    /**
     * Set the entity alias in use in the query builder
     * @param alias the entity alias
     */
    void setEntityAlias(String alias);


    /**
     * Set the ID of the workspace to be used to restrict the query result
     * @param wsid the UUID
     */
    void setWorkspaceId(UUID wsid);

    /**
     * Get the ID of the workspace to be used to restrict the query result
     * @return instance of UUID
     */
    UUID getWorkspaceId();
}
