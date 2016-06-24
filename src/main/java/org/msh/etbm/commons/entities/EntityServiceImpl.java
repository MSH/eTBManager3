package org.msh.etbm.commons.entities;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.msh.etbm.Messages;
import org.msh.etbm.commons.Displayable;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.entities.cmdlog.EntityCmdLogHandler;
import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLogUtils;
import org.msh.etbm.commons.entities.dao.EntityDAO;
import org.msh.etbm.commons.entities.dao.EntityDAOFactory;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.objutils.Diffs;
import org.msh.etbm.commons.objutils.DiffsUtils;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.objutils.ObjectValues;
import org.msh.etbm.db.Synchronizable;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Abstract class to provide a complete CRUD solution for entity services.
 *
 * Created by rmemoria on 17/10/15.
 */
public abstract class EntityServiceImpl<E extends Synchronizable, Q extends EntityQueryParams> implements EntityService<Q> {

    @Autowired
    UserRequestService userRequestService;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @Autowired
    EntityDAOFactory entityDAOFactory;

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

        EntityDAO<E> dao = createEntityDAO();

        dao.setEntity(entity);

        // set values from request to entity object (must ignore null values)
        mapRequest(req, dao.getEntity());

        validateAndSave(dao, req);

        // create the result of the service
        ServiceResult res = createResult(entity);
        res.setId( entity.getId() );

        res.setLogValues(createValuesToLog(entity, Operation.NEW));

        afterSave(dao.getEntity(), res, true);

        return res;
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
        EntityDAO<E> dao = createEntityDAO();

        dao.setId(id);

        // get initial state
        ObjectValues prevVals = createValuesToLog(dao.getEntity(), Operation.EDIT);

        // set the values to the entity
        mapRequest(req, dao.getEntity());

        // create diff list
        ObjectValues newVals = createValuesToLog(dao.getEntity(), Operation.EDIT);
        Diffs diffs = createDiffs(prevVals, newVals);

        // is there anything to save?
        if (diffs.getValues().size() == 0) {
            return createResult(dao.getEntity());
        }

        validateAndSave(dao, req);

        // create result object
        ServiceResult res = createResult(dao.getEntity());
        // generate the result
        res.setLogDiffs(diffs);

        afterSave(dao.getEntity(), res, false);

        return res;
    }


    @Transactional
    @CommandLog(type = EntityCmdLogHandler.DELETE, handler = EntityCmdLogHandler.class)
    @Override
    public ServiceResult delete(UUID id) {
        EntityDAO<E> dao = createEntityDAO();

        dao.setId(id);

        // create result to be sent back to the client
        ServiceResult res = createResult(dao.getEntity());

        // prepare entity to be deleted
        beforeDelete(dao.getEntity(), dao.getErrors());

        // check if there is any error
        if (dao.hasErrors()) {
            dao.raiseValidationError();
        }

        // generate the values to log
        res.setLogValues(createValuesToLog(dao.getEntity(), Operation.DELETE));

        // delete the entity
        dao.delete();

        afterDelete(dao.getEntity(), res);

        return res;
    }


    /**
     * Check if the given entity is unique by searching by the given field
     * @param entity the entity to check unique values
     * @param field the field (or comma separated list of fields) to check uniqueness
     * @return true if the entity is unique
     */
    protected boolean checkUnique(E entity, String field) {
        return checkUnique(entity, field, null);
    }


    /**
     * Check if the given entity is unique by searching by the given field
     * @param entity the entity to check unique values
     * @param field the field (or comma separated list of fields) to check uniqueness
     * @param restriction an optional restriction to be included in the HQL WHERE clause
     * @return true if the entity is unique
     */
    protected boolean checkUnique(E entity, String field, String restriction) {
        String hql = "select count(*) from " + getEntityClass().getSimpleName();

        List<String> criterias = new ArrayList<>();

        if (entity instanceof WorkspaceEntity) {
            criterias.add("workspace.id = :wsid");
        }

        String[] fields = field.split(",");
        for (String f: fields) {
            criterias.add(f + " = :" + f);
        }

        if (entity.getId() != null) {
            criterias.add("id <> :id");
        }

        // any restriction available
        if (restriction != null) {
            criterias.add(restriction);
        }

        if (criterias.size() > 0) {
            hql += " where " + StringUtils.join(criterias, " and ");
        }

        Query qry = entityManager
                .createQuery(hql);

        if (entity instanceof WorkspaceEntity) {
            qry.setParameter("wsid", getWorkspaceId());
        }

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

    protected void validateAndSave(EntityDAO<E> dao, Object req) {
        beforeValidate(dao.getEntity(), req);

        // validate entity data
        if (!dao.validate()) {
            dao.raiseValidationError();
        }

        // prepare entity to be saved
        beforeSave(dao.getEntity(), dao.getErrors());

        if (dao.hasErrors()) {
            dao.raiseValidationError();
        }

        // save the entity
        dao.save();
    }

    /**
     * Method that must be override in order to make any initialization before validation
     * @param entity
     * @param request
     */
    protected void beforeValidate(E entity, Object request) {
        // do nothing... To be implemented in the child class
    }

    /**
     * Prepare entity for saving, called right after the validation
     * @param entity the instance of the entity to be saved
     * @param errors a container to receive any validation error found during the method call
     */
    protected void beforeSave(E entity, Errors errors) {
        // do nothing... To be implemented in the child class
    }

    /**
     * Called after the entity is saved
     * @param entity the saved entity
     * @param res the result to be returned to the caller
     * @param isNew indicate if a new entity was saved, or if it is an update
     */
    protected void afterSave(E entity, ServiceResult res, boolean isNew) {
        // do nothing... To be implemented in the child class
    }

    /**
     * Make any preparation before deleting the entity
     * @param entity the entity to be deleted
     * @param errors the list of possible validation errors
     */
    protected void beforeDelete(E entity, Errors errors) {
        // do nothing... To be implemented in the child class
    }

    /**
     * Called after the entity is deleted
     * @param entity the deleted entity
     * @param res the data to be returned to the caller
     */
    protected void afterDelete(E entity, ServiceResult res) {
        // do nothing... To be implemented in the child class
    }

    /**
     * Copy the values of the request to the entity
     * @param request
     * @param entity
     */
    protected void mapRequest(Object request, E entity) {
        EntityDAO<E> dao = createEntityDAO();
        dao.setEntity(entity);
        dao.mapToEntity(request);
    }

    /**
     * Generate the response from the entity
     * @param entity the entity class
     * @param resultClass the class of the response data to be created
     * @param <K>
     * @return instance of the response data
     */
    protected <K> K mapResponse(E entity, Class<K> resultClass) {
        EntityDAO<E> dao = createEntityDAO();

        dao.setEntity(entity);

        return dao.mapFromEntity(resultClass);
    }


    /**
     * Create new DAO to handle CRUD operations
     * @return instance of {@link EntityDAO}
     */
    protected EntityDAO<E> createEntityDAO() {
        return entityDAOFactory.newDAO(getEntityClass());
    }

    /**
     * Create a new instance of {@link EntityDAO} for a given entity class
     * @param entityClass the class to create an EntityDAO for
     * @param <K> the generic class to be used
     * @return instance of {@link EntityDAO}
     */
    protected <K> EntityDAO<K> createEntityDAO(Class<K> entityClass) {
        return entityDAOFactory.newDAO(entityClass);
    }

    /**
     * Search for an entity by its ID
     * @param id
     * @param resultClass
     * @return the instance of resultClass containing the mapped entity values
     */
    @Override
    @Transactional
    public <K> K findOne(UUID id, Class<K> resultClass) {
        EntityDAO<E> dao = createEntityDAO();

        dao.setId(id);

        return dao.mapFromEntity(resultClass);
    }

    /**
     * Create the list of values to be logged
     * @param entity
     * @param oper
     * @return
     */
    protected ObjectValues createValuesToLog(E entity, Operation oper) {
        Class pureClass = Hibernate.getClass(entity);
        return PropertyLogUtils.generateLog(entity, pureClass, oper);
    }

    /**
     * Create the difference between two set of object values
     * @param prevVals
     * @param newVals
     * @return
     */
    protected Diffs createDiffs(ObjectValues prevVals, ObjectValues newVals) {
        return DiffsUtils.generateDiffsFromValues(prevVals, newVals);
    }


    /**
     * Return the current workspace ID
     * @return instance of UUID
     */
    protected UUID getWorkspaceId() {
        return userRequestService.getUserSession().getWorkspaceId();
    }

    /**
     * Create the result to be returned by the create, update or delete operation
     * @param entity the entity involved in the operation
     * @return instance of {@link ServiceResult}
     */
    protected ServiceResult createResult(E entity) {
        ServiceResult res = new ServiceResult();
        res.setId(entity.getId());
        res.setEntityClass(getEntityClass());
        if (entity instanceof Displayable) {
            res.setEntityName(((Displayable)entity).getDisplayString());
        } else {
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
        } catch (EntityNotFoundException e) {
            return null;
        }
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
     * Raise exception of a required field not present
     * @param obj the object related to the exception
     * @param field the name of the field
     */
    protected void raiseRequiredFieldException(Object obj, String field) {
        rejectFieldException(obj, field, Messages.REQUIRED);
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

        buildQuery(builder, qryParams);

        builder.initialize(qryParams);

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
    }
}
