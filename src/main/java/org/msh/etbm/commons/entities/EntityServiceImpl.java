package org.msh.etbm.commons.entities;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.Displayable;
import org.msh.etbm.commons.ErrorMessages;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.entities.cmdlog.EntityCmdLogHandler;
import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLogUtils;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.messages.MessageKeyResolver;
import org.msh.etbm.commons.messages.MessageList;
import org.msh.etbm.commons.objutils.ObjectDiffs;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.Synchronizable;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.db.repositories.WorkspaceRepository;
import org.msh.etbm.services.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Abstract class to provide a complete CRUD solution for entity services.
 *
 * Created by rmemoria on 17/10/15.
 */
public abstract class EntityServiceImpl<E extends Synchronizable, Q extends EntityQueryParams> implements EntityService<Q> {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    MessageKeyResolver messageKeyResolver;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    Validator validator;

    @Autowired
    QueryBuilderFactory queryBuilderFactory;


    /**
     * The entity class
     */
    private Class<E> entityClass;

    /**
     * Create a new entity based on the given request object. The request object depends on the implementation
     * and contains the values to create a new entity
     * @param req list of all values in the format field x value
     * @return service result containing information about the new entity generated
     */
    @Transactional
    @CommandLog(type = EntityCmdLogHandler.CREATE, handler = EntityCmdLogHandler.class)
    @Override
    public ServiceResult create(@Valid @NotNull Object req) {
        // create a new instance of the entity
        E entity = createEntityInstance(req);

        // set values from request to entity object (must ignore null values)
        mapRequest(req, entity);

        BindingResult bindingResult = createBindingResult(entity);

        // prepare entity to be saved
        prepareToSave(entity, bindingResult);

        // validation errors?
        if (bindingResult.hasErrors()) {
            throw new EntityValidationException(bindingResult);
        }

        saveEntity(entity);

        // create the result of the service
        ServiceResult res = createResult(entity);
        res.setId( entity.getId() );

        res.setLogValues(createValuesToLog(entity, Operation.NEW));

        return res;
    }

    /**
     * Validate the entity
     * @param entity the entity to be validated
     * @return instance of BindingResult to receive the validation errors;
     */
    private void validateEntity(E entity, BindingResult bindingResult) {
        validator.validate(entity, bindingResult);
    }


    /**
     * Create a binding result to store validation error messages
     * @param entity the entity assgined to the binding result
     * @return instance of {@link BindingResult}
     */
    protected BindingResult createBindingResult(Object entity) {
        return new BeanPropertyBindingResult(entity, getEntityClass().getSimpleName());
    }


    /**
     * Raise entity not found exception passing the ID of the entity
     * @param id
     */
    protected void raiseEntityNotFoundException(UUID id) {
        throw new  EntityNotFoundException("Entity not found " + getEntityClass().getSimpleName() + " with ID = " + id);
    }

    /**
     * Update the values of the entity
     * @param id the entity ID
     * @param req the object request with information about fields to be updated
     * @return
     */
    @Transactional
    @CommandLog(type = EntityCmdLogHandler.UPDATE, handler = EntityCmdLogHandler.class)
    @Override
    public ServiceResult update(UUID id, Object req) {
        E entity = findEntity(id);

        if (entity == null) {
            raiseEntityNotFoundException(id);
        }

        // get initial state
        ObjectValues prevVals = createValuesToLog(entity, Operation.EDIT);

        // set the values to the entity
        mapRequest(req, entity);

        // create diff list
        ObjectValues newVals = createValuesToLog(entity, Operation.EDIT);
        Diffs diffs = createDiffs(prevVals, newVals);

        // is there anything to save?
        if (diffs.getValues().size() == 0) {
            return createResult(entity);
        }

        BindingResult bindingResult = createBindingResult(entity);

        // prepare object to save
        prepareToSave(entity, bindingResult);

        // validation errors?
        if (bindingResult.hasErrors()) {
            throw new EntityValidationException(bindingResult);
        }

        // save the entity
        saveEntity(entity);

        // create result object
        ServiceResult res = createResult(entity);
        // generate the result
        res.setLogDiffs(diffs);

        return res;
    }


    @Transactional
    @CommandLog(type = EntityCmdLogHandler.DELETE, handler = EntityCmdLogHandler.class)
    public ServiceResult delete(UUID id) {
        E entity = findEntity(id);
        if (entity == null) {
            raiseEntityNotFoundException(id);
        }

        // create result to be sent back to the client
        ServiceResult res = createResult(entity);

        BindingResult bindingResult = createBindingResult(entity);
        // prepare entity to be deleted
        prepareToDelete(entity, bindingResult);

        // delete the entity
        deleteEntity(entity);

        // generate the values to log
        res.setLogValues(createValuesToLog(entity, Operation.DELETE));

        return res;
    }


    /**
     * Check if the given entity is unique by searching by the given field
     * @param entity the entity to check unique values
     * @param field the field (or comma separated list of fields) to check uniqueness
     * @return true if the entity is unique
     */
    protected boolean checkUnique(E entity, String field) {
        String hql = "select count(*) from " + getEntityClass().getSimpleName() + " where workspace.id = :wsid ";

        String[] fields = field.split(",");
        for (String f: fields) {
            hql += " and " + f + " = :" + f;
        }

        if (entity.getId() != null) {
            hql += " and id <> :id";
        }

        Query qry = entityManager
                .createQuery(hql)
                .setParameter("wsid", getWorkspaceId());

        if (entity.getId() != null) {
            qry.setParameter("id", entity.getId());
        }

        // get the field value in the given object
        for (String f: fields) {
            Object val = ObjectUtils.getProperty(entity, f);
            qry.setParameter(f, val);
        }

        // query the database
        Number count = (Number)qry.getSingleResult();

        return count.intValue() == 0;
    }


    /**
     * Prepare entity for saving, checking its workspace and if the entity is unique
     * @param entity
     * @param bindingResult the list of possible validation errors along the preparation
     */
    protected void prepareToSave(E entity, BindingResult bindingResult) {
        checkWorkspace(entity, bindingResult);
        validateEntity(entity, bindingResult);
    }

    /**
     * Make any preparation before deleting the entity
     * @param entity the entity to be deleted
     */
    protected void prepareToDelete(E entity, BindingResult bindingResult) {
        checkWorkspace(entity, bindingResult);
    }

    /**
     * Copy the values of the request to the entity
     * @param request
     * @param entity
     */
    protected void mapRequest(Object request, E entity) {
        mapper.map(request, entity);
    }

    /**
     * Generate the response from the entity
     * @param entity the entity class
     * @param resultClass the class of the response data to be created
     * @param <K>
     * @return instance of the response data
     */
    protected <K> K mapResponse(E entity, Class<K> resultClass) {
        return mapper.map(entity, resultClass);
    }

    /**
     * Search for an entity by its ID
     * @param id
     * @param resultClass
     * @return the instance of resultClass containing the mapped entity values
     */
    @Override
    public <K> K findOne(UUID id, Class<K> resultClass) {
        E ent = findEntity(id);
        if (ent == null) {
            raiseEntityNotFoundException(id);
        }

        return mapResponse(ent, resultClass);
    }

    /**
     * Create the list of values to be logged
     * @param entity
     * @param oper
     * @return
     */
    protected ObjectValues createValuesToLog(E entity, Operation oper) {
        return PropertyLogUtils.generateLog(entity, getEntityClass(), oper);
    }

    /**
     * Create the difference between two set of object values
     * @param prevVals
     * @param newVals
     * @return
     */
    protected Diffs createDiffs(ObjectValues prevVals, ObjectValues newVals) {
        return ObjectDiffs.compareOldAndNew(prevVals, newVals);
    }


    /**
     * Create the result to be returned by the method call
     * @param entity
     * @return
     */
    protected ServiceResult createResult(E entity) {
        ServiceResult res = new ServiceResult();
        res.setId(entity.getId());
        res.setEntityClass(getEntityClass());
        if (entity instanceof Displayable) {
            res.setEntityName(((Displayable)entity).getDisplayString());
        }
        else {
            res.setEntityName(entity.toString());
        }

        return res;
    }


    /**
     * Return the entity by its given id
     * @param id the entity primary key
     * @return entity object
     */
    protected E findEntity(Object id) {
        try {
            return entityManager.find(getEntityClass(), id);
        }
        catch (EntityNotFoundException e) {
            return null;
        }
    }

    /**
     * Create the list of messages for validation error messages
     * @return instance of MessageList object
     */
    protected MessageList createMessageList() {
        return messageKeyResolver.createMessageList();
    }

    /**
     * Raise exception of a required field not present
     * @param obj the object related to the exception
     * @param field the name of the field
     */
    protected void raiseRequiredFieldException(Object obj, String field) {
        rejectFieldException(obj, field, ErrorMessages.REQUIRED);
    }

    /**
     * Raise a validation error exception. The exception is thrown immediately and there will be
     * just one field assigned to it
     * @param obj the object related to the validation error
     * @param field the field with an error
     * @param msg the message with the validation error
     */
    protected void rejectFieldException(Object obj, String field, String msg) {
        BindingResult res = createBindingResult(obj);
        res.rejectValue(field, msg);
        throw new EntityValidationException(res);
    }

    /**
     * Simple method to intercept the moment before saving the entity
     * @param entity
     */
    protected void saveEntity(E entity) {
        entityManager.persist(entity);
    }


    /**
     * Simple method to intercept the moment before deleting the entity
     * @param entity
     */
    protected void deleteEntity(E entity) {
        entityManager.remove(entity);
        entityManager.flush();
    }


    /**
     * Check the workspace in use by the entity. If no workspace is set, the workspace of the current
     * user is set. If the workspace is set and it is different of the current user workspace, an exception
     * is thrown indicating it is an invalid workspace
     * @param entity
     * @param bindingResult
     */
    protected void checkWorkspace(E entity, BindingResult bindingResult) {
        if (!(entity instanceof WorkspaceEntity)) {
            return;
        }

        WorkspaceEntity wsdata = (WorkspaceEntity)entity;
        UUID wsid = getWorkspaceId();

        if (wsdata.getWorkspace() != null) {
            if (!wsdata.getWorkspace().getId().equals(wsid)) {
                bindingResult.rejectValue("id", "Invalid workspace");
            }
        }
        else {
            Workspace ws = workspaceRepository.findOne(wsid);
            wsdata.setWorkspace(ws);
        }
    }

    /**
     * Return the ID of the workspace in use by the current user
     * @return
     */
    protected UUID getWorkspaceId() {
        return userRequestService.getUserSession().getWorkspaceId();
    }


    /**
     * Return the class of the entity being managed
     * @return
     */
    protected Class<E> getEntityClass() {
        if (entityClass == null) {
            entityClass = ObjectUtils.getGenericType(getClass(), 0);
            if (entityClass == null) {
                throw new RuntimeException("Could not get entity class");
            }
        }
        return entityClass;
    }

    /**
     * Create a new instance of the entity class
     * @param req the object used as request for this service
     * @return
     */
    protected E createEntityInstance(Object req) {
        Class<E> clazz = getEntityClass();
        return ObjectUtils.newInstance(clazz);
    }

    @Override
    public QueryResult findMany(Q qryParams) {
        QueryBuilder<E> builder = queryBuilderFactory.createQueryBuilder( getEntityClass() );

        builder.initialize(qryParams);

        buildQuery(builder, qryParams);

        return builder.createQueryResult();
    }

    /**
     * Return the entity manager in use
     * @return instance of {@link EntityManager}
     */
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Called when the method {@link EntityServiceImpl#findMany(EntityQueryParams)} is invoked, in order to
     * build the query that will return the entities
     * @param builder instance of the {@link QueryBuilder}
     * @param queryParams the parameters sent from the client to return the entities
     */
    protected void buildQuery(QueryBuilder<E> builder, Q queryParams) {
        // nothing to do
    };
}
