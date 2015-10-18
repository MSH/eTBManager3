package org.msh.etbm.commons.entities;

import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.forms.DiffValue;
import org.msh.etbm.commons.forms.FormValues;
import org.msh.etbm.db.Synchronizable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 17/10/15.
 */
public abstract class EntityService<E extends Synchronizable, R extends CrudRepository> {

    @Autowired
    ApplicationContext applicationContext;

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
     * @param values list of all values in the format field x value
     * @return the primary key generated for the entity
     */
    @Transactional
    public UUID createEntity(FormValues values) {
        CrudRepository rep = getCrudRepository();

        return null;
    }

    /**
     * Update the values of the entity
     * @param id
     * @param values
     * @return
     */
    @Transactional
    public List<DiffValue> updateEntity(UUID id, FormValues values) {
        return null;
    }

    @Transactional
    public E removeEntity(UUID id) {
        return null;
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

}
