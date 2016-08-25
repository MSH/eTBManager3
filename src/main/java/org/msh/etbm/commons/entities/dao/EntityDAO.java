package org.msh.etbm.commons.entities.dao;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.Synchronizable;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.UUID;

/**
 * Simple DAO for entities. Different from the others, this one retains entity state (the entity
 * and validation errors are stored inside the class) and supports validation, user workspace
 * and Dozer mapping.
 * <p>
 * This one is not intended to replace {@link org.msh.etbm.commons.entities.EntityService}, but as
 * a composition solution when simple CRUD operations are required without creating service inheritance.
 * <p>
 * EntityDAO is not a Spring component. Instances of EntityDAO must be created using the
 * {@link EntityDAOFactory#newDAO(Class)} method. {@link EntityDAOFactory} is a Spring component, and
 * can be easily injected in components
 * <p>
 * Created by rmemoria on 8/4/16.
 */
public class EntityDAO<E> {

    /**
     * The entity being managed
     */
    private E entity;

    /**
     * The entity class
     */
    private Class entityClass;

    /**
     * The list of validation errors
     */
    private BindingResult errors;

    // these components are passed by EntityDAOFactory during creation of a new instance
    private EntityManager entityManager;
    private DozerBeanMapper mapper;
    private Validator validator;
    private UserRequestService userRequestService;

    /**
     * The default constructor, used by {@link EntityDAOFactory}
     *
     * @param entityClass        The class of the entity being managed
     * @param entityManager      instance of EntityManager to be injected by the factory class
     * @param mapper             used when copying data from DTO to the entity using Dozer
     * @param validator          used when validating the content of the object
     * @param userRequestService used when getting the workspace in use (just for {@link Synchronizable} objects)
     */
    protected EntityDAO(Class entityClass, EntityManager entityManager, DozerBeanMapper mapper,
                        Validator validator, UserRequestService userRequestService) {
        this.entityClass = entityClass;
        this.entityManager = entityManager;
        this.mapper = mapper;
        this.validator = validator;
        this.userRequestService = userRequestService;
    }

    /**
     * Return the entity class being managed
     *
     * @return Class of the entity being managed
     */
    public Class<E> getEntityClass() {
        return (Class<E>) entityClass;
    }

    /**
     * Set the ID of the entity being managed. Force the entity to be reloaded
     *
     * @param id the entity ID
     */
    public void setId(Object id) {
        // is a null entity ?
        if (id == null) {
            // erase it
            entity = null;
        } else {
            // load the entity
            entity = (E) entityManager.find(getEntityClass(), id);
            if (entity == null) {
                throw new EntityNotFoundException("Entity of class " + entityClass.getSimpleName() + " not found with id = " + id);
            }
        }
        clearValidationErrors();
    }

    /**
     * Return the ID of the entity being managed
     *
     * @return the ID of the entity, or null if it is a new entity
     */
    public Object getId() {
        return ObjectUtils.getProperty(entity, "id");
    }

    /**
     * First check if the entity exists. If so, set the ID (and the entity) to the one loaded.
     * If entity doesn't exist, don't change the state and return false
     *
     * @param id the ID of the entity to be loaded
     * @return true if entity was found, or false if entity was not found
     */
    public boolean setIdIfExists(Object id) {
        // if ID is null, just call the regular setId method
        if (id == null) {
            setId(id);
            return true;
        }

        // search for the entity
        E ent = entityManager.find(getEntityClass(), id);

        // entity was found ?
        if (ent != null) {
            entity = ent;
            clearValidationErrors();
        }

        return ent != null;
    }

    /**
     * Copy the values of the given object properties to the entity using Dozer mapping
     *
     * @param data the object to copy property values from
     */
    public void mapToEntity(Object data) {
        mapper.map(data, getEntity());
    }

    /**
     * Map the values of the managed entity to a new instance of the given class
     *
     * @param classData the class of an object that will be created and will receive property values from entity object
     * @param <E>
     * @return The instance of the class data
     */
    public <E> E mapFromEntity(Class<E> classData) {
        return mapper.map(getEntity(), classData);
    }

    /**
     * Return the entity being managed
     *
     * @return instance of entity class
     */
    public E getEntity() {
        if (entity == null) {
            entity = (E) ObjectUtils.newInstance(getEntityClass());
        }

        return entity;
    }

    /**
     * Change the entity being managed by the DAO
     *
     * @param entity the new entity to be replaced
     */
    public void setEntity(E entity) {
        this.entity = entity;
        clearValidationErrors();
    }

    /**
     * Clear all error messages stored in this DAO
     */
    public void clearValidationErrors() {
        errors = null;
    }

    /**
     * Validate the entity being managed. Validation errors can be retrieved using {@link EntityDAO#getErrors()} method
     *
     * @return true if there is no validation error
     */
    public boolean validate() {
        errors = null;
        checkWorkspace();
        validator.validate(entity, getErrors());
        return !getErrors().hasErrors();
    }

    /**
     * Check if there is any validation error
     *
     * @return true if there are validation errors, or false if there is no error by now
     */
    public boolean hasErrors() {
        return errors != null ? errors.hasErrors() : false;
    }

    /**
     * Return true if the entity is valid. An entity is valid if there is no validation error message
     *
     * @return true if it is a valid object, or false if there are validation error messages
     */
    public boolean isValid() {
        return !getErrors().hasErrors();
    }

    /**
     * Return true if it is a new entity, i.e, it doesn't exist in the database
     *
     * @return true if it is a new entity, or false if this entity is managed by the entity manager
     */
    public boolean isNew() {
        return !entityManager.contains(getEntity());
    }

    /**
     * Return the object containing all error messages of the entity. Messages are stored using Spring
     * BindingResult object
     *
     * @return instance of BindingResult
     */
    public Errors getErrors() {
        if (errors == null) {
            errors = new BeanPropertyBindingResult(entity, entityClass.getSimpleName());
        }
        return errors;
    }


    /**
     * If there are error messages, an {@link EntityValidationException} is thrown with all messages. It is
     * expected that messages will be handled by the application somewhere
     */
    public void raiseValidationError() {
        if (!isValid()) {
            throw new EntityValidationException(errors);
        }
    }

    /**
     * Save the entity. Before saving it, check if there is any error message or if validation fails. In case
     * of validation error messages, an {@link EntityValidationException} is thrown
     */
    public void save() {
        // check if it is a valid entity to be saved
        if (!isValid() || !validate()) {
            raiseValidationError();
        }

        // save the entity
        entityManager.persist(entity);
        entityManager.flush();
    }

    /**
     * Delete the entity. The entity must be managed by the entity Manager, otherwise an exception will be thrown
     */
    public void delete() {
        if (isNew()) {
            throw new RuntimeException("Cannot delete an entity that doesn't exist");
        }

        checkWorkspace();

        entityManager.remove(entity);
        entityManager.flush();
    }

    /**
     * Check if the entity workspace is the same as the current workspace. If not, an error is added
     */
    protected void checkSameWorkspace() {
        if (!(entity instanceof WorkspaceEntity)) {
            return;
        }
        WorkspaceEntity wsent = (WorkspaceEntity) entity;

        UUID id = userRequestService.getUserSession().getWorkspaceId();

        if (!wsent.getWorkspace().getId().equals(id)) {
            addError("workspace", Messages.NOT_VALID_WORKSPACE);
        }
    }

    /**
     * Check if entity is segmented by workspaces. If so, set the user workspace if none was specified
     */
    protected void checkWorkspace() {
        // doesn't support workspaces ?
        if (!(entity instanceof WorkspaceEntity)) {
            return;
        }

        // workspace was already set ?
        WorkspaceEntity wsent = (WorkspaceEntity) entity;
        if (wsent.getWorkspace() != null) {
            // check if is the same workspace
            checkSameWorkspace();
            return;
        }

        // get the current selected workspace
        UUID wsid = userRequestService.getUserSession().getWorkspaceId();

        if (wsid == null) {
            return;
        }


        // set workspace in the entity
        Workspace ws = entityManager.find(Workspace.class, wsid);
        wsent.setWorkspace(ws);
    }

    /**
     * Add a 'Not null' standard message to the list of error messages to the given field
     *
     * @param field
     */
    public void addNotNullError(String field) {
        getErrors().reject(field, Messages.REQUIRED);
    }

    /**
     * Add an error message to a given field
     *
     * @param field the field with an error
     * @param msg   the message with the validation error
     */
    public void addError(String field, String msg) {
        getErrors().rejectValue(field, msg);
    }

    /**
     * Add a global validation error message
     *
     * @param msg the key message to add
     */
    public void addError(String msg) {
        getErrors().reject(msg);
    }
}
