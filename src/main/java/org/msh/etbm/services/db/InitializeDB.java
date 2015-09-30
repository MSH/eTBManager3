package org.msh.etbm.services.db;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Called when application starts up in order to initialize data in the database, like
 * the list of user roles
 * Created by rmemoria on 2/9/15.
 */
@Component
public class InitializeDB implements ApplicationListener<ContextRefreshedEvent> {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Called on application startup
     * @param contextRefreshedEvent
     */
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        InitRoles init = new InitRoles();
        init.execute(entityManager);
    }


}
