package org.msh.etbm.commons.entities;

import org.apache.commons.beanutils.PropertyUtils;
import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.Displayable;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.entities.cmdlog.EntityCmdLogHandler;
import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLogUtils;
import org.msh.etbm.commons.entities.impl.ObjectUtils;
import org.msh.etbm.commons.messages.MessageKeyResolver;
import org.msh.etbm.commons.messages.MessageList;
import org.msh.etbm.db.Synchronizable;
import org.msh.etbm.db.WorkspaceData;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.db.repositories.WorkspaceRepository;
import org.msh.etbm.services.usersession.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Abstract class to provide a complete CRUD solution for entity services.
 *
 * Created by rmemoria on 17/10/15.
 */
public abstract class EntityService<E extends Synchronizable, R extends CrudRepository> {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    UserSession userSession;

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    ObjectUtils objectUtils;

    @Autowired
    MessageKeyResolver messageKeyResolver;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * The crud repository reference
     */
    private R crudRepository;

    /**
     * The Crud repository class
     */
    private Class<R> crudRepositoryClass;

    /**
     * The entity class
     */
    private Class<E> entityClass;


    /**
     * Create a new entity based on the given values
     * @param req list of all values in the format field x value
     * @return the primary key generated for the entity
     */
    @Transactional
    @CommandLog(type = EntityCmdLogHandler.CREATE, handler = EntityCmdLogHandler.class)
    public ServiceResult create(@Valid @NotNull Object req) {
        E entity = createEntityInstance();

        mapper.map(req, entity);

        // generate the result object to be sent to the client
        ServiceResult res = createResult(entity);

        MessageList msgs = res.getValidationErrors();

        try {
            // prepare entity to be saved
            prepareToSave(entity, msgs);
        } catch (EntityValidationException e) {
            msgs.add(e.getField(), e.getMessage());
        }

        // any error during preparation ?
        if (msgs.size() > 0) {
            return res;
        }

        // save the entity
        saveEntity(entity);
        res.setId( entity.getId() );

        res.setLogValues(createValuesToLog(entity, Operation.NEW));

        return res;
    }


    /**
     * Check if the given entity is unique by searching by the given field
     * @param entity
     * @param field
     * @return true if the entity is unique
     */
    protected boolean checkUnique(E entity, String field) {
        String hql = "select count(*) from " + getEntityClass().getSimpleName() + " where workspace.id = :wsid " +
                "and " + field + " = :" + field;
        if (entity.getId() != null) {
            hql += " and id <> :id";
        }

        // get the field value in the given object
        Object val;
        try {
            val = PropertyUtils.getProperty(entity, field);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Query qry = entityManager
                .createQuery(hql)
                .setParameter("wsid", getWorkspaceId())
                .setParameter(field, val);

        if (entity.getId() != null) {
            qry.setParameter("id", entity.getId());
        }

        // query the database
        Number count = (Number)qry.getSingleResult();

        return count.intValue() == 0;
    }


    /**
     * Update the values of the entity
     * @param id
     * @param req the object request with information about fields to be updated
     * @return
     */
    @Transactional
    @CommandLog(type = EntityCmdLogHandler.UPDATE, handler = EntityCmdLogHandler.class)
    public ServiceResult update(UUID id, Object req) {
        R rep = getCrudRepository();

        E entity = (E)getCrudRepository().findOne(id);

        if (entity == null) {
            throw new IllegalArgumentException("entity not found");
        }

        // get initial state
        ObjectValues prevVals = createValuesToLog(entity, Operation.EDIT);

        // set the values to the entity
        mapper.map(req, entity);

        // create diff list
        ObjectValues newVals = createValuesToLog(entity, Operation.EDIT);
        Diffs diffs = createDiffs(prevVals, newVals);

        // is there anything to save?
        if (diffs.getValues().size() == 0) {
            return null;
        }

        // create result object
        ServiceResult res = createResult(entity);
        MessageList msgs = res.getValidationErrors();

        try {
            // validate new values
            prepareToSave(entity, msgs);
        }
        catch (EntityValidationException e) {
            msgs.add(e.getField(), e.getMessage());
        }

        if (res.getValidationErrors().size() > 0) {
            return res;
        }

        // save the entity
        saveEntity(entity);

        // generate the result
        res.setLogDiffs(diffs);

        return res;
    }

    @Transactional
    @CommandLog(type = EntityCmdLogHandler.DELETE, handler = EntityCmdLogHandler.class)
    public ServiceResult delete(UUID id) {
        R rep = getCrudRepository();
        E entity = (E)rep.findOne(id);
        if (entity == null) {
            throw new IllegalArgumentException("Entity not found");
        }

        // create result to be sent back to the client
        ServiceResult res = createResult(entity);
        MessageList msgs = res.getValidationErrors();

        try {
            // prepare entity to be deleted
            prepareToDelete(entity, msgs);
        } catch (EntityValidationException e) {
            msgs.add(e.getField(), e.getMessage());
        }

        if (msgs.size() > 0) {
            return res;
        }

        // delete the entity
        deleteEntity(entity);

        // generate the values to log
        res.setLogValues(createValuesToLog(entity, Operation.DELETE));

        return res;
    }


    /**
     * Prepare entity for saving, checking its workspace and if the entity is unique
     * @param entity
     * @param msgs the list of possible validation errors along the preparation
     */
    protected void prepareToSave(E entity, MessageList msgs) throws EntityValidationException {
        checkWorkspace(entity);
    }

    /**
     * Make any preparation before deleting the entity
     * @param entity the entity to be deleted
     * @param msgs the list of possible validation errors along the preparation
     */
    protected void prepareToDelete(E entity, MessageList msgs) throws EntityValidationException {
        checkWorkspace(entity);
    }

    /**
     * Search for an entity by its ID
     * @param id
     * @param resultClass
     * @param <K>
     * @return
     */
    public <K> K findOne(UUID id, Class<K> resultClass) {
        E ent = (E)getCrudRepository().findOne(id);
        if (ent == null) {
            return null;
        }

        return mapper.map(ent, resultClass);
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
        return objectUtils.compareOldAndNew(prevVals, newVals);
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

        MessageList lst = messageKeyResolver.createMessageList();
        res.setValidationErrors(lst);

        return res;
    }

    /**
     * Simple method to intercept the moment before saving the entity
     * @param entity
     */
    protected void saveEntity(E entity) {
        getCrudRepository().save(entity);
    }


    /**
     * Simple method to intercept the moment before deleting the entity
     * @param entity
     */
    protected void deleteEntity(E entity) {
        getCrudRepository().delete(entity);
    }


    /**
     * Check the workspace in use by the entity. If no workspace is set, the workspace of the current
     * user is set. If the workspace is set and it is different of the current user workspace, an exception
     * is thrown indicating it is an invalid workspace
     * @param entity
     */
    protected void checkWorkspace(E entity) {
        if (!(entity instanceof WorkspaceData)) {
            return;
        }

        WorkspaceData wsdata = (WorkspaceData)entity;
        UUID wsid = getWorkspaceId();

        if (wsdata.getWorkspace() != null) {
            if (!wsdata.getWorkspace().getId().equals(wsid)) {
                throw new IllegalArgumentException("Invalid workspace in request");
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
        return userSession.getUserWorkspace().getWorkspace().getId();
    }


    /**
     * Return the crud repository
     * @return
     */
    protected R getCrudRepository() {
        if (crudRepository == null) {
            Class<R> clazz = getCrudRepositoryClass();
            crudRepository = applicationContext.getBean(clazz);
        }
        return crudRepository;
    }


    /**
     * Return the class of the CRUD repository component
     * @return
     */
    protected Class<R> getCrudRepositoryClass() {
        if (crudRepositoryClass == null) {
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) type;
                if (paramType.getActualTypeArguments().length == 2) {
                    crudRepositoryClass = (Class<R>) paramType.getActualTypeArguments()[1];
                }
                else {
                    throw new RuntimeException("Could not get repository class");
                }
            }
        }
        return crudRepositoryClass;
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
     * @return
     */
    protected E createEntityInstance() {
        Class<E> clazz = getEntityClass();
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
