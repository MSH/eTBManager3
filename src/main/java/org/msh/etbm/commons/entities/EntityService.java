package org.msh.etbm.commons.entities;

import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.commons.entities.query.QueryResult;

import java.util.UUID;

/**
 * Entity service interface that is implemented by the EntityServiceImpl, in order to provide an
 * easy way to expose the methods of an entity service in an interface way
 *
 * Created by rmemoria on 3/2/16.
 */
public interface EntityService<Q extends EntityQueryParams> {

    /**
     * Create a new entity based on the given request object. The request object depends on the implementation
     * and contains the values to create a new entity
     * @param req list of all values in the format field x value
     * @return service result containing information about the new entity generated
     */
    ServiceResult create(Object req);

    /**
     * Update the values of the entity
     * @param id the entity ID
     * @param req the object request with information about fields to be updated
     * @return information about the updated data
     */
    ServiceResult update(UUID id, Object req);

    /**
     * Delete an entity by its given ID. If the entity is not found, an {@link javax.persistence.EntityNotFoundException}
     * exception will be thrown
     * @param id the entity ID
     * @return information about the deleted operation
     */
    ServiceResult delete(UUID id);

    /**
     * Search for an entity by its ID. If entity is not found, an {@link javax.persistence.EntityNotFoundException} exception
     * will be thrown
     * @param id the ID of the entity
     * @param resultClass the class to create an instance and map entity values to
     * @param <K> the excepted class type to return
     * @return the instance of the given class with the entity values
     */
    public <K> K findOne(UUID id, Class<K> resultClass);

    /**
     * Search for entities based on the given query params
     * @param queryParams an instance of the {@link EntityQueryParams} containing criterias to search for the entities
     * @return the query result, with the list and number of entities found
     */
    public QueryResult findMany(Q queryParams);
}
