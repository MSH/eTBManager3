package org.msh.etbm.commons.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A context shared between events inside an {@link EntityServiceImpl}. The context
 * allows event interceptors to share information.
 *
 * Created by rmemoria on 16/7/16.
 */
public class EntityServiceContext<E> {

    /**
     * The ID of the entity. This is null when called from the {@link EntityService#create(Object)} method
     */
    private UUID requestedId;

    /**
     * The real entity involved in the operation
     */
    private E entity;

    /**
     * The request object involved in the create/update operation
     */
    private Object request;

    /**
     * A generic list of parameters to be preserved between event interceptors
     */
    private Map parameters = new HashMap<>();


    protected EntityServiceContext(UUID requestedId, E entity, Object request) {
        this.requestedId = requestedId;
        this.entity = entity;
        this.request = request;
    }

    public UUID getRequestedId() {
        return requestedId;
    }

    public E getEntity() {
        return entity;
    }

    public Object getRequest() {
        return request;
    }

    public Map getParameters() {
        return parameters;
    }

}
