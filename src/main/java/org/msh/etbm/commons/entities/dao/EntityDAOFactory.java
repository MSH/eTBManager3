package org.msh.etbm.commons.entities.dao;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Factory class to generate new instances of {@link EntityDAO} classes
 * Created by rmemoria on 8/4/16.
 */
@Component
public class EntityDAOFactory {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    Validator validator;

    @Autowired
    UserRequestService userRequestService;

    /**
     * Create a new instance of {@link EntityDAO} for the given entity class
     * @param entityClass The entity class to be managed by the {@link EntityDAO}
     * @param <E> The generic class of the entity (the same used in entityClass)
     * @return instance of {@link EntityDAO}
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public <E> EntityDAO<E> newDAO(Class<E> entityClass) {
        EntityDAO<E> dao = new EntityDAO<>(entityClass, entityManager, mapper, validator, userRequestService);
        return dao;
    }
}
