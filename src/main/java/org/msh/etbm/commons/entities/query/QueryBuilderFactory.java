package org.msh.etbm.commons.entities.query;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.services.usersession.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Simple factory class to create query builders
 * Created by rmemoria on 28/10/15.
 */
@Component
public class QueryBuilderFactory {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserSession userSession;

    @Autowired
    DozerBeanMapper mapper;

    /**
     * Create a query builder
     * @param entityClass the entity class to be queried
     * @return instance of the {@link QueryBuilder}
     */
    public <E> QueryBuilder<E> createQueryBuilder(Class<E> entityClass) {
        QueryBuilderImpl builder = new QueryBuilderImpl(entityClass);
        builder.setEntityManager(entityManager);
        builder.setUserSession(userSession);
        builder.setMapper(mapper);

        return builder;
    }
}
