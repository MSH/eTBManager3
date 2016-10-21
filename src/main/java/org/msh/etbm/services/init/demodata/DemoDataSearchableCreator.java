package org.msh.etbm.services.init.demodata;

import org.msh.etbm.db.entities.Searchable;
import org.msh.etbm.services.session.search.SearchableBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mauricio on 21/10/2016.
 */
@Component
public class DemoDataSearchableCreator extends SearchableBuilder {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void create(Class entityClass, UUID workspaceId) {
        List<Object> lst = entityManager.createQuery(" from " + entityClass.getSimpleName())
                .getResultList();

        if (lst == null || lst.size() < 1) {
            return;
        }

        for (Object o : lst) {
            Searchable s = super.buildSearchable(o, workspaceId);

            entityManager.persist(s);
        }

        entityManager.flush();
    }
}
