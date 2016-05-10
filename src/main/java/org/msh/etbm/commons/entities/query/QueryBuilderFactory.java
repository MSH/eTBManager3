package org.msh.etbm.commons.entities.query;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.services.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

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
    UserRequestService userRequestService;

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    PlatformTransactionManager transactionManager;


    /**
     * Create a query builder
     * @param entityClass the entity class to be queried
     * @return instance of the {@link QueryBuilder}
     */
    public <E> QueryBuilder<E> createQueryBuilder(Class<E> entityClass) {
        return createQueryBuilder(entityClass, null);
    }


    /**
     * Create a query builder
     * @param entityClass the entity class to be queried
     * @param path the alias used as the table path in the query
     * @return instance of the {@link QueryBuilder}
     */
    public <E> QueryBuilder<E> createQueryBuilder(Class<E> entityClass, String path) {
        QueryBuilderImpl<E> builder = new QueryBuilderImpl(entityClass, path);
        builder.setEntityManager(entityManager);
        builder.setTransactionManager(transactionManager);
        builder.setUserRequestService(userRequestService);
        if (userRequestService.isAuthenticated()) {
            builder.setWorkspaceId(userRequestService.getUserSession().getWorkspaceId());
        }
        builder.setMapper(mapper);

        return builder;
    }

}
