package org.msh.etbm.services.session.search;

import org.msh.etbm.commons.PersonNameUtils;
import org.msh.etbm.commons.entities.EntityServiceEvent;
import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.db.Synchronizable;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.db.enums.SearchableType;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Listener that creates, updates or remove a searchable based on an entity
 * Created by Mauricio on 05/10/2016.
 */
@Component
public class SearchableListener {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    PersonNameUtils personNameUtils;

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

        Searchable searchable = buildSearchable(entity, null);

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
     * Builds a searchable checking the type of the entity
     * If param searchable is null a new searchable will be instantiated
     * @param entity
     * @param searchable
     * @return
     */
    private Searchable buildSearchable(Object entity, Searchable searchable) {
        // if searchable is null a searchable is being created
        if (searchable == null) {
            Workspace workspace = entityManager.find(Workspace.class, userRequestService.getUserSession().getWorkspaceId());

            searchable = new Searchable();
            searchable.setWorkspace(workspace);
            searchable.setId(((Synchronizable) entity).getId());
        }

        if (entity instanceof TbCase) {
            return buildSearchable((TbCase)entity, searchable);
        }

        if (entity instanceof Tbunit) {
            return buildSearchable((Tbunit)entity, searchable);
        }

        if (entity instanceof Laboratory) {
            return buildSearchable((Laboratory)entity, searchable);
        }

        if (entity instanceof AdministrativeUnit) {
            return buildSearchable((AdministrativeUnit)entity, searchable);
        }

        if (entity instanceof Workspace) {
            return buildSearchable((Workspace)entity, searchable);
        }

        throw new RuntimeException("Entity is not a searchable");
    }

    /**
     * Builds a TbCase searchable
     * @param entity
     * @param searchable
     * @return
     */
    private Searchable buildSearchable(TbCase entity, Searchable searchable) {
        searchable.setType("MALE".equals(entity.getPatient().getGender()) ? SearchableType.CASE_MAN : SearchableType.CASE_WOMAN );
        searchable.setUnit(entity.getOwnerUnit());
        searchable.setSubtitle(entity.getDiagnosisType().equals(DiagnosisType.SUSPECT) ? entity.getRegistrationNumber() : entity.getCaseNumber());
        searchable.setTitle(personNameUtils.displayPersonName(entity.getPatient().getName()));

        return searchable;
    }

    /**
     * Builds a Tbunit searchable
     * @param entity
     * @param searchable
     * @return
     */
    private Searchable buildSearchable(Tbunit entity, Searchable searchable) {
        searchable.setType(SearchableType.TBUNIT);
        searchable.setUnit(entity);
        searchable.setSubtitle(entity.getAddress().getAdminUnit().getFullDisplayName());
        searchable.setTitle(entity.getName());

        return searchable;
    }

    /**
     * Builds a Laboratory searchable
     * @param entity
     * @param searchable
     * @return
     */
    private Searchable buildSearchable(Laboratory entity, Searchable searchable) {
        searchable.setType(SearchableType.LAB);
        searchable.setUnit(entity);
        searchable.setSubtitle(entity.getAddress().getAdminUnit().getFullDisplayName());
        searchable.setTitle(entity.getName());

        return searchable;
    }

    /**
     * Builds a AdministrativeUnit searchable
     * @param entity
     * @param searchable
     * @return
     */
    private Searchable buildSearchable(AdministrativeUnit entity, Searchable searchable) {
        searchable.setType(SearchableType.ADMINUNIT);
        searchable.setUnit(null);
        searchable.setSubtitle(null);
        searchable.setTitle(entity.getFullDisplayName());

        return searchable;
    }

    /**
     * Builds a Workspace searchable
     * @param entity
     * @param searchable
     * @return
     */
    private Searchable buildSearchable(Workspace entity, Searchable searchable) {
        searchable.setType(SearchableType.WORKSPACE);
        searchable.setUnit(null);
        searchable.setSubtitle(null);
        searchable.setTitle(entity.getName());

        return searchable;
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
