package org.msh.etbm.services.session.search;

import org.msh.etbm.db.entities.Searchable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Mauricio on 21/10/2016.
 */
@Component
public class SearchableCreator extends SearchableBuilder {

    /**
     * Creates NEW searchable registers for the class passed as parameters
     * @param entityClass
     */
    @Transactional
    public void createNewSearchables(Class entityClass) {
        List<Object> lst = entityManager.createQuery(" from " + entityClass.getSimpleName())
                .getResultList();

        if (lst == null || lst.size() < 1) {
            return;
        }

        for (Object o : lst) {
            Searchable s = super.buildSearchable(o);

            entityManager.persist(s);
        }

        entityManager.flush();
    }

}
