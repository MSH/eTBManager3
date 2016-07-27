package org.msh.etbm.commons.entities;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.msh.etbm.Messages;
import org.msh.etbm.commons.Displayable;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandType;
import org.msh.etbm.commons.commands.CommandTypes;
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
import org.msh.etbm.db.CaseEntity;
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
 * <p>
 * Created by rmemoria on 17/10/15.
 */
public abstract class EntityServiceImpl<E extends Synchronizable, Q extends EntityQueryParams> implements EntityService<Q> {

    @Autowired
    protected UserRequestService userRequestService;

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
     * Return the command type used to register the entity service
     * @return instance of the {@link CommandType}
     */
    public abstract String getCommandType();

    /**
     * Create a new entity based on the given request object. The request object depends on the implementation
     * and contains the values to create a new entity
     *
     * @param req list of all values in the format field x value
     * @return service result containing information about the new entity generated
     */
    @Transactional
    @CommandLog(type = CommandTypes.CMD_CREATE, handler = EntityCmdLogHandler.class)
    @Override
    public ServiceResult create(@Valid @NotNull Object req) {
        // create a new instance of the entity
        E entity = createEntityInstance(req);

        EntityDAO<E> dao = createEntityDAO();

        dao.setEntity(entity);

        EntityServiceContext<E> context = createContext(null, entity, req);

        // set values from request to entity object (must ignore null values)
        mapRequest(context);

        validateAndSave(context, dao);

        // create the result of the service
        ServiceResult res = createResult(entity);
        res.setId(entity.getId());

        res.setLogValues(createValuesToLog(entity, Operation.NEW));
        res.setOperation(Operation.NEW);

        afterSave(context, res);

        return res;
    }


    /**
     * Update the values of the entity
     *
     * @param id  the entity ID
     * @param req the object request with information about fields to be updated
     * @return
     */
    @Transactional
    @CommandLog(type = CommandTypes.CMD_UPDATE, handler = EntityCmdLogHandler.class)
    @Override
    public ServiceResult update(UUID id, Object req) {
        EntityDAO<E> dao = createEntityDAO();

        dao.setId(id);

        // get initial state
        ObjectValues prevVals = createValuesToLog(dao.getEntity(), Operation.EDIT);

        EntityServiceContext<E> context = createContext(id, dao.getEntity(), req);

        // set the values to the entity
        mapRequest(context);

        // create diff list
        ObjectValues newVals = createValuesToLog(dao.getEntity(), Operation.EDIT);
        Diffs diffs = createDiffs(prevVals, newVals);

        // is there anything to save?
        if (diffs.getValues().size() == 0) {
            return createResult(dao.getEntity());
        }

        validateAndSave(context, dao);

        // create result object
        ServiceResult res = createResult(dao.getEntity());
        // generate the result
        res.setLogDiffs(diffs);
        res.setOperation(Operation.EDIT);

        afterSave(context, res);

        return res;
    }


    @Transactional
    @CommandLog(type = CommandTypes.CMD_DELETE, handler = EntityCmdLogHandler.class)
    @Override
    public ServiceResult delete(UUID id) {
        EntityDAO<E> dao = createEntityDAO();

        dao.setId(id);

        // create result to be sent back to the client
        ServiceResult res = createResult(dao.getEntity());

        EntityServiceContext<E> context = createContext(id, dao.getEntity(), null);

        // prepare entity to be deleted
        beforeDelete(context, dao.getErrors());

        // check if there is any error
        if (dao.hasErrors()) {
            dao.raiseValidationError();
        }

        // generate the values to log
        res.setLogValues(createValuesToLog(dao.getEntity(), Operation.DELETE));

        // delete the entity
        dao.delete();

        res.setOperation(Operation.DELETE);

        afterDelete(context, res);

        return res;
    }


    /**
     * Check if the given entity is unique by searching by the given field
     *
     * @param entity the entity to check unique values
     * @param field  the field (or comma separated list of fields) to check uniqueness
     * @return true if the entity is unique
     */
    protected boolean checkUnique(E entity, String field) {
        return checkUnique(entity, field, null);
    }

    /**
     * Check if a given entity for a given class is unique. THis is a generic way of checking unique entities
     * that are not related to the entity class of this service
     *
     * @param entityClass The entity class to check from
     * @param entity      The entity to evaluate the values
     * @param field       The field to check uniqueness
     * @param restriction Any optional restriction to the method
     * @return true if entity is unique
     */
    protected boolean checkUnique(Class<? extends Synchronizable> entityClass, Synchronizable entity, String field, String restriction) {
        String hql = "select count(*) from " + entityClass.getSimpleName();

        List<String> criterias = new ArrayList<>();

        if (entity instanceof WorkspaceEntity) {
            criterias.add("workspace.id = :wsid");
        }

        String[] fields = field.split(",");
        for (String f : fields) {
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
        for (String f : fields) {
            Object val = ObjectUtils.getProperty(entity, f);
            qry.setParameter(f, val);
        }

        // query the database
        Number count = (Number) qry.getSingleResult();

        return count.intValue() == 0;
    }

    /**
     * Check if the given entity is unique by searching by the given field
     *
     * @param entity      the entity to check unique values
     * @param field       the field (or comma separated list of fields) to check uniqueness
     * @param restriction an optional restriction to be included in the HQL WHERE clause
     * @return true if the entity is unique
     */
    protected boolean checkUnique(E entity, String field, String restriction) {
        return checkUnique((Class<Synchronizable>) getEntityClass(), entity, field, restriction);
    }

    protected void validateAndSave(EntityServiceContext<E> context, EntityDAO<E> dao) {
        beforeValidate(context);

        // validate entity data
        if (!dao.validate()) {
            dao.raiseValidationError();
        }

        // prepare entity to be saved
        beforeSave(context, dao.getErrors());

        if (dao.hasErrors()) {
            dao.raiseValidationError();
        }

        // save the entity
        dao.save();
    }

    /**
     * Method that must be override in order to make any initialization before validation
     *
     * @param context object containing information about the requested operation
     */
    protected void beforeValidate(EntityServiceContext<E> context) {
        // do nothing... To be implemented in the child class
    }

    /**
     * Prepare entity for saving, called right after the validation
     *
     * @param context object with information about the entity being saved
     * @param errors a container to receive any validation error found during the method call
     */
    protected void beforeSave(EntityServiceContext<E> context, Errors errors) {
        // do nothing... To be implemented in the child class
    }

    /**
     * Called after the entity is saved
     *
     * @param context object containing information about the entity
     * @param res    the result to be returned to the caller
     */
    protected void afterSave(EntityServiceContext<E> context, ServiceResult res) {
        // do nothing... To be implemented in the child class
    }

    /**
     * Make any preparation before deleting the entity
     *
     * @param context object containing information about the entity
     * @param errors the list of possible validation errors
     */
    protected void beforeDelete(EntityServiceContext<E> context, Errors errors) {
        // do nothing... To be implemented in the child class
    }

    /**
     * Called after the entity is deleted
     *
     * @param context object containing information about the entity
     * @param res    the data to be returned to the caller
     */
    protected void afterDelete(EntityServiceContext<E> context, ServiceResult res) {
        // do nothing... To be implemented in the child class
    }

    /**
     * Create a new object context to be passed to interceptors during CRUD operations
     * @param id the ID of the entity (if available)
     * @param entity the entity
     * @param request the request object that originated the operation
     * @return
     */
    protected EntityServiceContext<E> createContext(UUID id, E entity, Object request) {
        return new EntityServiceContext<>(id, entity, request);
    }

    /**
     * Copy the values of the request to the entity
     *
     * @param context The context shared among interceptors, with information about the entity
     */
    protected void mapRequest(EntityServiceContext<E> context) {
        EntityDAO<E> dao = createEntityDAO();
        dao.setEntity(context.getEntity());
        dao.mapToEntity(context.getRequest());
    }

    /**
     * Generate the response from the entity
     *
     * @param entity      the entity class
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
     *
     * @return instance of {@link EntityDAO}
     */
    protected EntityDAO<E> createEntityDAO() {
        return entityDAOFactory.newDAO(getEntityClass());
    }

    /**
     * Create a new instance of {@link EntityDAO} for a given entity class
     *
     * @param entityClass the class to create an EntityDAO for
     * @param <K>         the generic class to be used
     * @return instance of {@link EntityDAO}
     */
    protected <K> EntityDAO<K> createEntityDAO(Class<K> entityClass) {
        return entityDAOFactory.newDAO(entityClass);
    }

    /**
     * Search for an entity by its ID
     *
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
     *
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
     *
     * @param prevVals
     * @param newVals
     * @return
     */
    protected Diffs createDiffs(ObjectValues prevVals, ObjectValues newVals) {
        return DiffsUtils.generateDiffsFromValues(prevVals, newVals);
    }


    /**
     * Return the current workspace ID
     *
     * @return instance of UUID
     */
    protected UUID getWorkspaceId() {
        return userRequestService.getUserSession().getWorkspaceId();
    }

    /**
     * Create the result to be returned by the create, update or delete operation
     *
     * @param entity the entity involved in the operation
     * @return instance of {@link ServiceResult}
     */
    protected ServiceResult createResult(E entity) {
        ServiceResult res = new ServiceResult();

        res.setId(entity.getId());
        res.setEntityClass(getEntityClass());

        String cmdPath = getCommandType();
        res.setCommandType(CommandTypes.get(cmdPath));

        if (entity instanceof Displayable) {
            res.setEntityName(((Displayable) entity).getDisplayString());
        } else if (entity instanceof CaseEntity) {
            CaseEntity caseEntity = (CaseEntity) entity;
            res.setEntityName(caseEntity.getTbcase().getDisplayString());
            res.setParentId(caseEntity.getTbcase().getId());
        } else {
            res.setEntityName(entity.toString());
        }

        return res;
    }


    /**
     * Return the entity by its given id
     *
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
     *
     * @param entity the entity assgined to the binding result
     * @return instance of {@link BindingResult}
     */
    protected BindingResult createBindingResult(Object entity) {
        return new BeanPropertyBindingResult(entity, getEntityClass().getSimpleName());
    }


    /**
     * Raise exception of a required field not present
     *
     * @param obj   the object related to the exception
     * @param field the name of the field
     */
    protected void raiseRequiredFieldException(Object obj, String field) {
        rejectFieldException(obj, field, Messages.REQUIRED);
    }

    /**
     * Raise a validation error exception. The exception is thrown immediately and there will be
     * just one field assigned to it
     *
     * @param obj   the object related to the validation error
     * @param field the field with an error
     * @param msg   the message with the validation error
     */
    protected void rejectFieldException(Object obj, String field, String msg) {
        BindingResult res = createBindingResult(obj);
        res.rejectValue(field, msg);
        throw new EntityValidationException(res);
    }


    /**
     * Return the class of the entity being managed
     *
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
     *
     * @param req the object used as request for this service
     * @return
     */
    protected E createEntityInstance(Object req) {
        Class<E> clazz = getEntityClass();
        return ObjectUtils.newInstance(clazz);
    }

    @Override
    public QueryResult findMany(Q qryParams) {
        QueryBuilder<E> builder = queryBuilderFactory.createQueryBuilder(getEntityClass());

        buildQuery(builder, qryParams);

        builder.initialize(qryParams);

        return builder.createQueryResult();
    }

    /**
     * Return the entity manager in use
     *
     * @return instance of {@link EntityManager}
     */
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Called when the method {@link EntityServiceImpl#findMany(EntityQueryParams)} is invoked, in order to
     * build the query that will return the entities
     *
     * @param builder     instance of the {@link QueryBuilder}
     * @param queryParams the parameters sent from the client to return the entities
     */
    protected void buildQuery(QueryBuilder<E> builder, Q queryParams) {
        // nothing to do
    }
}
