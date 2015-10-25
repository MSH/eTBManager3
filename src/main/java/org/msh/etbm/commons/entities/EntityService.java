package org.msh.etbm.commons.entities;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.forms.FormValues;
import org.msh.etbm.db.Synchronizable;
import org.msh.etbm.db.WorkspaceData;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.db.repositories.WorkspaceRepository;
import org.msh.etbm.services.usersession.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.UUID;

/**
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
    public UUID createEntity(@Valid @NotNull Object req) {
        E entity = createEntityInstance();

        mapper.map(req, entity);
        checkWorkspace(entity);
        checkUnique(entity);

        CrudRepository rep = getCrudRepository();

        beforeSave(entity);
        rep.save(entity);

        return null;
    }


    /**
     * Update the values of the entity
     * @param id
     * @param req the object request with information about fields to be updated
     * @return
     */
    @Transactional
    public ObjectDiffValues updateEntity(UUID id, Object req) {
        R rep = getCrudRepository();

        E entity = (E)getCrudRepository().findOne(id);

        if (entity == null) {
            throw new IllegalArgumentException("entity not found");
        }

        checkWorkspace(entity);
        checkUnique(entity);

        // create diff list
        E ent2 = createEntityInstance();
        mapper.map(req, entity);
        ObjectDiffValues vals = objectUtils.compareOldAndNew(entity, ent2);

        // set the new values to the entity
        mapper.map(req, ent2);

        // save the entity
        rep.save(ent2);

        return vals;
    }

    @Transactional
    public ObjectValues removeEntity(UUID id) {
        R rep = getCrudRepository();
        E entity = (E)rep.findOne(id);
        if (entity == null) {
            throw new IllegalArgumentException("Entity not found");
        }
        rep.delete(entity);

        return null;
    }


    /**
     * Simple method to intercept the moment before saving the entity
     * @param entity
     */
    protected void beforeSave(E entity) {
        // do nothing
    }


    /**
     * Simple method to intercept the moment before deleting the entity
     * @param entity
     */
    protected void beforeDelete(E entity) {
        // do nothing
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
        UUID wsid = userSession.getUserWorkspace().getWorkspace().getId();

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
     * Check if the entity is unique. The test is done by calling the method isUniqueEntity. If its return is false,
     * so there is already an entity like that recorded, than an exception is thrown and saving is interrupted
     * @param entity the entity object to be checked (new or existing)
     */
    protected void checkUnique(E entity) {
        if (!isUniqueEntity(entity)) {
            throw new IllegalArgumentException("There is already an entity recorded with this information");
        }
    }

    /**
     * Check if the entity is unique
     * @param entity the entity object to be evaluated
     * @return true if the entity is unique in the database, otherwise, there is another entity and this one is duplicated
     */
    protected boolean isUniqueEntity(E entity) {
        return true;
    }

    /**
     * Validate the values of the entity
     * @param values
     * @return
     */
    public boolean validate(FormValues values) {
        return false;
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
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) type;
                if (paramType.getActualTypeArguments().length > 0) {
                    entityClass = (Class<E>) paramType.getActualTypeArguments()[0];
                }
                else {
                    throw new RuntimeException("Could not get entity class");
                }
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
