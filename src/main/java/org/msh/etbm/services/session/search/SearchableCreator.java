package org.msh.etbm.services.session.search;

import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.db.entities.*;
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

    /**
     * Deletes all existing searchables and then, creates NEW searchable registers for all searchable entities
     */
    @Transactional
    public void updateAllSearchables() {
        entityManager.createQuery("delete from Searchable").executeUpdate();

        this.createNewSearchables(Workspace.class);
        this.createNewSearchables(AdministrativeUnit.class);
        this.createNewSearchables(Unit.class);
        this.createNewSearchables(TbCase.class);
    }
}
