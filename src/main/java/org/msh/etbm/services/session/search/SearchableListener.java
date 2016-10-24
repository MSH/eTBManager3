package org.msh.etbm.services.session.search;

import org.msh.etbm.commons.entities.EntityServiceEvent;
import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.db.entities.Searchable;
import org.msh.etbm.db.enums.SearchableType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listener that creates, updates or remove a searchable based on an entity
 * Created by Mauricio on 05/10/2016.
 */
@Component
public class SearchableListener extends SearchableBuilder {

    /**
     * Called when a Searchable entity was created
     * @param event
     */
    @Transactional
    @TransactionalEventListener(condition = "T(org.msh.etbm.services.session.search.SearchableListener).isCreatingSearchable(#event)")
    public void copyToSearchable(EntityServiceEvent event) {
        Object entity = entityManager.find(event.getResult().getEntityClass(), event.getResult().getId());

        if (entity == null) {
            throw new RuntimeException("Entity doesn't exists");
        }

        Searchable searchable = buildSearchable(entity);

        entityManager.persist(searchable);
    }

    /**
     * Called when a Searchable entity was updated
     * @param event
     */
    @Transactional
    @TransactionalEventListener(condition = "T(org.msh.etbm.services.session.search.SearchableListener).isUpdatingSearchable(#event)")
    public void updateSearchable(EntityServiceEvent event) {
        Searchable searchable = entityManager.find(Searchable.class, event.getResult().getId());
        Object entity = entityManager.find(event.getResult().getEntityClass(), event.getResult().getId());

        if (entity == null) {
            throw new RuntimeException("Entity doesn't exists");
        }

        searchable = buildSearchable(entity, searchable);

        entityManager.persist(searchable);
    }

    /**
     * Called when a Searchable entity was removed
     * @param event
     */
    @Transactional
    @TransactionalEventListener(condition = "T(org.msh.etbm.services.session.search.SearchableListener).isRemovingSearchable(#event)")
    public void removeSearchable(EntityServiceEvent event) {
        Searchable searchable = entityManager.find(Searchable.class, event.getResult().getId());
        if (searchable != null) {
            entityManager.remove(searchable);
        }
    }

    /**
     * Checks if a searchable entity is being created based on the service result
     * @param event
     * @return
     */
    public static boolean isCreatingSearchable(EntityServiceEvent event) {
        if (event == null || event.getResult() == null || !Operation.NEW.equals(event.getResult().getOperation())) {
            return false;
        }

        return isSearchableEntity(event);
    }

    /**
     * Checks if a searchable entity is being updated based on the service result
     * @param event
     * @return
     */
    public static boolean isUpdatingSearchable(EntityServiceEvent event) {
        if (event == null || event.getResult() == null || !Operation.EDIT.equals(event.getResult().getOperation())) {
            return false;
        }

        return isSearchableEntity(event);
    }

    /**
     * Checks if a searchable entity is being removed based on the service result
     * @param event
     * @return
     */
    public static boolean isRemovingSearchable(EntityServiceEvent event) {
        if (event == null || event.getResult() == null || !Operation.DELETE.equals(event.getResult().getOperation())) {
            return false;
        }

        return isSearchableEntity(event);
    }

    /**
     * Checks if the entity is a searchable entity
     * @param event
     * @return true if it is a searchable
     */
    private static boolean isSearchableEntity(EntityServiceEvent event) {
        if (event.getResult().getEntityClass() == null) {
            return false;
        }

        String entityClassName = event.getResult().getEntityClass().getSimpleName();

        // check if is a searchable entity
        for (SearchableType s : SearchableType.values()) {
            if (entityClassName.equals(s.getEntityClassName()) || entityClassName.equals(s.getParentClassName())) {
                return true;
            }
        }

        return false;
    }
}
